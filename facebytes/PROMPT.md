# PROMPT.md — LLM & Pair Programming Guide for Facebytes

## Project Context

Facebytes is a cross-platform CLI tool for managing and anonymizing event photos. It is part of the JBang-Tools ecosystem, focusing on privacy-aware image processing workflows.

## What This Project Does

- Detects and blurs faces automatically
- Tags images by person, date, or event
- Batch processes directories of photos

## What This Project Does NOT Do

- Does not provide a GUI gallery
- Does not perform general image recognition beyond face detection
- Does not require cloud services (offline processing preferred)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate image I/O, face detection, and processing logic
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OpenCV or JavaCV for face detection
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for replacing blurred regions with avatars."
- "Integrate with clipstack to copy blurred images to clipboard automatically."
- "Add a feature to generate PDF albums of tagged photos."
- "Write tests that simulate face detection on sample images."

## How to Help the User

- Ensure face detection works across varied lighting conditions
- Suggest optimizations for processing large image batches
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's typical photo formats and workflows

## Gotchas

- Face detection models may vary in accuracy; document model requirements
- Large image files can consume memory; handle out-of-memory gracefully
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full-featured photo management GUI
- Cloud-based image processing
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or sample images!** 