package com.nttn.productivity_app.model;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nttn.productivity_app.engine.iTodoRepository;

import java.util.List;

public class TodoAndroidViewModel extends AndroidViewModel {
    private final iTodoRepository repository;
    private final MutableLiveData<List<Todo>> allTodos;

    public TodoAndroidViewModel(@NonNull Application application, iTodoRepository repository) {
        super(application);
        this.repository = repository;
        this.allTodos = new MutableLiveData<>();
        loadTodos();
    }

    private void loadTodos() {
        allTodos.setValue(repository.findAll());
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    public void save(Todo todo) {
        new Thread(() -> {
            repository.save(todo);
            loadTodos(); // Refresh the list
        }).start();
    }

    public void deleteById(String id) {
        new Thread(() -> {
            repository.deleteById(id);
            loadTodos(); // Refresh the list
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<Todo> findById(String id) {
        MutableLiveData<Todo> result = new MutableLiveData<>();
        new Thread(() -> result.postValue(repository.findById(id).orElse(null))).start();
        return result;
    }

    public LiveData<List<Todo>> findOverdue() {
        MutableLiveData<List<Todo>> result = new MutableLiveData<>();
        new Thread(() -> result.postValue(repository.findOverdue())).start();
        return result;
    }

    public LiveData<List<Todo>> findTodosForNotification() {
        MutableLiveData<List<Todo>> result = new MutableLiveData<>();
        new Thread(() -> result.postValue(repository.findTodosForNotification())).start();
        return result;
    }

    public LiveData<List<Todo>> findCompleted() {
        MutableLiveData<List<Todo>> result = new MutableLiveData<>();
        new Thread(() -> result.postValue(repository.findCompleted())).start();
        return result;
    }

}
