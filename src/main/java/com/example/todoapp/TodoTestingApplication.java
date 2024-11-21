package com.example.todoapp;

import com.example.todoapp.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class TodoTestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoTestingApplication.class, args);
    }

}