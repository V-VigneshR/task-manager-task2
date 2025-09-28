package com.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for creating or updating tasks.
 * This class is used to receive task data from API requests.
 */
public class TaskCreateRequest {

    /**
     * Task identifier
     */
    @NotBlank(message = "Task ID is required")
    private String id;

    /**
     * Task name
     */
    @NotBlank(message = "Task name is required")
    private String name;

    /**
     * Task owner
     */
    @NotBlank(message = "Task owner is required")
    private String owner;

    /**
     * Shell command to execute
     */
    @NotBlank(message = "Command is required")
    private String command;

    // Default constructor
    public TaskCreateRequest() {}

    /**
     * Constructor with all fields
     *
     * @param id Task ID
     * @param name Task name
     * @param owner Task owner
     * @param command Shell command
     */
    public TaskCreateRequest(String id, String name, String owner, String command) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.command = command;
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

    @Override
    public String toString() {
        return "TaskCreateRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}