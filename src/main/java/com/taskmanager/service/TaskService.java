package com.taskmanager.service;

import com.taskmanager.dto.TaskCreateRequest;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskExecution;

import java.util.List;
import java.util.Optional;

/**
 * TaskService defines the business logic operations for managing tasks.
 * This interface provides a contract for task operations.
 */
public interface TaskService {

    /**
     * Get all tasks from the database
     *
     * @return List of all tasks
     */
    List<Task> getAllTasks();

    /**
     * Get a task by its ID
     *
     * @param id The task ID
     * @return Optional containing the task if found, empty otherwise
     */
    Optional<Task> getTaskById(String id);

    /**
     * Create or update a task
     *
     * @param taskRequest The task creation request
     * @return The created/updated task
     * @throws IllegalArgumentException if the command is unsafe
     */
    Task createOrUpdateTask(TaskCreateRequest taskRequest);

    /**
     * Delete a task by ID
     *
     * @param id The task ID to delete
     * @return true if task was deleted, false if task didn't exist
     */
    boolean deleteTask(String id);

    /**
     * Find tasks by name pattern (case-insensitive search)
     *
     * @param namePattern The string to search for in task names
     * @return List of tasks matching the name pattern
     */
    List<Task> findTasksByName(String namePattern);

    /**
     * Execute a task by ID and store the execution result
     *
     * @param taskId The ID of the task to execute
     * @return The task execution result
     * @throws TaskNotFoundException if the task doesn't exist
     * @throws RuntimeException if command execution fails
     */
    TaskExecution executeTask(String taskId);

    /**
     * Check if a command is safe to execute
     *
     * @param command The command to validate
     * @return true if safe, false otherwise
     */
    boolean isCommandSafe(String command);
}