package productivity_app_core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryTaskRepository implements ITaskRepository {
    private List<Task> tasks;

    public InMemoryTaskRepository() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public Optional<Task> getTaskById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst();
    }

    public void updateTask(Task task) {
        getTaskById(task.getId()).ifPresent(existingTask -> {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setComplete(task.isComplete());
        });
    }

    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }
}
