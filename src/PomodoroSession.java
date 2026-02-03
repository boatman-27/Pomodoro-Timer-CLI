import java.awt.*;
import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.Map;

public class PomodoroSession {
  private final Map<Phase, Integer> phaseDurations = new EnumMap<>(Phase.class);
  private final int workSessionsBeforeLongBreak;
  private Phase currentPhase;
  private int remainingTime; // minutes
  private int completedWorkSessions;

  public PomodoroSession(
      int workDuration,
      int shortBreakDuration,
      int longBreakDuration,
      int workSessionsBeforeLongBreak) {
    phaseDurations.put(Phase.WORK, workDuration);
    phaseDurations.put(Phase.SHORT_BREAK, shortBreakDuration);
    phaseDurations.put(Phase.LONG_BREAK, longBreakDuration);

    this.workSessionsBeforeLongBreak = workSessionsBeforeLongBreak;
    this.currentPhase = Phase.WORK;
    this.remainingTime = phaseDurations.get(Phase.WORK);
    this.completedWorkSessions = 0;

    System.out.println("Starting with " + currentPhase);
  }

  public Phase getCurrentPhase() {
    return currentPhase;
  }

  public int getRemainingTime() {
    return remainingTime;
  }

  public int getPhaseDuration(Phase phase) {
    return switch (phase) {
      case WORK -> phaseDurations.get(Phase.WORK);
      case SHORT_BREAK -> phaseDurations.get(Phase.SHORT_BREAK);
      case LONG_BREAK -> phaseDurations.get(Phase.LONG_BREAK);
    };
  }

  public Map.Entry<Phase, Phase> phaseCompleted() {
    Phase toPhase = null;
    Phase fromPhase = currentPhase;

    switch (currentPhase) {
      case WORK -> {
        toPhase = handleWorkCompleted();
      }
      case SHORT_BREAK, LONG_BREAK -> startNextWorkOrLongBreak();
    }
    return new AbstractMap.SimpleEntry<>(fromPhase, toPhase);
  }

  private Phase handleWorkCompleted() {
    completedWorkSessions++;
    if (completedWorkSessions == workSessionsBeforeLongBreak) {
      return startLongBreak();
    } else {
      return startShortBreak();
    }
  }

  private void startNextWorkOrLongBreak() {
    if (currentPhase == Phase.LONG_BREAK) {
      // After long break, reset cycle
      completedWorkSessions = 0;
    }
    startWork();
  }

  private void startWork() {
    currentPhase = Phase.WORK;
    remainingTime = phaseDurations.get(Phase.WORK);
  }

  private Phase startShortBreak() {
    currentPhase = Phase.SHORT_BREAK;
    remainingTime = phaseDurations.get(Phase.SHORT_BREAK);
    return Phase.SHORT_BREAK;
  }

  private Phase startLongBreak() {
    currentPhase = Phase.LONG_BREAK;

    remainingTime = phaseDurations.get(Phase.LONG_BREAK);
    return Phase.LONG_BREAK;
  }

  public boolean isCycleComplete() {
    // A cycle is complete if the last phase was a long break
    return currentPhase == Phase.LONG_BREAK && completedWorkSessions == 0;
  }

  public enum Phase {
    WORK,
    SHORT_BREAK,
    LONG_BREAK
  }
}
