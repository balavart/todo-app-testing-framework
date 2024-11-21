package com.example.todoapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a TodoItem.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoItem {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("text")
    private String text;
    @JsonProperty("completed")
    private Boolean completed;
}