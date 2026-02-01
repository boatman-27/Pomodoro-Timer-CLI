import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CLIView {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_BLUE = "\u001B[34m";

  public void printFullMenu(PresetManager presetManager) {
    System.out.println(ANSI_YELLOW + "\n=== Welcome to the Pomodoro Timer App! ===\n" + ANSI_RESET);

    Collection<Preset> presets = presetManager.getPresets();
    int i = 1;
    for (Preset preset : presets) {
      System.out.printf(
          ANSI_CYAN
              + "%d. %s\n"
              + ANSI_RESET
              + "   %s\n"
              + ANSI_GREEN
              + "   Work: %d min | Short Break: %d min | Long Break: %d min | Sessions Before Long Break: %d\n"
              + ANSI_RESET,
          i++,
          preset.name(),
          preset.description(),
          preset.workDuration(),
          preset.shortBreakDuration(),
          preset.longBreakDuration(),
          preset.workSessionsBeforeLongBreak());
    }

    System.out.printf(ANSI_CYAN + "%d. %s\n" + ANSI_RESET, i, "Create a Custom Preset");
    System.out.printf(ANSI_CYAN + "%d. %s\n" + ANSI_RESET, ++i, "Edit Presets");
    System.out.printf(ANSI_CYAN + "%d. %s\n" + ANSI_RESET, ++i, "Manage Tasks");
    System.out.printf(ANSI_CYAN + "%d. %s\n" + ANSI_RESET, ++i, "View Statistics");
    System.out.printf(ANSI_CYAN + "%d. %s\n" + ANSI_RESET, 0, "Exit Program");

    System.out.println(
        ANSI_YELLOW + "\nSelect a preset by number, or choose an option." + ANSI_RESET);

    System.out.print("Enter your choice: ");
  }

  public void printClock(int displayMinutes, int displaySeconds, PomodoroSession.Phase phase) {
    // Pick color based on phase
    String color;
    switch (phase) {
      case WORK:
        color = ANSI_GREEN;
        break;
      case SHORT_BREAK:
        color = ANSI_CYAN;
        break;
      case LONG_BREAK:
        color = ANSI_PURPLE;
        break;
      default:
        color = ANSI_RESET;
    }

    // Format time MM:SS
    String time = String.format("%02d:%02d", displayMinutes, displaySeconds);

    System.out.print(color + "\r[" + phase + "] " + time + ANSI_RESET);

    // Flush for immediate printing
    System.out.flush();
  }

  public void printOptions() {
    System.out.println();
    System.out.println(ANSI_YELLOW + "[ Controls ]" + ANSI_RESET);
    System.out.println(ANSI_CYAN + "p" + ANSI_RESET + " → Pause / Resume");
    System.out.println(ANSI_CYAN + "s" + ANSI_RESET + " → Stop Session");
    System.out.println();
  }

  public void printTransitionMessage(
      PomodoroSession.Phase from, PomodoroSession.Phase to, int nextPhaseMinutes) {
    // Clear the clock line
    System.out.print("\r");
    System.out.println();

    SoundPlayer.play("/sounds/change.wav");

    // Completion message
    String fromColor =
        switch (from) {
          case WORK -> ANSI_GREEN;
          case SHORT_BREAK -> ANSI_CYAN;
          case LONG_BREAK -> ANSI_PURPLE;
        };

    System.out.println(
        fromColor + "✔ " + from.name().replace("_", " ") + " complete." + ANSI_RESET);

    // Transition message
    String toColor =
        switch (to) {
          case WORK -> ANSI_GREEN;
          case SHORT_BREAK -> ANSI_CYAN;
          case LONG_BREAK -> ANSI_PURPLE;
        };

    System.out.println(
        toColor
            + "→ Starting "
            + to.name().replace("_", " ")
            + " ("
            + nextPhaseMinutes
            + " min)"
            + ANSI_RESET);

    System.out.println();

    // Small pause so it doesn't feel instant and robotic
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {
    }
  }

  public void printSessionComplete() {
    System.out.println();
    System.out.println(ANSI_GREEN + "🎉 Pomodoro cycle complete!" + ANSI_RESET);
    System.out.println(ANSI_CYAN + "Take a longer break or start a new session." + ANSI_RESET);
    System.out.println();
  }

  public void printTaskMenu() {
    System.out.println(ANSI_YELLOW + "\n=== Task Management ===" + ANSI_RESET);
    System.out.println(ANSI_CYAN + "1." + ANSI_RESET + " View All Tasks");
    System.out.println(ANSI_CYAN + "2." + ANSI_RESET + " Add New Task");
    System.out.println(ANSI_CYAN + "3." + ANSI_RESET + " Mark Task Complete");
    System.out.println(ANSI_CYAN + "4." + ANSI_RESET + " Delete Task");
    System.out.println(ANSI_CYAN + "0." + ANSI_RESET + " Back to Main Menu");
    System.out.print("\nEnter your choice: ");
  }

  public void printTasks(List<Task> tasks) {
    if (tasks.isEmpty()) {
      System.out.println(ANSI_YELLOW + "\nNo tasks found." + ANSI_RESET);
      return;
    }

    System.out.println(ANSI_YELLOW + "\n=== Tasks ===" + ANSI_RESET);
    for (Task task : tasks) {
      String color = task.isCompleted() ? ANSI_GREEN : ANSI_CYAN;
      String status = task.isCompleted() ? "✓" : " ";
      System.out.printf(
          color + "[%s] %d. %s" + ANSI_RESET + " (%d/%d pomodoros)\n",
          status,
          task.getId(),
          task.getName(),
          task.getPomodorosCompleted(),
          task.getEstimatedPomodoros());
      if (!task.getDescription().isEmpty()) {
        System.out.println("   " + task.getDescription());
      }
    }
  }

  public void printTaskSelection(List<Task> tasks) {
    System.out.println(ANSI_YELLOW + "\n=== Select a Task ===" + ANSI_RESET);
    System.out.println(ANSI_CYAN + "0." + ANSI_RESET + " No task (just focus time)");

    List<Task> activeTasks = tasks.stream().filter(t -> !t.isCompleted()).toList();
    for (int i = 0; i < activeTasks.size(); i++) {
      Task task = activeTasks.get(i);
      System.out.printf(
          ANSI_CYAN + "%d. " + ANSI_RESET + "%s (%d/%d pomodoros)\n",
          i + 1,
          task.getName(),
          task.getPomodorosCompleted(),
          task.getEstimatedPomodoros());
    }

    System.out.print("\nSelect task: ");
  }

  public void printStatistics(Statistics stats) {
    System.out.println(ANSI_YELLOW + "\n╔════════════════════════════════════╗" + ANSI_RESET);
    System.out.println(ANSI_YELLOW + "║        📊 YOUR STATISTICS          ║" + ANSI_RESET);
    System.out.println(ANSI_YELLOW + "╚════════════════════════════════════╝" + ANSI_RESET);

    // Today
    System.out.println(ANSI_CYAN + "\n📅 Today:" + ANSI_RESET);
    System.out.printf(
        "   Sessions: "
            + ANSI_GREEN
            + "%d"
            + ANSI_RESET
            + " | Focus Time: "
            + ANSI_GREEN
            + "%d minutes (%s)\n"
            + ANSI_RESET,
        stats.getTodayCompletedSessions(),
        stats.getTodayFocusMinutes(),
        formatTime(stats.getTodayFocusMinutes()));

    // This Week
    System.out.println(ANSI_CYAN + "\n📆 This Week (Last 7 Days):" + ANSI_RESET);
    System.out.printf(
        "   Sessions: "
            + ANSI_GREEN
            + "%d"
            + ANSI_RESET
            + " | Focus Time: "
            + ANSI_GREEN
            + "%d minutes (%s)\n"
            + ANSI_RESET,
        stats.getWeekCompletedSessions(),
        stats.getWeekFocusMinutes(),
        formatTime(stats.getWeekFocusMinutes()));

    // This Month
    System.out.println(ANSI_CYAN + "\n📊 This Month (Last 30 Days):" + ANSI_RESET);
    System.out.printf(
        "   Sessions: "
            + ANSI_GREEN
            + "%d"
            + ANSI_RESET
            + " | Focus Time: "
            + ANSI_GREEN
            + "%d minutes (%s)\n"
            + ANSI_RESET,
        stats.getMonthCompletedSessions(),
        stats.getMonthFocusMinutes(),
        formatTime(stats.getMonthFocusMinutes()));

    // All Time
    System.out.println(ANSI_CYAN + "\n🏆 All Time:" + ANSI_RESET);
    System.out.printf(
        "   Sessions: "
            + ANSI_GREEN
            + "%d"
            + ANSI_RESET
            + " | Focus Time: "
            + ANSI_GREEN
            + "%d minutes (%s)\n"
            + ANSI_RESET,
        stats.getTotalCompletedSessions(),
        stats.getTotalFocusMinutes(),
        formatTime(stats.getTotalFocusMinutes()));

    // Streak
    int streak = stats.getCurrentStreak();
    String streakColor = streak > 0 ? ANSI_GREEN : ANSI_YELLOW;
    System.out.println(ANSI_CYAN + "\n🔥 Current Streak:" + ANSI_RESET);
    System.out.printf("   " + streakColor + "%d days" + ANSI_RESET + "\n", streak);

    // Task Breakdown
    Map<String, Integer> taskBreakdown = stats.getTaskBreakdown();
    if (!taskBreakdown.isEmpty()) {
      System.out.println(ANSI_CYAN + "\n📋 Time by Task:" + ANSI_RESET);
      taskBreakdown.entrySet().stream()
          .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
          .forEach(
              entry ->
                  System.out.printf(
                      "   %s: " + ANSI_GREEN + "%d minutes (%s)" + ANSI_RESET + "\n",
                      entry.getKey(),
                      entry.getValue(),
                      formatTime(entry.getValue())));
    }

    System.out.println();
  }

  private String formatTime(int minutes) {
    int hours = minutes / 60;
    int mins = minutes % 60;
    if (hours > 0) {
      return String.format("%dh %dm", hours, mins);
    } else {
      return String.format("%dm", mins);
    }
  }

  public void printWorkSessionComplete(Task task) {
    System.out.println();
    System.out.println(ANSI_GREEN + "✓ Work session complete!" + ANSI_RESET);
    if (task != null) {
      System.out.printf(
          ANSI_CYAN + "Task: %s (%d/%d pomodoros)" + ANSI_RESET + "\n",
          task.getName(),
          task.getPomodorosCompleted(),
          task.getEstimatedPomodoros());
    }
    System.out.println();
  }
}
