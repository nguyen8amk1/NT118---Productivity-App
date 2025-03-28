package productivity_app_core;

public class Statistics {
    private int totalTasks;
    private int completedTasks;
    private int pendingTasks;
    private int tasksCompletedToday;

    // Getters and Setters
    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getPendingTasks() {
        return pendingTasks;
    }

    public void setPendingTasks(int pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public int getTasksCompletedToday() {
        return tasksCompletedToday;
    }

    public void setTasksCompletedToday(int tasksCompletedToday) {
        this.tasksCompletedToday = tasksCompletedToday;
    }
}
