package com.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * TaskExecution represents a single execution of a task command.
 * This class stores the execution details including timing and output.
 */
public class TaskExecution {

    /**
     * The date and time when the task execution started
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSZ", timezone = "UTC")
    private Date startTime;

    /**
     * The date and time when the task execution ended
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSZ", timezone = "UTC")
    private Date endTime;

    /**
     * The output produced by the command execution
     */
    private String output;

    // Default constructor for JSON deserialization
    public TaskExecution() {}

    /**
     * Constructor for creating a new task execution
     *
     * @param startTime When the execution started
     * @param endTime When the execution ended
     * @param output The command output
     */
    public TaskExecution(Date startTime, Date endTime, String output) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.output = output;
    }

    // Getters and Setters

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "TaskExecution{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", output='" + output + '\'' +
                '}';
    }
}