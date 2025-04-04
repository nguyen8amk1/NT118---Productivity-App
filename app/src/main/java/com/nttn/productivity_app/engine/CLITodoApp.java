package com.nttn.productivity_app.engine;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.nttn.productivity_app.model.Todo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CLITodoApp {
    private iTodoRepository todoRepository;

    public CLITodoApp() {
        // Initialize the in-memory repository
        todoRepository = new TodoRepository_InMemory();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void runDemo() {
        // Create some sample todos
        LocalDateTime now = LocalDateTime.now();
        Todo todo1 = new Todo("1", "Buy groceries", "Milk, eggs, bread", now.plusHours(2));
        Todo todo2 = new Todo("2", "Finish report", "Quarterly sales report", now.plusDays(1));
        Todo todo3 = new Todo("3", "Call mom", "Wish her happy birthday", now.minusHours(1)); // Overdue

        // Save todos
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        // Find a todo by ID
        Optional<Todo> foundTodo = todoRepository.findById("1");
        foundTodo.ifPresent(todo -> {
            System.out.println("Found todo: " + todo.getTitle());

            // Update the todo
            todo.setDescription("Milk, eggs, bread, butter");
            todoRepository.save(todo);
            System.out.println("Updated description: " + todo.getDescription());
        });

        // Find all todos
        List<Todo> allTodos = todoRepository.findAll();
        System.out.println("\nAll Todos:");
        allTodos.forEach(todo -> System.out.println(todo.getTitle() +
                " - Due: " + todo.getDeadline() +
                " - Completed: " + todo.isCompleted()));

        // Find todos within a deadline range
        LocalDateTime start = now.minusDays(1);
        LocalDateTime end = now.plusDays(2);
        List<Todo> todosInRange = todoRepository.findByDeadlineBetween(start, end);
        System.out.println("\nTodos between " + start + " and " + end + ":");
        todosInRange.forEach(todo -> System.out.println(todo.getTitle()));

        // Mark a todo as completed
        foundTodo = todoRepository.findById("2");
        foundTodo.ifPresent(todo -> {
            todo.markAsCompleted();
            todoRepository.save(todo);
            System.out.println("\nMarked as completed: " + todo.getTitle());
        });

        // Find overdue todos
        List<Todo> overdueTodos = todoRepository.findOverdue();
        System.out.println("\nOverdue Todos:");
        overdueTodos.forEach(todo -> System.out.println(todo.getTitle() +
                " - Due: " + todo.getDeadline()));

        // Find todos needing notification
        List<Todo> todosForNotification = todoRepository.findTodosForNotification();
        System.out.println("\nTodos needing notification:");
        todosForNotification.forEach(todo -> System.out.println(todo.getTitle()));

        // Delete a todo
        todoRepository.deleteById("3");
        System.out.println("\nAfter deleting todo 3, total count: " + todoRepository.findAll().size());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {
        new CLITodoApp().runDemo();
    }
}
