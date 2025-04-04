package com.nttn.productivity_app.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.nttn.productivity_app.model.Habit;
import com.nttn.productivity_app.util.TypeConverterUtil;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Habit.class}, version = 2, exportSchema = false)
@TypeConverters({TypeConverterUtil.class})
public abstract class HabitDatabase extends RoomDatabase {

    public abstract HabitDao habitDao();

    private static volatile HabitDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static HabitDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HabitDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HabitDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
