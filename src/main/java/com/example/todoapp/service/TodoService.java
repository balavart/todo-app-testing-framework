package com.example.todoapp.service;

import com.example.todoapp.model.TodoItem;

import java.util.List;

/**
 * Service interface for {@link TodoItem} operations.
 */
public interface TodoService {

    List<TodoItem> getTodos(int offset, int limit);

    void createTodo(TodoItem todoItem);

    void updateTodo(Long id, TodoItem todoItem);

    void deleteTodo(Long id);

}