package com.taskmanager.exception;

/**
 * Custom exception thrown when a requested task is not found in the database.
 * This exception will be handled by the global exception handler to return a 404 status.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Constructor with message
     *
     * @param message The error message describing what task was not found
     */
    public TaskNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     *
     * @param message The error message
     * @param cause The underlying cause of the exception
     */
    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}