import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable {
    private static int nextId = 1;
    private final int id;
    private final LocalDateTime createdAt;
    private String name;
    private String description;
    private boolean completed;
    private int pomodorosCompleted;
    private int estimatedPomodoros;
    private LocalDateTime completedAt;

    public Task(String name, String description, int estimatedPomodoros) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
        this.estimatedPomodoros = estimatedPomodoros;
        this.completed = false;
        this.pomodorosCompleted = 0;
        this.createdAt = LocalDateTime.now();
    }

    public static void setNextId(int nextId) {
        Task.nextId = nextId;
    }

    public void incrementPomodoros() {
        this.pomodorosCompleted++;
    }

    public void markComplete() {
        this.completed = true;
        this.completedAt = LocalDateTime.now();
    }

    public void markIncomplete() {
        this.completed = false;
        this.completedAt = null;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getPomodorosCompleted() {
        return pomodorosCompleted;
    }

    public int getEstimatedPomodoros() {
        return estimatedPomodoros;
    }

    public void setEstimatedPomodoros(int estimatedPomodoros) {
        this.estimatedPomodoros = estimatedPomodoros;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    @Override
    public String toString() {
        String status = completed ? "✓" : " ";
        return String.format(
                "[%s] %d. %s (%d/%d pomodoros)",
                status, id, name, pomodorosCompleted, estimatedPomodoros);
    }
}