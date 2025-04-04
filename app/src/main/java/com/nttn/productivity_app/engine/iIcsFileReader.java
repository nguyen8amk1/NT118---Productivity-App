package com.nttn.productivity_app.engine;

import java.util.List;

public interface iIcsFileReader {
    List<Todo> readTodosFromIcs(String filePath);
}
