package productivity_app_core;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// NOTE: (nttn) this class suppose to work even if the db not existed
public class SqliteTaskRepository extends SQLiteOpenHelper implements ITaskRepository {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IS_COMPLETE = "is_complete";
    private static final String COLUMN_COMPLETION_DATE = "completion_date";

    public SqliteTaskRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IS_COMPLETE + " INTEGER, " +
                COLUMN_COMPLETION_DATE + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_IS_COMPLETE, task.isComplete() ? 1 : 0);
        values.put(COLUMN_COMPLETION_DATE, task.getCompletionDate() != null ? task.getCompletionDate().toString() : null);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @SuppressLint("Range")
    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Task task = new Task(
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                );
                task.setComplete(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_COMPLETE)) == 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    task.setCompletionDate(cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETION_DATE)) != null ?
                            LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETION_DATE))) : null);
                }
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    @SuppressLint("Range")
    @Override
    public Optional<Task> getTaskById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") Task task = new Task(
                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            );
            task.setComplete(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_COMPLETE)) == 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                task.setCompletionDate(cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETION_DATE)) != null ?
                        LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(COLUMN_COMPLETION_DATE))) : null);
            }
            cursor.close();
            db.close();
            return Optional.of(task);
        } else {
            cursor.close();
            db.close();
            return Optional.empty();
        }
    }

    @Override
    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_IS_COMPLETE, task.isComplete() ? 1 : 0);
        values.put(COLUMN_COMPLETION_DATE, task.getCompletionDate() != null ? task.getCompletionDate().toString() : null);
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    @Override
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}