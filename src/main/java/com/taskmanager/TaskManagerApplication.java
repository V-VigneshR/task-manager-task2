package com.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * TaskManagerApplication is the main Spring Boot application class.
 * This class bootstraps the entire application.
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.taskmanager.repository")
public class TaskManagerApplication {

    /**
     * Main method to start the Spring Boot application
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting Task Manager API...");

        // Print some startup information
        System.out.println("========================================");
        System.out.println("  Task Manager REST API");
        System.out.println("  Version: 1.0.0");
        System.out.println("  Port: 8080");
        System.out.println("  Context Path: /api/v1");
        System.out.println("  Base URL: http://localhost:8080/api/v1");
        System.out.println("========================================");

        // Start the Spring Boot application
        SpringApplication.run(TaskManagerApplication.class, args);

        System.out.println("Task Manager API started successfully!");
        System.out.println("API Documentation:");
        System.out.println("- GET    /api/v1/tasks                     - Get all tasks");
        System.out.println("- GET    /api/v1/tasks?id={id}             - Get task by ID");
        System.out.println("- PUT    /api/v1/tasks                     - Create/update task");
        System.out.println("- DELETE /api/v1/tasks/{id}                - Delete task");
        System.out.println("- GET    /api/v1/tasks/search?name={name}  - Search tasks by name");
        System.out.println("- PUT    /api/v1/tasks/{id}/execute        - Execute task");
        System.out.println("- GET    /api/v1/tasks/{id}/executions     - Get execution history");
        System.out.println("- GET    /api/v1/tasks/validate?command={cmd} - Validate command");
        System.out.println("- GET    /api/v1/tasks/health              - Health check");
    }
}