import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Command {
  static Preset createCustomPreset(PresetManager manager, Scanner scanner) {
    System.out.print("Enter preset name: ");
    String name = scanner.nextLine();

    System.out.print("Enter description: ");
    String desc = scanner.nextLine();

    System.out.print("Work duration (minutes): ");
    int work = scanner.nextInt();

    System.out.print("Short break duration (minutes): ");
    int shortBreak = scanner.nextInt();

    System.out.print("Long break duration (minutes): ");
    int longBreak = scanner.nextInt();

    System.out.print("Work sessions before long break: ");
    int cycles = scanner.nextInt();
    scanner.nextLine(); // consume leftover newline

    Preset custom = new Preset(name, desc, work, shortBreak, longBreak, cycles);
    manager.addPreset(name, custom);
    System.out.println("Custom preset added!");
    return custom;
  }

  static void editPreset(PresetManager manager, Scanner scanner) {
    List<Preset> presets = new ArrayList<>(manager.getPresets());

    if (presets.isEmpty()) {
      System.out.println("No presets to edit.");
      return;
    }

    System.out.println("\nSelect a preset to edit:");
    for (int i = 0; i < presets.size(); i++) {
      System.out.printf("%d. %s\n", i + 1, presets.get(i).name());
    }
    System.out.println("0. Cancel");

    int choice;
    while (true) {
      System.out.print("Choice: ");
      if (scanner.hasNextInt()) {
        choice = scanner.nextInt();
        scanner.nextLine();
        if (choice >= 0 && choice <= presets.size()) break;
      } else {
        scanner.nextLine();
      }
      System.out.println("Invalid choice.");
    }

    if (choice == 0) return;

    Preset old = presets.get(choice - 1);

    System.out.println("\nPress Enter to keep current value.");

    System.out.printf("Name (%s): ", old.name());
    String name = scanner.nextLine();
    if (name.isBlank()) name = old.name();

    System.out.printf("Description (%s): ", old.description());
    String desc = scanner.nextLine();
    if (desc.isBlank()) desc = old.description();

    int work = readIntOrKeep(scanner, "Work duration", old.workDuration());
    int shortBreak = readIntOrKeep(scanner, "Short break duration", old.shortBreakDuration());
    int longBreak = readIntOrKeep(scanner, "Long break duration", old.longBreakDuration());
    int cycles =
        readIntOrKeep(
            scanner, "Work sessions before long break", old.workSessionsBeforeLongBreak());

    Preset updated = new Preset(name, desc, work, shortBreak, longBreak, cycles);

    manager.removePreset(old.name());
    manager.addPreset(name, updated);

    System.out.println("Preset updated.");
  }

  static void manageTasksMenu(TaskManager taskManager, CLIView view, Scanner scanner) {
    int choice;
    do {
      view.printTaskMenu();
      choice = readInt(scanner);

      switch (choice) {
        case 1:
          viewTasks(taskManager, view);
          break;
        case 2:
          addTask(taskManager, scanner);
          break;
        case 3:
          markTaskComplete(taskManager, view, scanner);
          break;
        case 4:
          deleteTask(taskManager, view, scanner);
          break;
        case 0:
          break;
        default:
          System.out.println("Invalid choice.");
      }
    } while (choice != 0);
  }

  static void viewTasks(TaskManager taskManager, CLIView view) {
    List<Task> tasks = taskManager.getAllTasks();
    view.printTasks(tasks);
  }

  static void addTask(TaskManager taskManager, Scanner scanner) {
    System.out.print("\nEnter task name: ");
    String name = scanner.nextLine();

    System.out.print("Enter task description (optional): ");
    String desc = scanner.nextLine();

    System.out.print("Estimated pomodoros to complete: ");
    int estimated = readInt(scanner);

    Task task = new Task(name, desc, estimated);
    taskManager.addTask(task);

    System.out.println(CLIView.ANSI_GREEN + "✓ Task added!" + CLIView.ANSI_RESET);
  }

  static void markTaskComplete(TaskManager taskManager, CLIView view, Scanner scanner) {
    List<Task> activeTasks = taskManager.getActiveTasks();

    if (activeTasks.isEmpty()) {
      System.out.println("No active tasks.");
      return;
    }

    view.printTasks(activeTasks);
    System.out.print("\nEnter task ID to mark complete (0 to cancel): ");
    int taskId = readInt(scanner);

    if (taskId == 0) return;

    Task task = taskManager.getTask(taskId);
    if (task != null) {
      task.markComplete();
      taskManager.saveToFile();
      System.out.println(CLIView.ANSI_GREEN + "✓ Task marked complete!" + CLIView.ANSI_RESET);
    } else {
      System.out.println("Task not found.");
    }
  }

  static void deleteTask(TaskManager taskManager, CLIView view, Scanner scanner) {
    List<Task> tasks = taskManager.getAllTasks();

    if (tasks.isEmpty()) {
      System.out.println("No tasks to delete.");
      return;
    }

    view.printTasks(tasks);
    System.out.print("\nEnter task ID to delete (0 to cancel): ");
    int taskId = readInt(scanner);

    if (taskId == 0) return;

    taskManager.removeTask(taskId);
    System.out.println(CLIView.ANSI_GREEN + "✓ Task deleted!" + CLIView.ANSI_RESET);
  }

  static Task selectTask(TaskManager taskManager, CLIView view, Scanner scanner) {
    List<Task> activeTasks = taskManager.getActiveTasks();

    if (activeTasks.isEmpty()) {
      System.out.println(
          CLIView.ANSI_YELLOW
              + "No active tasks. Starting focus session without a task."
              + CLIView.ANSI_RESET);
      return null;
    }

    view.printTaskSelection(activeTasks);
    int choice = readInt(scanner);

    if (choice == 0) {
      return null;
    } else if (choice > 0 && choice <= activeTasks.size()) {
      return activeTasks.get(choice - 1);
    } else {
      System.out.println("Invalid choice. Starting without a task.");
      return null;
    }
  }

  private static int readIntOrKeep(Scanner scanner, String label, int current) {
    System.out.printf("%s (%d): ", label, current);
    String input = scanner.nextLine();
    if (input.isBlank()) return current;

    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException e) {
      System.out.println("Invalid number. Keeping previous value.");
      return current;
    }
  }

  private static int readInt(Scanner scanner) {
    while (!scanner.hasNextInt()) {
      System.out.println("Please enter a valid number:");
      scanner.nextLine();
    }
    int input = scanner.nextInt();
    scanner.nextLine();
    return input;
  }

  public int userMenuInput(Scanner scanner) {
    return readInt(scanner);
  }
}
