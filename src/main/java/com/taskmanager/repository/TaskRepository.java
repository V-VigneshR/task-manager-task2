package com.taskmanager.repository;

import com.taskmanager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TaskRepository provides data access methods for Task entities.
 * It extends MongoRepository which provides basic CRUD operations.
 */
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    /**
     * Find tasks by name containing the specified string (case-insensitive)
     *
     * @param namePattern The string to search for in task names
     * @return List of tasks whose names contain the search string
     */
    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<Task> findByNameContainingIgnoreCase(String namePattern);

    /**
     * Find tasks by exact name match (case-insensitive)
     *
     * @param name The exact name to search for
     * @return List of tasks with matching names
     */
    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<Task> findByNameIgnoreCase(String name);

    /**
     * Find tasks by owner
     *
     * @param owner The owner to search for
     * @return List of tasks owned by the specified owner
     */
    List<Task> findByOwner(String owner);

    /**
     * Check if a task exists with the given ID
     *
     * @param id The task ID to check
     * @return true if task exists, false otherwise
     */
    boolean existsById(String id);
}