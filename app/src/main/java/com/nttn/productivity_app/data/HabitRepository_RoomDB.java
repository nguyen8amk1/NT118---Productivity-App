package com.nttn.productivity_app.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.nttn.productivity_app.model.Habit;

import java.util.Date;
import java.util.List;

public class HabitRepository_RoomDB implements iHabitRepository {

    private final HabitDao habitDao;
    private final LiveData<List<Habit>> allHabits;
    private final LiveData<Long> habitsCounts;

    public HabitRepository_RoomDB(Application application) {
        HabitDatabase habitDatabase = HabitDatabase.getDatabase(application);
        this.habitDao = habitDatabase.habitDao();
        this.allHabits = habitDao.getAll();
        this.habitsCounts = habitDao.count();
    }

    @Override
    public LiveData<List<Habit>> getAllHabits() {
        return allHabits;
    }

    @Override
    public LiveData<Habit> getHabit(long habitId) {
        return habitDao.get(habitId);
    }

    @Override
    public LiveData<Long> countHabits() {
        return habitsCounts;
    }

    @Override
    public LiveData<Habit> getFirstStartedHabit() {
        return habitDao.getFirstStarted();
    }

    @Override
    public void insertHabit(Habit habit) {
        sanitizeHabit(habit);
        HabitDatabase.databaseWriteExecutor.execute(() -> habitDao.insert(habit));
    }

    @Override
    public void updateHabit(Habit habit) {
        sanitizeHabit(habit);
        HabitDatabase.databaseWriteExecutor.execute(() -> habitDao.update(habit));
    }

    @Override
    public void deleteHabit(Habit habit) {
        HabitDatabase.databaseWriteExecutor.execute(() -> habitDao.delete(habit));
    }

    @Override
    public void deleteLastAddedHabit() {
        HabitDatabase.databaseWriteExecutor.execute(habitDao::deleteLastAdded);
    }

    private void sanitizeHabit(Habit habit) {
        if(habit.getStartedAt() == null) {
            habit.setStartedAt(new Date());
        }
    }

}
