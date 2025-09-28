package com.taskmanager.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Task represents a shell command that can be executed.
 * This is the main entity stored in MongoDB.
 */
@Document(collection = "tasks") // MongoDB collection name
public class Task {

    /**
     * Unique identifier for the task
     */
    @Id
    private String id;

    /**
     * Human-readable name for the task
     */
    @NotBlank(message = "Task name is required")
    private String name;

    /**
     * Owner of the task
     */
    @NotBlank(message = "Task owner is required")
    private String owner;

    /**
     * The shell command to be executed
     */
    @NotBlank(message = "Command is required")
    private String command;

    /**
     * List of all executions for this task
     */
    @NotNull
    private List<TaskExecution> taskExecutions;

    // Default constructor
    public Task() {
        this.taskExecutions = new ArrayList<>();
    }

    /**
     * Constructor for creating a new task
     *
     * @param id Unique task identifier
     * @param name Task name
     * @param owner Task owner
     * @param command Shell command to execute
     */
    public Task(String id, String name, String owner, String command) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.command = command;
        this.taskExecutions = new ArrayList<>();
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<TaskExecution> getTaskExecutions() {
        return taskExecutions;
    }

    public void setTaskExecutions(List<TaskExecution> taskExecutions) {
        this.taskExecutions = taskExecutions != null ? taskExecutions : new ArrayList<>();
    }

    /**
     * Add a new task execution to the list
     *
     * @param execution The task execution to add
     */
    public void addTaskExecution(TaskExecution execution) {
        if (this.taskExecutions == null) {
            this.taskExecutions = new ArrayList<>();
        }
        this.taskExecutions.add(execution);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", command='" + command + '\'' +
                ", taskExecutions=" + taskExecutions +
                '}';
    }
}