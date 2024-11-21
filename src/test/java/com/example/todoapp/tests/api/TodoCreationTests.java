package com.example.todoapp.tests.api;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.tests.BaseTodoTestingApplicationTests;
import com.example.todoapp.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.testng.Tag;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Tests related to Todo creation.
 */
public class TodoCreationTests extends BaseTodoTestingApplicationTests {

    @Test(description = "Create valid todo", groups = {"positive"})
    @Tag("positive")
    @Description("Verify creating valid todo")
    public void testCreateValidTodo() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();
        createAndVerifyTodo(todoItem);
    }

    @Test(description = "Create todo with missing fields", groups = {"negative"})
    @Tag("negative")
    @Description("Verify API response when creating a todo with missing fields")
    public void testCreateTodoWithMissingFields() {
        TodoItem invalidTodo = new TodoItem();
        int statusCode = createTodoExpectingFailure(invalidTodo);
        Assert.assertEquals(statusCode, HttpStatus.SC_BAD_REQUEST, "Status code does not match expected");
    }

    @Test(description = "Create todo with special characters", groups = {"positive"})
    @Tag("positive")
    @Description("Verify creating a todo with special characters")
    public void testCreateTodoWithSpecialCharacters() {
        TodoItem todoItem = TestDataFactory.getTodoItemWithSpecialCharacters();
        createAndVerifyTodo(todoItem);
    }

    @Test(description = "Create todo wit large text", groups = {"positive"}, enabled = false)
    @Tag("positive")
    @Description("Verify creating todo with a very large text")
    public void testCreateTodoWithLargeText() {
        TodoItem todoItem = TestDataFactory.getTodoItemWithLargeText();
        createAndVerifyTodo(todoItem);
    }

    @Step("Create a todo and verify it exists")
    private void createAndVerifyTodo(TodoItem todoItem) {
        todoApiClient.createTodo(todoItem);

        List<TodoItem> todos = todoApiClient.getTodos(DEFAULT_OFFSET, DEFAULT_LIMIT);
        Assert.assertFalse(todos.isEmpty(), "Todo list is empty after creation");

        TodoItem createdTodo = todos.stream()
                .filter(todo -> todo.getText().equals(todoItem.getText()))
                .findFirst()
                .orElse(null);

        Assert.assertNotNull(createdTodo, "Created todo not found in the list");
        Assert.assertEquals(createdTodo.getText(), todoItem.getText(), "Todo text does not match");
    }

    @Step("Attempt to create an invalid todo and expect failure")
    private int createTodoExpectingFailure(TodoItem todoItem) {
        return todoApiClient.createTodoExpectingStatusCode(todoItem);
    }

}