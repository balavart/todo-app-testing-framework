package com.example.todoapp.tests.api;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.tests.BaseTodoTestingApplicationTests;
import com.example.todoapp.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.testng.Tag;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;

/**
 * Tests related to Todo updating.
 */
public class TodoUpdateTests extends BaseTodoTestingApplicationTests {

    @Test(description = "Update an existing todo", groups = {"positive"})
    @Tag("positive")
    @Description("Verify updating an existing todo")
    public void testUpdateExistingTodo() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();
        TodoItem createdTodo = createAndVerifyTodo(todoItem);

        TodoItem updatedTodo = updateTodoAndVerify(createdTodo, "Updated Text", true);

        Assert.assertEquals(updatedTodo.getText(), "Updated Text", "Todo text was not updated");
        Assert.assertTrue(updatedTodo.getCompleted(), "Todo completion status was not updated");
    }

    @Test(description = "Update non-existent todo", groups = {"negative"})
    @Tag("negative")
    @Description("Verify API response when updating a non-existent todo")
    public void testUpdateNonExistentTodo() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();
        int statusCode = updateTodoExpectingFailure(NON_EXISTENT_TODO_ID, todoItem);
        Assert.assertEquals(statusCode, SC_NOT_FOUND, "Status code does not match expected");
    }

    @Test(description = "Update todo with invalid ID format", groups = {"negative"})
    @Tag("negative")
    @Description("Verify API response when updating a todo with invalid ID format")
    public void testUpdateTodoWithInvalidIdFormat() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();
        int statusCode = updateTodoWithInvalidIdFormatExpectingFailure("invalid-id", todoItem);
        Assert.assertEquals(statusCode, SC_NOT_FOUND, "Status code does not match expected");
    }

    @Step("Create and verify a Todo item")
    private TodoItem createAndVerifyTodo(TodoItem todoItem) {
        todoApiClient.createTodo(todoItem);
        List<TodoItem> todos = todoApiClient.getTodos(DEFAULT_OFFSET, DEFAULT_LIMIT);

        TodoItem createdTodo = todos.stream()
                .filter(todo -> todo.getText().equals(todoItem.getText()))
                .findFirst()
                .orElse(null);

        Assert.assertNotNull(createdTodo, "Created todo not found");
        return createdTodo;
    }

    @Step("Update a Todo item with new text: {newText} and completion status: {completed}")
    private TodoItem updateTodoAndVerify(TodoItem todoItem, String newText, boolean completed) {
        todoItem.setText(newText);
        todoItem.setCompleted(completed);
        todoApiClient.updateTodo(todoItem.getId(), todoItem);

        List<TodoItem> todos = todoApiClient.getTodos(DEFAULT_OFFSET, DEFAULT_LIMIT);
        return todos.stream()
                .filter(todo -> todo.getId().equals(todoItem.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Updated todo not found"));
    }

    @Step("Attempt to update a non-existent todo")
    private int updateTodoExpectingFailure(Long todoId, TodoItem todoItem) {
        return todoApiClient.updateTodoExpectingStatusCode(todoId, todoItem);
    }

    @Step("Attempt to update a todo with invalid ID format: {invalidId}")
    private int updateTodoWithInvalidIdFormatExpectingFailure(String invalidId, TodoItem todoItem) {
        return todoApiClient.updateTodoWithInvalidIdFormatExpectingStatusCode(invalidId, todoItem);
    }

}