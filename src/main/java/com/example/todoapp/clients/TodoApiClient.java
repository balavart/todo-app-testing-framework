package com.example.todoapp.clients;

import com.example.todoapp.config.AppConfig;
import com.example.todoapp.model.TodoItem;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

/**
 * Client for interacting with the TodoApp API.
 */
@Component
@Slf4j
public class TodoApiClient {

    @Autowired
    private AppConfig appConfig;

    private RequestSpecification baseRequest() {
        return RestAssured.given()
                .baseUri(appConfig.getBaseUrl())
                .contentType(appConfig.getContentType());
    }

    public List<TodoItem> getTodos(int offset, int limit) {
        return baseRequest()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(appConfig.getTodosEndpoint())
                .then()
                .statusCode(SC_OK)
                .extract()
                .jsonPath()
                .getList(".", TodoItem.class);
    }

    public int getTodosExpectingStatusCode(int offset, int limit) {
        return baseRequest()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(appConfig.getTodosEndpoint())
                .then()
                .extract()
                .statusCode();
    }

    public void createTodo(TodoItem todoItem) {
        baseRequest()
                .body(todoItem)
                .when()
                .post(appConfig.getTodosEndpoint())
                .then()
                .statusCode(SC_CREATED);
        log.info("Created Todo: {}", todoItem);
    }

    public int createTodoExpectingStatusCode(TodoItem todoItem) {
        return baseRequest()
                .body(todoItem)
                .when()
                .post(appConfig.getTodosEndpoint())
                .then()
                .extract()
                .statusCode();
    }

    public void updateTodo(Long id, TodoItem todoItem) {
        baseRequest()
                .body(todoItem)
                .when()
                .put(appConfig.getTodosEndpoint() + "/{id}", id)
                .then()
                .statusCode(SC_OK);
    }

    public int updateTodoExpectingStatusCode(Long id, TodoItem todoItem) {
        return baseRequest()
                .body(todoItem)
                .when()
                .put(appConfig.getTodosEndpoint() + "/{id}", id)
                .then()
                .extract()
                .statusCode();
    }

    public int updateTodoWithInvalidIdFormatExpectingStatusCode(String id, TodoItem todoItem) {
        return baseRequest()
                .body(todoItem)
                .when()
                .put(appConfig.getTodosEndpoint() + "/{id}", id)
                .then()
                .extract()
                .statusCode();
    }

    public void deleteTodo(Long id) {
        baseRequest()
                .auth()
                .preemptive()
                .basic(appConfig.getAdminUsername(), appConfig.getAdminPassword())
                .when()
                .delete(appConfig.getTodosEndpoint() + "/{id}", id)
                .then()
                .statusCode(anyOf(equalTo(SC_OK), equalTo(SC_NO_CONTENT)));
        log.info("Deleted Todo with id {}", id);
    }

    public int deleteTodoExpectingStatusCode(Long id) {
        return baseRequest()
                .auth()
                .preemptive()
                .basic(appConfig.getAdminUsername(), appConfig.getAdminPassword())
                .when()
                .delete(appConfig.getTodosEndpoint() + "/{id}", id)
                .then()
                .extract()
                .statusCode();
    }

    public int deleteTodoWithoutAuthorizationExpectingStatusCode(Long id) {
        return baseRequest()
                .when()
                .delete(appConfig.getTodosEndpoint() + "/{id}", id)
                .then()
                .extract()
                .statusCode();
    }

}