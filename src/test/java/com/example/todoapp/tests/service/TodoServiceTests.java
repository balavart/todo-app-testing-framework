package com.example.todoapp.tests.service;

import com.example.todoapp.clients.TodoApiClient;
import com.example.todoapp.model.TodoItem;
import com.example.todoapp.service.TodoServiceImpl;
import com.example.todoapp.tests.BaseTodoTestingApplicationTests;
import com.example.todoapp.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.testng.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TodoService.
 */
public class TodoServiceTests extends BaseTodoTestingApplicationTests {

    @MockBean
    private TodoApiClient todoApiClient;

    @Autowired
    private TodoServiceImpl todoService;

    @Test(description = "Test retrieving list of todos", groups = {"positive"})
    @Tag("positive")
    @Description("Verify retrieving list of todos")
    public void testGetTodos() {
        List<TodoItem> mockTodos = Arrays.asList(
                TestDataFactory.getValidTodoItem(),
                TestDataFactory.getTodoItemWithSpecialCharacters()
        );
        mockGetTodos(mockTodos);

        List<TodoItem> todos = retrieveTodos(DEFAULT_OFFSET, DEFAULT_LIMIT);

        Assert.assertNotNull(todos, "Todo list is null");
        Assert.assertEquals(todos.size(), mockTodos.size(), "Todo list size doesn't match");
    }

    @Test(description = "Test creating a todo", groups = {"positive"})
    @Tag("positive")
    @Description("Verify creating a todo")
    public void testCreateTodo() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();

        mockCreateTodo();
        createTodoAndVerify(todoItem);
    }

    @Test(description = "Test updating a todo", groups = {"positive"})
    @Tag("positive")
    @Description("Verify updating a todo")
    public void testUpdateTodo() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();
        todoItem.setText("Updated Text");

        mockUpdateTodo();
        updateTodoAndVerify(todoItem.getId(), todoItem);
    }

    @Test(description = "Test deleting a todo", groups = {"positive"})
    @Tag("positive")
    @Description("Verify deleting a todo")
    public void testDeleteTodo() {
        Long todoId = 1L;

        mockDeleteTodo();
        deleteTodoAndVerify(todoId);
    }

    @Test(description = "Test getTodos with invalid parameters", groups = {"negative"})
    @Tag("negative")
    @Description("Verify service response when getTodos is called with invalid parameters")
    public void testGetTodosWithInvalidParameters() {
        mockGetTodosWithException(new IllegalArgumentException("Invalid parameters"));

        expectException(() -> todoService.getTodos(INVALID_OFFSET, INVALID_LIMIT), IllegalArgumentException.class, "Invalid parameters");
    }

    @Test(description = "Test updating non-existent todo", groups = {"negative"})
    @Tag("negative")
    @Description("Verify service response when updating a non-existent todo")
    public void testUpdateNonExistentTodo() {
        TodoItem todoItem = TestDataFactory.getValidTodoItem();

        mockUpdateTodoWithException(todoItem.getId(), new RuntimeException("Todo not found"));
        expectException(() -> todoService.updateTodo(todoItem.getId(), todoItem), RuntimeException.class, "Todo not found");
    }

    @Test(description = "Test deleting non-existent todo", groups = {"negative"})
    @Tag("negative")
    @Description("Verify service response when deleting a non-existent todo")
    public void testDeleteNonExistentTodo() {
        Long todoId = NON_EXISTENT_TODO_ID;

        mockDeleteTodoWithException(todoId, new RuntimeException("Todo not found"));
        expectException(() -> todoService.deleteTodo(todoId), RuntimeException.class, "Todo not found");
    }

    @Step("Mock API to return a list of todos")
    private void mockGetTodos(List<TodoItem> mockTodos) {
        when(todoApiClient.getTodos(anyInt(), anyInt())).thenReturn(mockTodos);
    }

    @Step("Mock API to throw an exception")
    private void mockGetTodosWithException(Exception exception) {
        when(todoApiClient.getTodos(anyInt(), anyInt())).thenThrow(exception);
    }

    @Step("Mock API to allow creating a todo")
    private void mockCreateTodo() {
        doNothing().when(todoApiClient).createTodo(any(TodoItem.class));
    }

    @Step("Mock API to allow updating a todo")
    private void mockUpdateTodo() {
        doNothing().when(todoApiClient).updateTodo(anyLong(), any(TodoItem.class));
    }

    @Step("Mock API to throw exception when updating a todo")
    private void mockUpdateTodoWithException(Long todoId, Exception exception) {
        doThrow(exception).when(todoApiClient).updateTodo(eq(todoId), any(TodoItem.class));
    }

    @Step("Mock API to allow deleting a todo")
    private void mockDeleteTodo() {
        doNothing().when(todoApiClient).deleteTodo(anyLong());
    }

    @Step("Mock API to throw exception when deleting a todo")
    private void mockDeleteTodoWithException(Long todoId, Exception exception) {
        doThrow(exception).when(todoApiClient).deleteTodo(eq(todoId));
    }

    @Step("Retrieve todos")
    private List<TodoItem> retrieveTodos(int offset, int limit) {
        return todoService.getTodos(offset, limit);
    }

    @Step("Create a todo and verify")
    private void createTodoAndVerify(TodoItem todoItem) {
        todoService.createTodo(todoItem);
        verify(todoApiClient, times(1)).createTodo(eq(todoItem));
    }

    @Step("Update a todo and verify")
    private void updateTodoAndVerify(Long todoId, TodoItem todoItem) {
        todoService.updateTodo(todoId, todoItem);
        verify(todoApiClient, times(1)).updateTodo(eq(todoId), eq(todoItem));
    }

    @Step("Delete a todo and verify")
    private void deleteTodoAndVerify(Long todoId) {
        todoService.deleteTodo(todoId);
        verify(todoApiClient, times(1)).deleteTodo(eq(todoId));
    }

    @Step("Expect exception of type {expectedException}")
    private <T extends Throwable> void expectException(Runnable action, Class<T> expectedException, String expectedMessage) {
        try {
            action.run();
            Assert.fail("Expected exception of type " + expectedException.getName());
        } catch (Throwable e) {
            Assert.assertTrue(expectedException.isInstance(e), "Exception type mismatch");
            Assert.assertEquals(e.getMessage(), expectedMessage, "Exception message doe not match");
        }
    }

}