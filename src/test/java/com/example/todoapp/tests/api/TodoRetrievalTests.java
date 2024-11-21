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
import java.util.stream.IntStream;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

/**
 * Tests related to Todo retrieval.
 */
public class TodoRetrievalTests extends BaseTodoTestingApplicationTests {

    @Test(description = "Retrieve empty list of todos", groups = {"positive"})
    @Tag("positive")
    @Description("Verify retrieving an empty list of todos")
    public void testGetTodosEmptyList() {
        List<TodoItem> todos = retrieveTodos(DEFAULT_OFFSET, DEFAULT_LIMIT);
        Assert.assertTrue(todos.isEmpty(), "Todo list is not empty");
    }

    @Test(description = "Retrieve todos with items", groups = {"positive"})
    @Tag("positive")
    @Description("Verify retrieving a list of todos with items")
    public void testGetTodosWithItems() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();
        createTodo(todoItem);

        List<TodoItem> todos = retrieveTodos(DEFAULT_OFFSET, DEFAULT_LIMIT);
        Assert.assertFalse(todos.isEmpty(), "Todo list is empty");
        Assert.assertTrue(todos.stream().anyMatch(todo -> todo.getText().equals(todoItem.getText())),
                "Created todo not found in the list");
    }

    @Test(description = "Test offset and limit parameters", groups = {"positive"})
    @Tag("positive")
    @Description("Verify offset and limit query parameters")
    public void testGetTodosWithOffsetAndLimit() {
        createMultipleTodos(SMALL_TOTAL_TODOS);

        List<TodoItem> todos = retrieveTodos(SMALL_OFFSET, SMALL_LIMIT);
        Assert.assertEquals(todos.size(), EXPECTED_SMALL_SIZ, "Todo list size does not match expected");
        Assert.assertEquals(todos.get(0).getText(), "Todo 3", "First todo text does not match");
        Assert.assertEquals(todos.get(1).getText(), "Todo 4", "Second todo text does not match");
    }

    @Test(description = "Test large offset and limit values", groups = {"positive"})
    @Tag("positive")
    @Description("Verify handling of large offset and limit values")
    public void testGetTodosWithLargeOffsetAndLimit() {
        createMultipleTodos(LARGE_TOTAL_TODOS);

        List<TodoItem> todos = retrieveTodos(LARGE_OFFSET, LARGE_LIMIT);
        Assert.assertEquals(todos.size(), EXPECTED_LARGE_SIZE, "Todo list size does not match expected");
    }

    @Test(description = "Get todos with negative offset and limit", groups = {"negative"})
    @Tag("negative")
    @Description("Verify API response when using negative offset and limit values")
    public void testGetTodosWithNegativeOffsetAndLimit() {
        int statusCode = getTodosWithInvalidParametersExpectingFailure(INVALID_OFFSET, INVALID_LIMIT);
        Assert.assertEquals(statusCode, SC_BAD_REQUEST, "Status code does not match expected");
    }

    @Step("Retrieve todos with offset {offset} and limit {limit}")
    private List<TodoItem> retrieveTodos(int offset, int limit) {
        return todoApiClient.getTodos(offset, limit);
    }

    @Step("Create a single todo item")
    private void createTodo(TodoItem todoItem) {
        todoApiClient.createTodo(todoItem);
    }

    @Step("Create {totalTodos} todos")
    private void createMultipleTodos(int totalTodos) {
        IntStream.rangeClosed(START_ID, totalTodos)
                .mapToObj(this::createTodoItem)
                .forEach(this::createTodo);
    }

    @Step("Attempt to retrieve todos with invalid offset {offset} and limit {limit}")
    private int getTodosWithInvalidParametersExpectingFailure(int offset, int limit) {
        return todoApiClient.getTodosExpectingStatusCode(offset, limit);
    }

    private TodoItem createTodoItem(int id) {
        return new TodoItem((long) id, "Todo " + id, false);
    }

}