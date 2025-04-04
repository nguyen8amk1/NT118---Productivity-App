package com.nttn.productivity_app.data;

import androidx.lifecycle.LiveData;

import com.nttn.productivity_app.model.Habit;

import java.util.List;

public interface iHabitRepository {
    LiveData<List<Habit>> getAllHabits();
    LiveData<Habit> getHabit(long habitId);
    LiveData<Long> countHabits();
    LiveData<Habit> getFirstStartedHabit();
    void insertHabit(Habit habit);
    void updateHabit(Habit habit);
    void deleteHabit(Habit habit);
    void deleteLastAddedHabit();
}