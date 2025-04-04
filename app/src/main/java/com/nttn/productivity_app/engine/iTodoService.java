package com.nttn.productivity_app.engine;

import java.util.Optional;
import java.util.List;

public interface iTodoService {
    Todo createTodo(Todo todo);

    Optional<Todo> readTodo(String id);

    Todo updateTodo(Todo todo);

    void deleteTodo(String id);

    List<Todo> getTodayTodos();

    List<Todo> getAllTodos();

    void updateAllTodosCountdown();

    List<Todo> checkDeadlineNotifications();
}
