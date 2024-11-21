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

/**
 * Tests related to application security.
 */
public class TodoSecurityTests extends BaseTodoTestingApplicationTests {

    @Test(description = "Attempt SQL Injection", groups = {"negative", "security"})
    @Tag("negative")
    @Description("Verify API security against SQL Injection")
    public void testSqlInjectionAttempt() {
        TodoItem sqlInjectionTodo = TestDataFactory.getTodoItemWithSQLInjection();
        createTodoAndVerify(sqlInjectionTodo);
    }

    @Test(description = "Test XSS in text field", groups = {"negative", "security"})
    @Tag("negative")
    @Description("Verify API security against XSS attacks")
    public void testXssInTextField() {
        TodoItem xssTodo = TestDataFactory.getTodoItemWithXSS();
        createTodoAndVerify(xssTodo);
    }

    @Step("Create a Todo item and verify it exists in the list")
    private void createTodoAndVerify(TodoItem todoItem) {
        createTodo(todoItem);
        List<TodoItem> todos = fetchAllTodos();
        assertTodoExists(todos, todoItem);
    }

    @Step("Create a Todo item: {todoItem.text}")
    private void createTodo(TodoItem todoItem) {
        todoApiClient.createTodo(todoItem);
    }

    @Step("Fetch all todos")
    private List<TodoItem> fetchAllTodos() {
        return todoApiClient.getTodos(DEFAULT_OFFSET, DEFAULT_LIMIT);
    }

    @Step("Verify that todo item exists in the list: {expectedTodo.text}")
    private void assertTodoExists(List<TodoItem> todos, TodoItem expectedTodo) {
        boolean exists = todos.stream().anyMatch(todo -> todo.getText().equals(expectedTodo.getText()));
        Assert.assertTrue(exists, "Expected todo not found in the list.");
    }

}