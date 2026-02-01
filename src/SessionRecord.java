import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @param workDuration minutes
 * @param taskId -1 if no task
 */
public record SessionRecord(
    LocalDateTime startTime,
    LocalDateTime endTime,
    int workDuration,
    int taskId,
    String taskName,
    boolean completed)
    implements Serializable {}
