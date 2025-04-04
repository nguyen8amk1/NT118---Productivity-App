package com.nttn.productivity_app.engine;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Objects;

public class Todo {
    private String id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private long delta; // time remaining in minutes
    private boolean completed;
    private boolean notified;

    // Constructor
    public Todo(String id, String title, String description, LocalDateTime deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.delta = calculateDelta(deadline);
        this.completed = false;
        this.notified = false;
    }

    // Calculate the time remaining in minutes
    private long calculateDelta(LocalDateTime deadline) {
        return Duration.between(LocalDateTime.now(), deadline).toMinutes();
    }

    // Update delta based on the current time
    public void updateDelta() {
        this.delta = calculateDelta(this.deadline);
    }

    // Mark as completed
    public void markAsCompleted() {
        this.completed = true;
    }

    // Check if the task is overdue
    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(deadline);
    }

    // Set the task as notified
    public void markAsNotified() {
        this.notified = true;
    }

    // Update the deadline
    public void updateDeadline(LocalDateTime newDeadline) {
        this.deadline = newDeadline;
        updateDelta();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public long getDelta() {
        return delta;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isNotified() {
        return notified;
    }

    // Override equals and hashCode for proper comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", delta=" + delta +
                ", completed=" + completed +
                ", notified=" + notified +
                '}';
    }
}
