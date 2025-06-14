import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.time.Instant;
import java.util.List;
import java.nio.file.Path;

public class Sysmon {
    public static void main(String[] args) {
        System.out.println("Sysmon app placeholder");
    }
}

// Abstract designs for the sysmon module following SOLID and secure coding practices

// Generic asynchronous use-case interface
interface UseCase<I, O> {
    CompletableFuture<O> execute(I input);
}

// Value objects for system metrics
final class CpuMetrics {
    private final double usagePercent;
    private final Instant timestamp;

    public CpuMetrics(double usagePercent, Instant timestamp) {
        if (usagePercent < 0 || usagePercent > 100) {
            throw new IllegalArgumentException("CPU usage percent must be between 0 and 100");
        }
        this.usagePercent = usagePercent;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public double getUsagePercent() { return usagePercent; }
    public Instant getTimestamp() { return timestamp; }
}

final class MemoryMetrics {
    private final long usedBytes;
    private final long totalBytes;
    private final Instant timestamp;

    public MemoryMetrics(long usedBytes, long totalBytes, Instant timestamp) {
        if (usedBytes < 0 || totalBytes <= 0 || usedBytes > totalBytes) {
            throw new IllegalArgumentException("Invalid memory usage values");
        }
        this.usedBytes = usedBytes;
        this.totalBytes = totalBytes;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public long getUsedBytes() { return usedBytes; }
    public long getTotalBytes() { return totalBytes; }
    public Instant getTimestamp() { return timestamp; }
}

final class DiskMetrics {
    private final double usagePercent;
    private final Instant timestamp;

    public DiskMetrics(double usagePercent, Instant timestamp) {
        if (usagePercent < 0 || usagePercent > 100) {
            throw new IllegalArgumentException("Disk usage percent must be between 0 and 100");
        }
        this.usagePercent = usagePercent;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public double getUsagePercent() { return usagePercent; }
    public Instant getTimestamp() { return timestamp; }
}

final class NetworkMetrics {
    private final long bytesSent;
    private final long bytesReceived;
    private final Instant timestamp;

    public NetworkMetrics(long bytesSent, long bytesReceived, Instant timestamp) {
        if (bytesSent < 0 || bytesReceived < 0) {
            throw new IllegalArgumentException("Network byte counts cannot be negative");
        }
        this.bytesSent = bytesSent;
        this.bytesReceived = bytesReceived;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public long getBytesSent() { return bytesSent; }
    public long getBytesReceived() { return bytesReceived; }
    public Instant getTimestamp() { return timestamp; }
}

final class SystemMetrics {
    private final CpuMetrics cpu;
    private final MemoryMetrics memory;
    private final DiskMetrics disk;
    private final NetworkMetrics network;
    private final Instant timestamp;

