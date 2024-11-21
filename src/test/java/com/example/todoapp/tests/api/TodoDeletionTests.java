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

import static org.apache.http.HttpStatus.*;

/**
 * Tests related to Todo deletion.
 */
public class TodoDeletionTests extends BaseTodoTestingApplicationTests {

    @Test(description = "Delete todo with authorization", groups = {"positive"})
    @Tag("positive")
    @Description("Verify deleting a todo with proper authorization")
    public void testDeleteTodoWithAuthorization() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();
        TodoItem createdTodo = createAndVerifyTodo(todoItem);

        deleteTodoAndVerify(createdTodo);
    }

    @Test(description = "Delete todo without authorization", groups = {"negative"})
    @Tag("negative")
    @Description("Verify API response when deleting a todo without authorization")
    public void testDeleteTodoWithoutAuthorization() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();
        TodoItem createdTodo = createAndVerifyTodo(todoItem);

        int statusCode = deleteTodoWithoutAuthorizationExpectingFailure(createdTodo.getId());
        Assert.assertEquals(statusCode, SC_UNAUTHORIZED, "Deletion without authorization did not return expected status code");
    }

    @Test(description = "Delete non-existent todo", groups = {"negative"})
    @Tag("negative")
    @Description("Verify API response when deleting a non-existent todo")
    public void testDeleteNonExistentTodo() {
        int statusCode = deleteNonExistentTodoExpectingFailure();
        Assert.assertEquals(statusCode, SC_NOT_FOUND, "Status code does not match expected");
    }

    @Step("Create a todo and verify its existence")
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

    @Step("Delete the todo and verify it is removed")
    private void deleteTodoAndVerify(TodoItem todoItem) {
        todoApiClient.deleteTodo(todoItem.getId());

        List<TodoItem> todos = todoApiClient.getTodos(DEFAULT_OFFSET, DEFAULT_LIMIT);
        boolean isTodoDeleted = todos.stream().noneMatch(todo -> todo.getId().equals(todoItem.getId()));

        Assert.assertTrue(isTodoDeleted, "Todo was not deleted");
    }

    @Step("Attempt to delete todo without authorization")
    private int deleteTodoWithoutAuthorizationExpectingFailure(Long todoId) {
        return todoApiClient.deleteTodoWithoutAuthorizationExpectingStatusCode(todoId);
    }

    @Step("Attempt to delete a not existent todo")
    private int deleteNonExistentTodoExpectingFailure() {
        return todoApiClient.deleteTodoExpectingStatusCode(NON_EXISTENT_TODO_ID);
    }

}