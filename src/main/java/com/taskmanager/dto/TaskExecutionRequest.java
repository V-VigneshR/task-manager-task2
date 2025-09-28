package com.taskmanager.dto;

/**
 * Data Transfer Object for task execution requests.
 * This class is used when executing a task.
 */
public class TaskExecutionRequest {

    /**
     * The ID of the task to execute
     */
    private String taskId;

    // Default constructor
    public TaskExecutionRequest() {}

    /**
     * Constructor with task ID
     *
     * @param taskId The ID of the task to execute
     */
    public TaskExecutionRequest(String taskId) {
        this.taskId = taskId;
    }

    // Getter and Setter

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "TaskExecutionRequest{" +
                "taskId='" + taskId + '\'' +
                '}';
    }
}