    public SystemMetrics(CpuMetrics cpu, MemoryMetrics memory, DiskMetrics disk, NetworkMetrics network, Instant timestamp) {
        this.cpu = Objects.requireNonNull(cpu, "cpu metrics cannot be null");
        this.memory = Objects.requireNonNull(memory, "memory metrics cannot be null");
        this.disk = Objects.requireNonNull(disk, "disk metrics cannot be null");
        this.network = Objects.requireNonNull(network, "network metrics cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public CpuMetrics getCpu() { return cpu; }
    public MemoryMetrics getMemory() { return memory; }
    public DiskMetrics getDisk() { return disk; }
    public NetworkMetrics getNetwork() { return network; }
    public Instant getTimestamp() { return timestamp; }
}

// Value object for anomaly prediction
final class AnomalyPrediction {
    private final boolean anomalyDetected;
    private final String description;
    private final Instant predictedAt;

    public AnomalyPrediction(boolean anomalyDetected, String description, Instant predictedAt) {
        this.anomalyDetected = anomalyDetected;
        this.description = Objects.requireNonNull(description, "description cannot be null");
        this.predictedAt = Objects.requireNonNull(predictedAt, "predictedAt cannot be null");
    }

    public boolean isAnomalyDetected() { return anomalyDetected; }
    public String getDescription() { return description; }
    public Instant getPredictedAt() { return predictedAt; }
}

// Commands and results
final class MonitorMetricsCommand {
    private final int intervalSeconds;
    private final int durationSeconds;

    public MonitorMetricsCommand(int intervalSeconds, int durationSeconds) {
        if (intervalSeconds <= 0 || durationSeconds <= 0) {
            throw new IllegalArgumentException("Interval and duration must be positive");
        }
        this.intervalSeconds = intervalSeconds;
        this.durationSeconds = durationSeconds;
    }

    public int getIntervalSeconds() { return intervalSeconds; }
    public int getDurationSeconds() { return durationSeconds; }
}

final class MonitorMetricsResult {
    private final SystemMetrics metrics;

    public MonitorMetricsResult(SystemMetrics metrics) {
        this.metrics = Objects.requireNonNull(metrics, "metrics cannot be null");
    }

    public SystemMetrics getMetrics() { return metrics; }
}

final class PredictAnomalyCommand {
    private final List<SystemMetrics> history;

    public PredictAnomalyCommand(List<SystemMetrics> history) {
        this.history = Objects.requireNonNull(history, "history cannot be null");
    }

    public List<SystemMetrics> getHistory() { return history; }
}

final class PredictAnomalyResult {
    private final AnomalyPrediction prediction;

    public PredictAnomalyResult(AnomalyPrediction prediction) {
        this.prediction = Objects.requireNonNull(prediction, "prediction cannot be null");
    }

    public AnomalyPrediction getPrediction() { return prediction; }
}

final class RetrieveHistoryCommand {
    private final Instant from;
    private final Instant to;

    public RetrieveHistoryCommand(Instant from, Instant to) {
        this.from = Objects.requireNonNull(from, "from cannot be null");
        this.to = Objects.requireNonNull(to, "to cannot be null");
    }

    public Instant getFrom() { return from; }
    public Instant getTo() { return to; }
}

final class RetrieveHistoryResult {
    private final List<SystemMetrics> history;

    public RetrieveHistoryResult(List<SystemMetrics> history) {
        this.history = Objects.requireNonNull(history, "history cannot be null");
    }

    public List<SystemMetrics> getHistory() { return history; }
}

final class ExportMetricsCommand {
    private final Path outputPath;

    public ExportMetricsCommand(Path outputPath) {
        this.outputPath = Objects.requireNonNull(outputPath, "outputPath cannot be null");
    }

    public Path getOutputPath() { return outputPath; }
}

final class ExportMetricsResult {
    private final Path filePath;
    private final Instant timestamp;

    public ExportMetricsResult(Path filePath, Instant timestamp) {
        this.filePath = Objects.requireNonNull(filePath, "filePath cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public Path getFilePath() { return filePath; }
    public Instant getTimestamp() { return timestamp; }
}

// Use-case interfaces
interface MonitorMetricsUseCase extends UseCase<MonitorMetricsCommand, MonitorMetricsResult> {}
interface PredictAnomalyUseCase extends UseCase<PredictAnomalyCommand, PredictAnomalyResult> {}
interface RetrieveHistoryUseCase extends UseCase<RetrieveHistoryCommand, RetrieveHistoryResult> {}
interface ExportMetricsUseCase extends UseCase<ExportMetricsCommand, ExportMetricsResult> {}

// Domain-specific exceptions
class SysmonException extends Exception {
    public SysmonException(String message) { super(message); }
    public SysmonException(String message, Throwable cause) { super(message, cause); }
}

class MonitoringException extends SysmonException {
    public MonitoringException(String reason) { super("System monitoring failed: " + reason); }
}

class PredictionException extends SysmonException {
    public PredictionException(String reason) { super("Anomaly prediction failed: " + reason); }
}

class HistoryRetrievalException extends SysmonException {
    public HistoryRetrievalException(String reason) { super("History retrieval failed: " + reason); }
}

class ExportException extends SysmonException {
    public ExportException(String reason) { super("Metrics export failed: " + reason); }
} 