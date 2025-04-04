package com.nttn.productivity_app.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nttn.productivity_app.data.iHabitRepository;

import java.util.List;

public class HabitAndroidViewModel extends AndroidViewModel {
    private final iHabitRepository habitRepository;
    public final LiveData<List<Habit>> allHabits;

    public HabitAndroidViewModel(@NonNull Application application, @NonNull iHabitRepository habitRepository) {
        super(application);
        this.habitRepository = habitRepository;
        allHabits = habitRepository.getAllHabits();
    }

    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    public LiveData<Habit> getHabit(long habitId) {
        return habitRepository.getHabit(habitId);
    }

    public LiveData<Long> countHabits() {
        return habitRepository.countHabits();
    }

    public LiveData<Habit> getFirstStartedHabit() {
        return habitRepository.getFirstStartedHabit();
    }
}
