package com.nttn.productivity_app.engine;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class TodoService implements iTodoService {
    private final iTodoRepository todoRepository;
    private final iIcsFileReader icsFileReader;

    public TodoService(iTodoRepository todoRepository, iIcsFileReader icsFileReader) {
        this.todoRepository = todoRepository;
        this.icsFileReader = icsFileReader;
    }

    @Override
    public Todo createTodo(Todo todo) {
        todo.updateDelta();
        return todoRepository.save(todo);
    }

    @Override
    public Optional<Todo> readTodo(String id) {
        return todoRepository.findById(id);
    }

    @Override
    public Todo updateTodo(Todo todo) {
        todo.updateDelta();
        return todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(String id) {
        todoRepository.deleteById(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Todo> getTodayTodos() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59);

        List<Todo> todos = todoRepository.findByDeadlineBetween(now, endOfDay);
        todos.sort((t1, t2) -> Long.compare(t1.getDelta(), t2.getDelta()));

        return todos;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Todo> getAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        todos.sort((t1, t2) -> Long.compare(t1.getDelta(), t2.getDelta()));
        return todos;
    }

    @Override
    public void updateAllTodosCountdown() {
        List<Todo> todos = todoRepository.findAll();
        for (Todo todo : todos) {
            todo.updateDelta();
            todoRepository.save(todo);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public List<Todo> checkDeadlineNotifications() {
        LocalDateTime now = LocalDateTime.now();
        List<Todo> todos = todoRepository.findAll()
                .stream()
                .filter(todo -> !todo.isNotified() &&
                        !todo.isCompleted() &&
                        todo.getDelta() <= 0)
                .collect(Collectors.toList());

        // Mark as notified
        todos.forEach(todo -> {
            todo.setNotified(true);
            todoRepository.save(todo);
        });
        return todos;
    }

    // ICS file method (stub implementation)
    public List<Todo> importTodosFromIcs(String filePath) {
        return icsFileReader.readTodosFromIcs(filePath);
    }
}
