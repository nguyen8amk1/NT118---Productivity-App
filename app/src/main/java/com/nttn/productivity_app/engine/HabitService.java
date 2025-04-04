package com.nttn.productivity_app.engine;

import com.nttn.productivity_app.model.Habit;
import com.nttn.productivity_app.model.HabitAndroidViewModel;

import java.util.Date;

public class HabitService {
    public void updateHabit(Habit habit) {
        HabitAndroidViewModel.updateHabit(habit);
    }
    public void deleteHabit(Habit habit) {
        HabitAndroidViewModel.deleteHabit(habit);
    }
}
