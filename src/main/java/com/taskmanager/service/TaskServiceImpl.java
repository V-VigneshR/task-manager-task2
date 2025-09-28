package com.taskmanager.service;

import com.taskmanager.dto.TaskCreateRequest;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskExecution;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.util.CommandValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taskmanager.exception.TaskNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * TaskServiceImpl provides the business logic for managing and executing tasks.
 * This service handles CRUD operations and command execution.
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CommandValidator commandValidator;

    /**
     * Constructor with dependency injection
     *
     * @param taskRepository Repository for database operations
     * @param commandValidator Validator for command safety
     */
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, CommandValidator commandValidator) {
        this.taskRepository = taskRepository;
        this.commandValidator = commandValidator;
    }

    /**
     * Get all tasks from the database
     *
     * @return List of all tasks
     */
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Get a task by its ID
     *
     * @param id The task ID
     * @return Optional containing the task if found, empty otherwise
     */
    @Override
    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    /**
     * Create or update a task with command validation
     *
     * @param taskRequest The task creation request
     * @return The created/updated task
     * @throws IllegalArgumentException if the command is unsafe
     */
    @Override
    public Task createOrUpdateTask(TaskCreateRequest taskRequest) {
        // Validate the command for security
        if (!commandValidator.isCommandSafe(taskRequest.getCommand())) {
            String reason = commandValidator.getUnsafeReason(taskRequest.getCommand());
            throw new IllegalArgumentException("Unsafe command detected: " + reason);
        }

        // Create or update the task
        Task task = new Task();
        task.setId(taskRequest.getId());
        task.setName(taskRequest.getName());
        task.setOwner(taskRequest.getOwner());
        task.setCommand(taskRequest.getCommand());

        // If updating existing task, preserve execution history
        Optional<Task> existingTask = taskRepository.findById(taskRequest.getId());
        if (existingTask.isPresent()) {
            task.setTaskExecutions(existingTask.get().getTaskExecutions());
        }

        return taskRepository.save(task);
    }

    /**
     * Delete a task by ID
     *
     * @param id The task ID to delete
     * @return true if task was deleted, false if task didn't exist
     */
    @Override
    public boolean deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Find tasks by name pattern (case-insensitive search)
     *
     * @param namePattern The string to search for in task names
     * @return List of tasks matching the name pattern
     */
    @Override
    public List<Task> findTasksByName(String namePattern) {
        return taskRepository.findByNameContainingIgnoreCase(namePattern);
    }

    /**
     * Execute a task by ID and store the execution result
     *
     * @param taskId The ID of the task to execute
     * @return The task execution result
     * @throws TaskNotFoundException if the task doesn't exist
     * @throws RuntimeException if command execution fails
     */
    @Override
    public TaskExecution executeTask(String taskId) {
        // Find the task
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Task with ID " + taskId + " not found");
        }

        Task task = optionalTask.get();

        // Double-check command safety before execution
        if (!commandValidator.isCommandSafe(task.getCommand())) {
            throw new RuntimeException("Cannot execute unsafe command: " + task.getCommand());
        }

        Date startTime = new Date();
        String output;
        Date endTime;

        try {
            // Execute the shell command
            output = executeShellCommand(task.getCommand());
            endTime = new Date();
        } catch (Exception e) {
            endTime = new Date();
            output = "Error executing command: " + e.getMessage();
        }

        // Create task execution record
        TaskExecution execution = new TaskExecution(startTime, endTime, output);

        // Add execution to task and save
        task.addTaskExecution(execution);
        taskRepository.save(task);

        return execution;
    }

    /**
     * Execute a shell command safely
     *
     * @param command The command to execute
     * @return The command output
     * @throws IOException if execution fails
     * @throws InterruptedException if execution is interrupted
     */
    private String executeShellCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Set up the command - use shell appropriate for the OS
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("sh", "-c", command);
        }

        // Start the process
        Process process = processBuilder.start();

        // Read the output
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        // Read error output if any
        StringBuilder errorOutput = new StringBuilder();
        try (BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
        }

        // Wait for the process to complete with timeout
        boolean finished = process.waitFor(30, java.util.concurrent.TimeUnit.SECONDS);

        if (!finished) {
            process.destroyForcibly();
            throw new RuntimeException("Command execution timed out after 30 seconds");
        }

        // Check exit code
        int exitCode = process.exitValue();
        if (exitCode != 0) {
            String errorMsg = errorOutput.length() > 0 ? errorOutput.toString() : "Command failed with exit code " + exitCode;
            return "Error: " + errorMsg + (output.length() > 0 ? "\nOutput: " + output.toString() : "");
        }

        return output.toString();
    }

    /**
     * Check if a command is safe to execute
     *
     * @param command The command to validate
     * @return true if safe, false otherwise
     */
    @Override
    public boolean isCommandSafe(String command) {
        return commandValidator.isCommandSafe(command);
    }
}