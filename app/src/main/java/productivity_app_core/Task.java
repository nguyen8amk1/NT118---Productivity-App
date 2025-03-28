package productivity_app_core;
import java.time.LocalDateTime;

public class Task {
    private static int idCounter = 0;
    private int id;
    private String title;
    private String description;
    private boolean isComplete;
    private LocalDateTime completionDate;

    public Task(String title, String description) {
        this.id = ++idCounter;
        this.title = title;
        this.description = description;
        this.isComplete = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }
}
