package com.example.todoapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppConfig {
    private String baseUrl;
    private String adminUsername;
    private String adminPassword;
    private String todosEndpoint;
    private String contentType;
}