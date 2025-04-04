package com.nttn.productivity_app.engine;

// import android.content.ContentValues;

// import android.content.Context;
// import android.database.Cursor;
// import android.database.sqlite.SQLiteDatabase;
// import android.database.sqlite.SQLiteOpenHelper;
//
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// public class TodoRepository_SqLite extends SQLiteOpenHelper implements iTodoRepository {
public class TodoRepository_RoomDB {
    // private static final String DATABASE_NAME = "todo_database";
    // private static final int DATABASE_VERSION = 1;

    // // Table name and columns
    // private static final String TABLE_TODOS = "todos";
    // private static final String COLUMN_ID = "id";
    // private static final String COLUMN_TITLE = "title";
    // private static final String COLUMN_DESCRIPTION = "description";
    // private static final String COLUMN_DEADLINE = "deadline";
    // private static final String COLUMN_COMPLETED = "completed";
    // private static final String COLUMN_NOTIFIED = "notified";
    //
    // // DateTime formatter for SQLite storage
    // private static final DateTimeFormatter formatter =
    // DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    //
    // public SQLiteTodoRepository(Context context) {
    // super(context, DATABASE_NAME, null, DATABASE_VERSION);
    // }
    //
    // @Override
    // public void onCreate(SQLiteDatabase db) {
    // String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODOS + "("
    // + COLUMN_ID + " TEXT PRIMARY KEY,"
    // + COLUMN_TITLE + " TEXT,"
    // + COLUMN_DESCRIPTION + " TEXT,"
    // + COLUMN_DEADLINE + " TEXT,"
    // + COLUMN_COMPLETED + " INTEGER,"
    // + COLUMN_NOTIFIED + " INTEGER"
    // + ")";
    // db.execSQL(CREATE_TODOS_TABLE);
    // }
    //
    // @Override
    // public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
    // onCreate(db);
    // }
    //
    // @Override
    // public Todo save(Todo todo) {
    // SQLiteDatabase db = this.getWritableDatabase();
    // ContentValues values = new ContentValues();
    //
    // values.put(COLUMN_ID, todo.getId());
    // values.put(COLUMN_TITLE, todo.getTitle());
    // values.put(COLUMN_DESCRIPTION, todo.getDescription());
    // values.put(COLUMN_DEADLINE, todo.getDeadline().format(formatter));
    // values.put(COLUMN_COMPLETED, todo.isCompleted() ? 1 : 0);
    // values.put(COLUMN_NOTIFIED, todo.isNotified() ? 1 : 0);
    //
    // // First try to update, if no rows affected then insert
    // int rowsAffected = db.update(TABLE_TODOS, values, COLUMN_ID + " = ?",
    // new String[] { todo.getId() });
    //
    // if (rowsAffected == 0) {
    // db.insert(TABLE_TODOS, null, values);
    // }
    //
    // db.close();
    // return todo;
    // }
    //
    // @Override
    // public Optional<Todo> findById(String id) {
    // SQLiteDatabase db = this.getReadableDatabase();
    // Cursor cursor = db.query(TABLE_TODOS,
    // new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
    // COLUMN_DEADLINE, COLUMN_COMPLETED, COLUMN_NOTIFIED },
    // COLUMN_ID + " = ?", new String[] { id },
    // null, null, null);
    //
    // if (cursor != null && cursor.moveToFirst()) {
    // Todo todo = cursorToTodo(cursor);
    // cursor.close();
    // db.close();
    // return Optional.of(todo);
    // }
    //
    // if (cursor != null) {
    // cursor.close();
    // }
    // db.close();
    // return Optional.empty();
    // }
    //
    // @Override
    // public List<Todo> findAll() {
    // List<Todo> todoList = new ArrayList<>();
    // SQLiteDatabase db = this.getReadableDatabase();
    // Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TODOS, null);
    //
    // if (cursor.moveToFirst()) {
    // do {
    // todoList.add(cursorToTodo(cursor));
    // } while (cursor.moveToNext());
    // }
    //
    // cursor.close();
    // db.close();
    // return todoList;
    // }
    //
    // @Override
    // public void deleteById(String id) {
    // SQLiteDatabase db = this.getWritableDatabase();
    // db.delete(TABLE_TODOS, COLUMN_ID + " = ?", new String[] { id });
    // db.close();
    // }
    //
    // @Override
    // public List<Todo> findByDeadlineBetween(LocalDateTime start, LocalDateTime
    // end) {
    // List<Todo> todoList = new ArrayList<>();
    // SQLiteDatabase db = this.getReadableDatabase();
    //
    // String startStr = start.format(formatter);
    // String endStr = end.format(formatter);
    //
    // Cursor cursor = db.query(TABLE_TODOS,
    // new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
    // COLUMN_DEADLINE, COLUMN_COMPLETED, COLUMN_NOTIFIED },
    // COLUMN_DEADLINE + " BETWEEN ? AND ?",
    // new String[] { startStr, endStr },
    // null, null, COLUMN_DEADLINE + " ASC");
    //
    // if (cursor.moveToFirst()) {
    // do {
    // todoList.add(cursorToTodo(cursor));
    // } while (cursor.moveToNext());
    // }
    //
    // cursor.close();
    // db.close();
    // return todoList;
    // }
    //
    // // Helper method to convert cursor to Todo object
    // private Todo cursorToTodo(Cursor cursor) {
    // String id = cursor.getString(0);
    // String title = cursor.getString(1);
    // String description = cursor.getString(2);
    // LocalDateTime deadline = LocalDateTime.parse(cursor.getString(3), formatter);
    // boolean completed = cursor.getInt(4) == 1;
    // boolean notified = cursor.getInt(5) == 1;
    //
    // Todo todo = new Todo(id, title, description, deadline);
    // if (completed) {
    // todo.markAsCompleted();
    // }
    // todo.setNotified(notified);
    // return todo;
    // }
    //
    // // Additional helper methods
    //
    // public List<Todo> findOverdue() {
    // String nowStr = LocalDateTime.now().format(formatter);
    // List<Todo> todoList = new ArrayList<>();
    // SQLiteDatabase db = this.getReadableDatabase();
    //
    // Cursor cursor = db.query(TABLE_TODOS,
    // new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
    // COLUMN_DEADLINE, COLUMN_COMPLETED, COLUMN_NOTIFIED },
    // COLUMN_DEADLINE + " < ? AND " + COLUMN_COMPLETED + " = 0",
    // new String[] { nowStr },
    // null, null, COLUMN_DEADLINE + " ASC");
    //
    // if (cursor.moveToFirst()) {
    // do {
    // todoList.add(cursorToTodo(cursor));
    // } while (cursor.moveToNext());
    // }
    //
    // cursor.close();
    // db.close();
    // return todoList;
    // }
    //
    // public List<Todo> findTodosForNotification() {
    // List<Todo> todoList = new ArrayList<>();
    // SQLiteDatabase db = this.getReadableDatabase();
    //
    // Cursor cursor = db.query(TABLE_TODOS,
    // new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
    // COLUMN_DEADLINE, COLUMN_COMPLETED, COLUMN_NOTIFIED },
    // COLUMN_COMPLETED + " = 0 AND " + COLUMN_NOTIFIED + " = 0",
    // null,
    // null, null, COLUMN_DEADLINE + " ASC");
    //
    // if (cursor.moveToFirst()) {
    // do {
    // todoList.add(cursorToTodo(cursor));
    // } while (cursor.moveToNext());
    // }
    //
    // cursor.close();
    // db.close();
    // return todoList;
    // }
    //
    // public List<Todo> findCompleted() {
    // List<Todo> todoList = new ArrayList<>();
    // SQLiteDatabase db = this.getReadableDatabase();
    //
    // Cursor cursor = db.query(TABLE_TODOS,
    // new String[] { COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,
    // COLUMN_DEADLINE, COLUMN_COMPLETED, COLUMN_NOTIFIED },
    // COLUMN_COMPLETED + " = 1",
    // null,
    // null, null, COLUMN_DEADLINE + " DESC");
    //
    // if (cursor.moveToFirst()) {
    // do {
    // todoList.add(cursorToTodo(cursor));
    // } while (cursor.moveToNext());
    // }
    //
    // cursor.close();
    // db.close();
    // return todoList;
    // }
}
