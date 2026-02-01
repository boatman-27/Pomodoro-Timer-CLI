import java.time.LocalDateTime;
import java.util.*;

public class PomodoroRunner {
  public void run(
      Preset preset, Task selectedTask, TaskManager taskManager, Statistics stats, CLIView view) {
    if (preset != null) {
      // initialize a new session with the selected preset
      PomodoroSession session =
          new PomodoroSession(
              preset.workDuration(),
              preset.shortBreakDuration(),
              preset.longBreakDuration(),
              preset.workSessionsBeforeLongBreak());

      TimeManager timeManager = new TimeManager(view);

      while (!session.isCycleComplete()) {
        PomodoroSession.Phase current = session.getCurrentPhase();
        int minutes = session.getRemainingTime();

        LocalDateTime startTime = LocalDateTime.now();
        timeManager.startPhase(current, minutes);
        LocalDateTime endTime = LocalDateTime.now();

        if (timeManager.isStopped()) {
          System.out.println("Timer stopped by user.");
          break;
        }

        // Record the session if it was a work session
        if (current == PomodoroSession.Phase.WORK) {
          SessionRecord record =
              new SessionRecord(
                  startTime,
                  endTime,
                  minutes,
                  selectedTask != null ? selectedTask.getId() : -1,
                  selectedTask != null ? selectedTask.getName() : "No task",
                  !timeManager.isStopped());

          stats.recordSession(record);

          // Increment task pomodoros if a task was selected
          if (selectedTask != null && !timeManager.isStopped()) {
            selectedTask.incrementPomodoros();
            taskManager.saveToFile();
            view.printWorkSessionComplete(selectedTask);
          }
        }

        Map.Entry<PomodoroSession.Phase, PomodoroSession.Phase> result = session.phaseCompleted();

        PomodoroSession.Phase from = result.getKey();
        PomodoroSession.Phase to = result.getValue();

        if (to != null) {
          int nextPhaseMinutes = session.getPhaseDuration(to);
          view.printTransitionMessage(from, to, nextPhaseMinutes);
        } else {
          if (!timeManager.isStopped() && session.isCycleComplete()) {
            view.printSessionComplete();
          }
        }
      }
    }
  }

  public void mainMenu(
      PresetManager presetManager,
      TaskManager taskManager,
      Statistics stats,
      CLIView view,
      Command cmd) {
    Scanner scanner = new Scanner(System.in);

    int choice;
    do {
      view.printFullMenu(presetManager);

      Preset selectedPreset = null;
      Task selectedTask = null;
      Collection<Preset> presets = presetManager.getPresets();
      List<Preset> presetList = new ArrayList<>(presets);

      choice = cmd.userMenuInput(scanner);

      int customPresetIndex = presetList.size() + 1;
      int editPresetsIndex = presetList.size() + 2;
      int manageTasksIndex = presetList.size() + 3;
      int viewStatsIndex = presetList.size() + 4;

      if (choice >= 1 && choice <= presetList.size()) {
        selectedPreset = presetList.get(choice - 1);
        // Ask user to select a task
        selectedTask = Command.selectTask(taskManager, view, scanner);
      } else if (choice == customPresetIndex) {
        selectedPreset = Command.createCustomPreset(presetManager, scanner);
        if (selectedPreset != null) {
          selectedTask = Command.selectTask(taskManager, view, scanner);
        }
      } else if (choice == editPresetsIndex) {
        Command.editPreset(presetManager, scanner);
      } else if (choice == manageTasksIndex) {
        Command.manageTasksMenu(taskManager, view, scanner);
      } else if (choice == viewStatsIndex) {
        view.printStatistics(stats);
      } else if (choice == 0) {
        System.out.println("Goodbye!");
      } else {
        System.out.println("Invalid choice.");
      }

      if (selectedPreset != null) {
        this.run(selectedPreset, selectedTask, taskManager, stats, view);
      }

    } while (choice != 0);
  }
}
