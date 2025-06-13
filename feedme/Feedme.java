package feedme;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.time.Instant;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Feedme {
    public static void main(String[] args) {
        System.out.println("Feedme app placeholder");
    }
}

// Generic asynchronous use-case interface
interface UseCase<I, O> {
    CompletableFuture<O> execute(I input);
}

// Command and result abstractions for food inspection
final class InspectFoodCommand {
    private final Path imagePath;

    public InspectFoodCommand(Path imagePath) {
        this.imagePath = Objects.requireNonNull(imagePath, "imagePath cannot be null");
    }

    public Path getImagePath() { return imagePath; }
}

final class InspectFoodResult {
    private final List<String> ingredients;
    private final List<String> warnings;
    private final Instant timestamp;

    public InspectFoodResult(List<String> ingredients, List<String> warnings, Instant timestamp) {
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients cannot be null");
        this.warnings = Objects.requireNonNull(warnings, "warnings cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public List<String> getIngredients() { return ingredients; }
    public List<String> getWarnings() { return warnings; }
    public Instant getTimestamp() { return timestamp; }
}

// Command and result abstractions for nutrition analysis
final class NutritionAnalysisCommand {
    private final String description;

    public NutritionAnalysisCommand(String description) {
        this.description = Objects.requireNonNull(description, "description cannot be null");
    }

    public String getDescription() { return description; }
}

final class NutritionAnalysisResult {
    private final double calories;
    private final Map<String, Double> macros;
    private final List<String> allergyWarnings;
    private final Instant timestamp;

    public NutritionAnalysisResult(double calories, Map<String, Double> macros, List<String> allergyWarnings, Instant timestamp) {
        this.calories = calories;
        this.macros = Objects.requireNonNull(macros, "macros cannot be null");
        this.allergyWarnings = Objects.requireNonNull(allergyWarnings, "allergyWarnings cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public double getCalories() { return calories; }
    public Map<String, Double> getMacros() { return macros; }
    public List<String> getAllergyWarnings() { return allergyWarnings; }
    public Instant getTimestamp() { return timestamp; }
}

// Use-case interfaces for feedme
interface InspectFoodUseCase extends UseCase<InspectFoodCommand, InspectFoodResult> {}
interface NutritionAnalysisUseCase extends UseCase<NutritionAnalysisCommand, NutritionAnalysisResult> {}

// Domain-specific exceptions
class FeedmeException extends Exception {
    public FeedmeException(String message) { super(message); }
    public FeedmeException(String message, Throwable cause) { super(message, cause); }
}

class FoodInspectionException extends FeedmeException {
    public FoodInspectionException(Path imagePath) {
        super("Failed to inspect food image: " + imagePath);
    }
}

class NutritionAnalysisException extends FeedmeException {
    public NutritionAnalysisException(String description) {
        super("Failed to analyze nutrition for: " + description);
    }
} 