package productivity_app_core;

import android.os.Build;

import java.time.LocalDateTime;
import java.util.List;

public class TaskService {
    private ITaskRepository taskRepository;

    public TaskService(ITaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        taskRepository.addTask(newTask);
    }

    public List<Task> viewTasks() {
        return taskRepository.getAllTasks();
    }

    public void editTask(int taskId, String newTitle, String newDescription) {
        Task task = taskRepository.getTaskById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setTitle(newTitle);
        task.setDescription(newDescription);
        taskRepository.updateTask(task);
    }

    public void deleteTask(int taskId) {
        taskRepository.deleteTask(taskId);
    }

    public void markTaskAsComplete(int taskId) {
        Task task = taskRepository.getTaskById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        task.setComplete(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            task.setCompletionDate(LocalDateTime.now());
        }
        taskRepository.updateTask(task);
    }

    public Statistics viewStatistics() {
        List<Task> tasks = taskRepository.getAllTasks();
        int totalTasks = tasks.size();
        long completedTasks = tasks.stream().filter(Task::isComplete).count();
        long pendingTasks = totalTasks - completedTasks;
        long tasksCompletedToday = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tasksCompletedToday = tasks.stream().filter(task -> task.isComplete() && LocalDateTime.now().equals(task.getCompletionDate())).count();
        }

        Statistics statistics = new Statistics();
        statistics.setTotalTasks(totalTasks);
        statistics.setCompletedTasks((int) completedTasks);
        statistics.setPendingTasks((int) pendingTasks);
        statistics.setTasksCompletedToday((int) tasksCompletedToday);

        return statistics;
    }
}
