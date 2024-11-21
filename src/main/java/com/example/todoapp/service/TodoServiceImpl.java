package com.example.todoapp.service;

import com.example.todoapp.clients.TodoApiClient;
import com.example.todoapp.model.TodoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link TodoService} interface.
 */
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoApiClient todoApiClient;

    @Override
    public List<TodoItem> getTodos(int offset, int limit) {
        return todoApiClient.getTodos(offset, limit);
    }

    @Override
    public void createTodo(TodoItem todoItem) {
        todoApiClient.createTodo(todoItem);
    }

    @Override
    public void updateTodo(Long id, TodoItem todoItem) {
        todoApiClient.updateTodo(id, todoItem);
    }

    @Override
    public void deleteTodo(Long id) {
        todoApiClient.deleteTodo(id);
    }

}