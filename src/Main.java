public class Main {
  static void main(String[] args) {
    CLIView view = new CLIView();
    PresetManager presetManager = new PresetManager();
    TaskManager taskManager = new TaskManager();
    Statistics statistics = new Statistics();
    Command cmd = new Command();
    PomodoroRunner runner = new PomodoroRunner();

    runner.mainMenu(presetManager, taskManager, statistics, view, cmd);
  }
}
