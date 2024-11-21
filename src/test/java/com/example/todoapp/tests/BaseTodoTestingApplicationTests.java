package com.example.todoapp.tests;

import com.example.todoapp.clients.TodoApiClient;
import com.example.todoapp.fixtures.TestDataConstants;
import com.example.todoapp.model.TodoItem;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

import java.util.List;

import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Base test class for tests.
 */
@Slf4j
@SpringBootTest
public class BaseTodoTestingApplicationTests extends AbstractTestNGSpringContextTests implements TestDataConstants {

    @Autowired
    protected TodoApiClient todoApiClient;

    @BeforeMethod(alwaysRun = true, groups = {"positive", "negative", "security"})
    public void setUp() {
        log.debug("Starting data cleanup before test: {}", this.getClass().getSimpleName());
        clearAllTodosWithVerification();
    }

    @AfterClass(alwaysRun = true, groups = {"positive", "negative"})
    public void cleanUp() {
        log.debug("Starting final data cleanup after tests in class: {}", this.getClass().getSimpleName());
        boolean cleared = retryUntilSuccess(this::clearAllTodosWithVerification, MAX_ATTEMPTS, RETRY_DELAY_MS);
        if (!cleared) {
            log.error("Failed to clear all Todos after {} attempts", MAX_ATTEMPTS);
            throw new RuntimeException("Failed to clear all Todos after maximum number of attempts");
        }
        log.debug("Successfully cleared all Todos after tests in class: {}", this.getClass().getSimpleName());
    }

    @Step("Clearing all Todos with verification")
    private void clearAllTodosWithVerification() {
        List<TodoItem> todos = todoApiClient.getTodos(DEFAULT_OFFSET, LARGE_TOTAL_TODOS);

        if (todos.isEmpty()) {
            log.trace("No Todos to delete");
            return;
        }

        log.trace("Found {} Todos to delete", todos.size());

        todos.parallelStream().forEach(todo -> {
            try {
                todoApiClient.deleteTodo(todo.getId());
                log.trace("Deleted Todo with id {}", todo.getId());
            } catch (Exception e) {
                log.warn("Failed to delete Todo with id {}: {}", todo.getId(), e.getMessage());
            }
        });

        await()
                .atMost(RETRY_DELAY_MS * MAX_ATTEMPTS, MILLISECONDS)
                .pollInterval(RETRY_DELAY_MS, MILLISECONDS)
                .until(this::isTodoListEmpty);
    }

    private boolean isTodoListEmpty() {
        List<TodoItem> todos = todoApiClient.getTodos(DEFAULT_OFFSET, LARGE_TOTAL_TODOS);
        log.trace("Checking if Todo list is empty. Current size: {}", todos.size());
        return todos.isEmpty();
    }

    private boolean retryUntilSuccess(Runnable action, int maxRetries, long delayMs) {
        try {
            await()
                    .atMost(delayMs * maxRetries, MILLISECONDS)
                    .pollInterval(delayMs, MILLISECONDS)
                    .untilAsserted(() -> {
                        action.run();
                    });
            return true;
        } catch (Exception e) {
            log.warn("Action failed after {} attempts: {}", maxRetries, e.getMessage());
            return false;
        }
    }

}