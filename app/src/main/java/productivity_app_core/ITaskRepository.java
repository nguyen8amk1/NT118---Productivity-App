package productivity_app_core;

import java.util.List;
import java.util.Optional;

public interface ITaskRepository {
    public void addTask(Task task);
    public List<Task> getAllTasks();
    public Optional<Task> getTaskById(int id);
    public void updateTask(Task task);
    public void deleteTask(int id);
}
