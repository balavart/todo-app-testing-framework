package com.example.todoapp.fixtures;

public interface TestDataConstants {
    int START_ID = 1;
    int SMALL_TOTAL_TODOS = 5;
    int SMALL_OFFSET = 2;
    int SMALL_LIMIT = 2;
    int LARGE_TOTAL_TODOS = 100;
    int LARGE_OFFSET = 90;
    int LARGE_LIMIT = 20;
    int MAX_ATTEMPTS = 5;
    int RETRY_DELAY_MS = 100;
    int EXPECTED_LARGE_SIZE = 10;
    int DEFAULT_OFFSET = 0;
    int DEFAULT_LIMIT = 10;
    int INVALID_OFFSET = -1;
    int INVALID_LIMIT = -5;
    long NON_EXISTENT_TODO_ID = 9999L;
    int EXPECTED_SMALL_SIZ = 2;
    String DEFAULT_TODO_TEXT = "Test Todo Item ";
    String SPECIAL_CHARACTERS_TEXT = "Test Todo 😊🚀";
    String SQL_INJECTION_TEXT = "Test'); DROP TABLE todos; --";
    String XSS_TEXT = "<script>alert('XSS')</script>";
    int LARGE_TEXT_REPEAT_COUNT = 100;
}