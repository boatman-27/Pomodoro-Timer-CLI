import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private static final String TASKS_FILE = "tasks.dat";
    private final List<Task> tasks = new ArrayList<>();

    public TaskManager() {
        loadFromFile();
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveToFile();
    }

    public void removeTask(int taskId) {
        tasks.removeIf(task -> task.getId() == taskId);
        saveToFile();
    }

    public Task getTask(int taskId) {
        return tasks.stream()
                .filter(task -> task.getId() == taskId)
                .findFirst()
                .orElse(null);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getActiveTasks() {
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }

    public List<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());
    }

    public void saveToFile() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(TASKS_FILE))) {
            // Save the next ID along with tasks
            output.writeInt(getNextId());
            output.writeObject(tasks);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(TASKS_FILE))) {
            int nextId = input.readInt();
            Task.setNextId(nextId);
            List<Task> loaded = (List<Task>) input.readObject();
            tasks.clear();
            tasks.addAll(loaded);
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, that's okay
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    private int getNextId() {
        return tasks.stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;
    }
}