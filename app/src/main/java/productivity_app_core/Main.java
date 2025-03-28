package productivity_app_core;

public class Main {
    public static void main(String[] args) {
        final ITaskRepository taskRepository = new InMemoryTaskRepository();
        final TaskService taskService = new TaskService(taskRepository);
        taskService.createTask("Task 1", "Description 1");
        taskService.createTask("Task 2", "Description 2");
        taskService.markTaskAsComplete(1);
        System.out.println(taskService.viewTasks());

        final Statistics statistics = taskService.viewStatistics();
        System.out.println("Total Tasks: " + statistics.getTotalTasks());
        System.out.println("Completed Tasks: " + statistics.getCompletedTasks());
        System.out.println("Pending Tasks: " + statistics.getPendingTasks());
        System.out.println("Tasks Completed Today: " + statistics.getTasksCompletedToday());
    }
}