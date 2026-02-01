import java.io.Serializable;

public record Preset(
    String name,
    String description,
    int workDuration,
    int shortBreakDuration,
    int longBreakDuration,
    int workSessionsBeforeLongBreak)
    implements Serializable {
  public Preset {

    // validate inputs
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("name cannot be null or empty");
    }
    if (description == null || description.isEmpty()) {
      throw new IllegalArgumentException("description cannot be null or empty");
    }
    if (workDuration <= 0) {
      throw new IllegalArgumentException("workDuration cannot be negative");
    }
    if (shortBreakDuration <= 0) {
      throw new IllegalArgumentException("shortBreakDuration cannot be negative");
    }
    if (longBreakDuration <= 0) {
      throw new IllegalArgumentException("longBreakDuration cannot be negative");
    }
    if (workSessionsBeforeLongBreak <= 0) {
      throw new IllegalArgumentException("workSessionsBeforeLongBreak cannot be negative");
    }
  }
}
