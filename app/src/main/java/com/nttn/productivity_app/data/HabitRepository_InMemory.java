package com.nttn.productivity_app.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nttn.productivity_app.model.Habit;

import java.util.ArrayList;
import java.util.List;

public class HabitRepository_InMemory implements iHabitRepository {
    private static HabitRepository_InMemory instance;
    private final MutableLiveData<List<Habit>> habitsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final List<Habit> habits = new ArrayList<>();

    // Private constructor to prevent instantiation
    private HabitRepository_InMemory() {}

    // Public method to provide access to the singleton instance
    public static synchronized HabitRepository_InMemory getInstance() {
        if (instance == null) {
            instance = new HabitRepository_InMemory();
        }
        return instance;
    }

    @Override
    public LiveData<List<Habit>> getAllHabits() {
        return habitsLiveData;
    }

    @Override
    public LiveData<Habit> getHabit(long habitId) {
        MutableLiveData<Habit> habitLiveData = new MutableLiveData<>();
        for (Habit habit : habits) {
            if (habit.getHabitId() == habitId) {
                habitLiveData.setValue(habit);
                break;
            }
        }
        return habitLiveData;
    }

    @Override
    public LiveData<Long> countHabits() {
        MutableLiveData<Long> countLiveData = new MutableLiveData<>((long) habits.size());
        return countLiveData;
    }

    @Override
    public LiveData<Habit> getFirstStartedHabit() {
        MutableLiveData<Habit> habitLiveData = new MutableLiveData<>();
        if (!habits.isEmpty()) {
            habitLiveData.setValue(habits.get(0));
        }
        return habitLiveData;
    }

    @Override
    public void insertHabit(Habit habit) {
        habits.add(habit);
        habitsLiveData.setValue(new ArrayList<>(habits));
    }

    @Override
    public void updateHabit(Habit habit) {
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).getHabitId() == habit.getHabitId()) {
                habits.set(i, habit);
                habitsLiveData.setValue(new ArrayList<>(habits));
                break;
            }
        }
    }

    @Override
    public void deleteHabit(Habit habit) {
        habits.remove(habit);
        habitsLiveData.setValue(new ArrayList<>(habits));
    }

    @Override
    public void deleteLastAddedHabit() {
        if (!habits.isEmpty()) {
            habits.remove(habits.size() - 1);
            habitsLiveData.setValue(new ArrayList<>(habits));
        }
    }
}
