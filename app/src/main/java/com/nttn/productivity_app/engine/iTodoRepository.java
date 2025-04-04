package com.nttn.productivity_app.engine;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

// Secondary Ports (Interfaces that core requires)
public interface iTodoRepository {
    Todo save(Todo todo);

    Optional<Todo> findById(String id);

    List<Todo> findAll();

    void deleteById(String id);

    List<Todo> findByDeadlineBetween(LocalDateTime start, LocalDateTime end);

    List<Todo> findOverdue();

    List<Todo> findCompleted();

    List<Todo> findTodosForNotification();
}
