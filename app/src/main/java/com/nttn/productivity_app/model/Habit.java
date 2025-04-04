package com.nttn.productivity_app.model;

import java.util.Date;
import java.util.Objects;

public class Habit {
    private long habitId;
    private String title;
    private Date startedAt;
    private Date createdDate;

    public Habit(String title, Date startedAt, Date createdDate) {
        this.title = title;
        this.startedAt = startedAt;
        this.createdDate = createdDate;
    }

    public Habit(String title, Date startedAt) {
        this.title = title;
        this.startedAt = startedAt;
        this.createdDate = new Date();
    }

    public Habit() {
    }

    public long getHabitId() {
        return habitId;
    }

    public void setHabitId(long habitId) {
        this.habitId = habitId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "habitId=" + habitId +
                ", title='" + title + '\'' +
                ", startedAt=" + startedAt +
                ", createdDate=" + createdDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return habitId == habit.habitId &&
                Objects.equals(title, habit.title) &&
                Objects.equals(startedAt, habit.startedAt) &&
                Objects.equals(createdDate, habit.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(habitId, title, startedAt, createdDate);
    }
}