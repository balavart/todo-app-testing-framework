package com.example.todoapp.utils;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.fixtures.TestDataConstants;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * Factory for creating test data for TodoItems.
 */
public class TestDataFactory implements TestDataConstants {

    private static final ConcurrentSkipListSet<Long> usedIds = new ConcurrentSkipListSet<>();

    public static TodoItem getValidTodoItem() {
        return createTodoItem(DEFAULT_TODO_TEXT + generateUniqueId());
    }

    public static TodoItem getTodoItemWithSpecialCharacters() {
        return createTodoItem(SPECIAL_CHARACTERS_TEXT);
    }

    public static TodoItem getTodoItemWithSQLInjection() {
        return createTodoItem(SQL_INJECTION_TEXT);
    }

    public static TodoItem getTodoItemWithXSS() {
        return createTodoItem(XSS_TEXT);
    }

    public static TodoItem getTodoItemWithLargeText() {
        String largeText = "Test text ".repeat(LARGE_TEXT_REPEAT_COUNT).trim();
        return createTodoItem(largeText);
    }

    private static Long generateUniqueId() {
        return Stream.generate(() -> ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE))
                .filter(usedIds::add)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to generate unique ID"));
    }

    private static TodoItem createTodoItem(String text) {
        Long id = generateUniqueId();
        return new TodoItem(id, text, false);
    }

}