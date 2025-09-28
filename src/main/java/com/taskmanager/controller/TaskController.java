package com.taskmanager.controller;

import com.taskmanager.dto.TaskCreateRequest;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskExecution;
import com.taskmanager.service.TaskService;
import com.taskmanager.exception.TaskNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * TaskController handles all REST API endpoints for task management.
 * Base URL: /api/v1/tasks
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*") // Allow CORS for testing with Postman/frontend
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructor with dependency injection
     *
     * @param taskService Service for task operations
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /tasks - Get all tasks or a specific task by ID
     *
     * @param id Optional task ID parameter
     * @return List of all tasks or a specific task
     */
    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(required = false) String id) {
        if (id != null && !id.trim().isEmpty()) {
            // Get specific task by ID
            Optional<Task> task = taskService.getTaskById(id.trim());
            if (task.isPresent()) {
                return ResponseEntity.ok(task.get());
            } else {
                throw new TaskNotFoundException("Task with ID '" + id + "' not found");
            }
        } else {
            // Get all tasks
            List<Task> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        }
    }

    /**
     * PUT /tasks - Create or update a task
     *
     * @param taskRequest The task data from request body
     * @return The created/updated task
     */
    @PutMapping
    public ResponseEntity<Task> createOrUpdateTask(@Valid @RequestBody TaskCreateRequest taskRequest) {
        try {
            Task savedTask = taskService.createOrUpdateTask(taskRequest);
            return ResponseEntity.ok(savedTask);
        } catch (IllegalArgumentException e) {
            // This will be handled by the global exception handler
            throw e;
        }
    }

    /**
     * DELETE /tasks/{id} - Delete a task by ID
     *
     * @param id The task ID to delete
     * @return Success or error response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.ok("Task with ID '" + id + "' has been deleted successfully");
        } else {
            throw new TaskNotFoundException("Task with ID '" + id + "' not found");
        }
    }

    /**
     * GET /tasks/search - Find tasks by name pattern
     *
     * @param name The name pattern to search for
     * @return List of matching tasks
     */
    @GetMapping("/search")
    public ResponseEntity<List<Task>> findTasksByName(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search parameter 'name' cannot be empty");
        }

        List<Task> tasks = taskService.findTasksByName(name.trim());

        if (tasks.isEmpty()) {
            throw new TaskNotFoundException("No tasks found with name containing '" + name + "'");
        }

        return ResponseEntity.ok(tasks);
    }

    /**
     * PUT /tasks/{id}/execute - Execute a task by ID
     *
     * @param id The task ID to execute
     * @return The task execution result
     */
    @PutMapping("/{id}/execute")
    public ResponseEntity<TaskExecution> executeTask(@PathVariable String id) {
        try {
            TaskExecution execution = taskService.executeTask(id);
            return ResponseEntity.ok(execution);
        } catch (TaskNotFoundException e) {
            throw e; // Will be handled by global exception handler
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute task: " + e.getMessage(), e);
        }
    }

    /**
     * GET /tasks/{id}/executions - Get execution history for a task
     *
     * @param id The task ID
     * @return List of task executions
     */
    @GetMapping("/{id}/executions")
    public ResponseEntity<List<TaskExecution>> getTaskExecutions(@PathVariable String id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get().getTaskExecutions());
        } else {
            throw new TaskNotFoundException("Task with ID '" + id + "' not found");
        }
    }

    /**
     * GET /tasks/validate - Validate if a command is safe (utility endpoint)
     *
     * @param command The command to validate
     * @return Validation result
     */
    @GetMapping("/validate")
    public ResponseEntity<String> validateCommand(@RequestParam String command) {
        if (command == null || command.trim().isEmpty()) {
            throw new IllegalArgumentException("Command parameter cannot be empty");
        }

        boolean isSafe = taskService.isCommandSafe(command);
        if (isSafe) {
            return ResponseEntity.ok("Command is safe to execute");
        } else {
            return ResponseEntity.badRequest()
                    .body("Command is not safe: contains dangerous operations or patterns");
        }
    }

    /**
     * GET /health - Health check endpoint
     *
     * @return Health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Task Manager API is running");
    }
}