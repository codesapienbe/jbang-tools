# PROMPT.md — LLM & Pair Programming Guide for Feedme

## Project Context

Feedme is a cross-platform CLI tool for AI-powered food inspection and nutritional analysis. It is part of the JBang-Tools ecosystem and designed for health, safety, and automation workflows.

## What This Project Does

- Analyzes food images for ingredient detection
- Estimates nutritional values and calories
- Provides allergy and spoilage warnings

## What This Project Does NOT Do

- Does not provide a GUI interface
- Does not require cloud services (offline AI models preferred)
- Does not track historical diet data (focus on single analyses)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate image analysis, nutrition modeling, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) TensorFlow Java, ONNX Runtime, or other AI frameworks for model inference
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for exporting results in CSV or JSON formats."
- "Integrate with inspeak to read out nutritional summaries aloud."
- "Add a feature to detect allergens like nuts or gluten."
- "Write tests that simulate image analysis on sample food photos."

## How to Help the User

- Ensure AI models load and run offline reliably
- Suggest ways to improve ingredient detection accuracy
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's typical data formats and workflows

## Gotchas

- Model sizes can be large; document download and caching strategies
- Image preprocessing may vary; handle different formats gracefully
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a nutrition tracking database
- Cloud-based food analysis
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or sample images!** 