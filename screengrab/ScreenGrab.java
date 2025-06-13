import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.time.Instant;
import java.util.List;
import java.nio.file.Path;

public class ScreenGrab {
    public static void main(String[] args) {
        System.out.println("Screengrab app placeholder");
    }
}

// Abstract designs for the screengrab module following SOLID and secure coding practices

// Generic asynchronous use-case interface
interface UseCase<I, O> {
    CompletableFuture<O> execute(I input);
}

// Capture modes for screenshots
enum CaptureMode { FULL, WINDOW, REGION }

// Value object representing a capture region
final class Region {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Region(int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}

// Value object representing a screenshot
final class Screenshot {
    private final Path path;
    private final CaptureMode mode;
    private final Region region; // null unless mode == REGION
    private final String windowId; // null unless mode == WINDOW
    private final Instant timestamp;

    public Screenshot(Path path, CaptureMode mode, Region region, String windowId, Instant timestamp) {
        this.path = Objects.requireNonNull(path, "path cannot be null");
        this.mode = Objects.requireNonNull(mode, "mode cannot be null");
        if (mode == CaptureMode.REGION) {
            this.region = Objects.requireNonNull(region, "region cannot be null for REGION mode");
        } else {
            this.region = null;
        }
        if (mode == CaptureMode.WINDOW) {
            this.windowId = Objects.requireNonNull(windowId, "windowId cannot be null for WINDOW mode");
        } else {
            this.windowId = null;
        }
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public Path getPath() { return path; }
    public CaptureMode getMode() { return mode; }
    public Region getRegion() { return region; }
    public String getWindowId() { return windowId; }
    public Instant getTimestamp() { return timestamp; }
}

// Value object representing an AI annotation
enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
final class Annotation {
    private final Region region;
    private final String label;
    private final double confidence;
    private final Instant timestamp;

    public Annotation(Region region, String label, double confidence, Instant timestamp) {
        this.region = Objects.requireNonNull(region, "region cannot be null");
        this.label = Objects.requireNonNull(label, "label cannot be null");
        if (confidence < 0.0 || confidence > 1.0) {
            throw new IllegalArgumentException("confidence must be between 0.0 and 1.0");
        }
        this.confidence = confidence;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public Region getRegion() { return region; }
    public String getLabel() { return label; }
    public double getConfidence() { return confidence; }
    public Instant getTimestamp() { return timestamp; }
}

// Command and result for capturing screenshots
final class CaptureScreenshotCommand {
    private final CaptureMode mode;
    private final Region region;
    private final String windowId;
    private final Path outputPath;

    public CaptureScreenshotCommand(CaptureMode mode, Region region, String windowId, Path outputPath) {
        this.mode = Objects.requireNonNull(mode, "mode cannot be null");
        if (mode == CaptureMode.REGION) {
            this.region = Objects.requireNonNull(region, "region cannot be null for REGION mode");
        } else {
            this.region = null;
        }
        if (mode == CaptureMode.WINDOW) {
            this.windowId = Objects.requireNonNull(windowId, "windowId cannot be null for WINDOW mode");
        } else {
            this.windowId = null;
        }
        this.outputPath = Objects.requireNonNull(outputPath, "outputPath cannot be null");
    }

    public CaptureMode getMode() { return mode; }
    public Region getRegion() { return region; }
    public String getWindowId() { return windowId; }
    public Path getOutputPath() { return outputPath; }
}

final class CaptureScreenshotResult {
    private final Screenshot screenshot;

    public CaptureScreenshotResult(Screenshot screenshot) {
        this.screenshot = Objects.requireNonNull(screenshot, "screenshot cannot be null");
    }

    public Screenshot getScreenshot() { return screenshot; }
}

// Command and result for AI annotation
final class AnnotateScreenshotCommand {
    private final Screenshot screenshot;

    public AnnotateScreenshotCommand(Screenshot screenshot) {
        this.screenshot = Objects.requireNonNull(screenshot, "screenshot cannot be null");
    }

    public Screenshot getScreenshot() { return screenshot; }
}

final class AnnotateScreenshotResult {
    private final Path annotatedPath;
    private final List<Annotation> annotations;
    private final Instant timestamp;

    public AnnotateScreenshotResult(Path annotatedPath, List<Annotation> annotations, Instant timestamp) {
        this.annotatedPath = Objects.requireNonNull(annotatedPath, "annotatedPath cannot be null");
        this.annotations = Objects.requireNonNull(annotations, "annotations cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public Path getAnnotatedPath() { return annotatedPath; }
    public List<Annotation> getAnnotations() { return annotations; }
    public Instant getTimestamp() { return timestamp; }
}

// Use-case interfaces for screengrab workflows
interface CaptureScreenshotUseCase extends UseCase<CaptureScreenshotCommand, CaptureScreenshotResult> {}
interface AnnotateScreenshotUseCase extends UseCase<AnnotateScreenshotCommand, AnnotateScreenshotResult> {}

// Domain-specific exceptions
class ScreengrabException extends Exception {
    public ScreengrabException(String message) { super(message); }
    public ScreengrabException(String message, Throwable cause) { super(message, cause); }
}

class CaptureException extends ScreengrabException {
    public CaptureException(String reason) { super("Screenshot capture failed: " + reason); }
}

class AnnotationException extends ScreengrabException {
    public AnnotationException(String reason) { super("Screenshot annotation failed: " + reason); }
} 