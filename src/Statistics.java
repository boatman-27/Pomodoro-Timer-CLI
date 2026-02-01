import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
  private static final String STATS_FILE = "statistics.dat";
  private final List<SessionRecord> sessions = new ArrayList<>();

  public Statistics() {
    loadFromFile();
  }

  public void recordSession(SessionRecord session) {
    sessions.add(session);
    saveToFile();
  }

  public int getTodayCompletedSessions() {
    LocalDate today = LocalDate.now();
    return (int)
        sessions.stream()
            .filter(SessionRecord::completed)
            .filter(s -> s.startTime().toLocalDate().equals(today))
            .count();
  }

  public int getTodayFocusMinutes() {
    LocalDate today = LocalDate.now();
    return sessions.stream()
        .filter(SessionRecord::completed)
        .filter(s -> s.startTime().toLocalDate().equals(today))
        .mapToInt(SessionRecord::workDuration)
        .sum();
  }

  public int getWeekCompletedSessions() {
    LocalDate weekAgo = LocalDate.now().minusDays(7);
    return (int)
        sessions.stream()
            .filter(SessionRecord::completed)
            .filter(s -> s.startTime().toLocalDate().isAfter(weekAgo))
            .count();
  }

  public int getWeekFocusMinutes() {
    LocalDate weekAgo = LocalDate.now().minusDays(7);
    return sessions.stream()
        .filter(SessionRecord::completed)
        .filter(s -> s.startTime().toLocalDate().isAfter(weekAgo))
        .mapToInt(SessionRecord::workDuration)
        .sum();
  }

  public int getMonthCompletedSessions() {
    LocalDate monthAgo = LocalDate.now().minusDays(30);
    return (int)
        sessions.stream()
            .filter(SessionRecord::completed)
            .filter(s -> s.startTime().toLocalDate().isAfter(monthAgo))
            .count();
  }

  public int getMonthFocusMinutes() {
    LocalDate monthAgo = LocalDate.now().minusDays(30);
    return sessions.stream()
        .filter(SessionRecord::completed)
        .filter(s -> s.startTime().toLocalDate().isAfter(monthAgo))
        .mapToInt(SessionRecord::workDuration)
        .sum();
  }

  public int getCurrentStreak() {
    if (sessions.isEmpty()) return 0;

    LocalDate today = LocalDate.now();
    LocalDate checkDate = today;

    // Check if there's a session today
    boolean hasSessionToday =
        sessions.stream()
            .filter(SessionRecord::completed)
            .anyMatch(s -> s.startTime().toLocalDate().equals(today));

    if (!hasSessionToday) {
      // Check yesterday
      checkDate = today.minusDays(1);
      LocalDate finalCheckDate = checkDate;
      boolean hasSessionYesterday =
          sessions.stream()
              .filter(SessionRecord::completed)
              .anyMatch(s -> s.startTime().toLocalDate().equals(finalCheckDate));

      if (!hasSessionYesterday) {
        return 0;
      }
    }

    int streak = 0;
    while (true) {
      final LocalDate currentDate = checkDate;
      boolean hasSession =
          sessions.stream()
              .filter(SessionRecord::completed)
              .anyMatch(s -> s.startTime().toLocalDate().equals(currentDate));

      if (!hasSession) break;

      streak++;
      checkDate = checkDate.minusDays(1);
    }

    return streak;
  }

  public Map<String, Integer> getTaskBreakdown() {
    Map<String, Integer> breakdown = new HashMap<>();

    sessions.stream()
        .filter(SessionRecord::completed)
        .filter(s -> s.taskId() != -1)
        .forEach(s -> breakdown.merge(s.taskName(), s.workDuration(), Integer::sum));

    return breakdown;
  }

  public int getTotalCompletedSessions() {
    return (int) sessions.stream().filter(SessionRecord::completed).count();
  }

  public int getTotalFocusMinutes() {
    return sessions.stream()
        .filter(SessionRecord::completed)
        .mapToInt(SessionRecord::workDuration)
        .sum();
  }

  public List<SessionRecord> getAllSessions() {
    return new ArrayList<>(sessions);
  }

  public void saveToFile() {
    try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(STATS_FILE))) {
      output.writeObject(sessions);
    } catch (IOException e) {
      System.err.println("Error saving statistics: " + e.getMessage());
    }
  }

  public void loadFromFile() {
    try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(STATS_FILE))) {
      List<SessionRecord> loaded = (List<SessionRecord>) input.readObject();
      sessions.clear();
      sessions.addAll(loaded);
    } catch (FileNotFoundException e) {
      // File doesn't exist yet, that's okay
    } catch (IOException | ClassNotFoundException e) {
      System.err.println("Error loading statistics: " + e.getMessage());
    }
  }
}
