package com.nttn.productivity_app.ui.habit;

import com.nttn.productivity_app.model.Habit;

public interface OnHabitClickListener {
    void onHabitClick(Habit habit);
    void onHabitLongClick(Habit habit);
}
