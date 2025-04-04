package com.nttn.productivity_app.engine;

import com.nttn.productivity_app.model.Todo;

import java.util.List;

public interface iIcsFileReader {
    List<Todo> readTodosFromIcs(String filePath);
}
