import java.io.IOException;
import java.util.Scanner;

public class TimeManager {
  private final CLIView view;
  private boolean stopped = false;
  private boolean paused = false;

  public TimeManager(CLIView view) {
    this.view = view;
  }

  public void startPhase(PomodoroSession.Phase phase, int minutes) {
    int remainingSeconds = minutes * 60;
    Scanner scanner = new Scanner(System.in);
    view.printOptions();
    while (remainingSeconds > 0 && !stopped) {
      int displayMinutes = remainingSeconds / 60;
      int displaySeconds = remainingSeconds % 60;
      view.printClock(displayMinutes, displaySeconds, phase);

      try {
        if (System.in.available() > 0) {
          String input = scanner.nextLine().trim();
          if (input.equalsIgnoreCase("p")) {
            this.pauseTimer();
            System.out.println(paused ? "\nPaused" : "\nResumed");
          } else if (input.equalsIgnoreCase("s")) {
            this.resetTimer();
            System.out.println("\nStopped!");
            break;
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        break;
      }

      if (!paused) {
        remainingSeconds--;
        SoundPlayer.play("/sounds/tick.wav");
      }
    }

    if (!stopped) {
      System.out.println("\nPhase complete!");
    }
  }

  public void pauseTimer() {
    this.paused = !this.paused;
  }

  public void resetTimer() {
    this.stopped = true;
  }

  public boolean isStopped() {
    return stopped;
  }
}
