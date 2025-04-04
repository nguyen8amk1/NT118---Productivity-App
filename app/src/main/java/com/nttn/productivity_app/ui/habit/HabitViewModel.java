package com.nttn.productivity_app.ui.habit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nttn.productivity_app.model.Habit;

public class HabitViewModel extends ViewModel {

    private final MutableLiveData<Habit> selectedHabit = new MutableLiveData<>();
    private boolean isEditing;

    private HabitBottomSheetFragment habitBottomSheetFragment;

    public MutableLiveData<Habit> getSelectedHabit() {
        return selectedHabit;
    }

    public void setHabit(Habit habit) {
        selectedHabit.setValue(habit);
    }

    public boolean isEditing() {
        return (getSelectedHabit().getValue() != null ) && isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public HabitBottomSheetFragment getHabitBottomSheetFragment() {
        return habitBottomSheetFragment;
    }

    public void setHabitBottomSheetFragment(HabitBottomSheetFragment habitBottomSheetFragment) {
        this.habitBottomSheetFragment = habitBottomSheetFragment;
    }
}
