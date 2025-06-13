# PROMPT.md — LLM & Pair Programming Guide for Screengrab

## Project Context

Screengrab is a cross-platform CLI tool for professional screen capturing with AI-powered annotation. It is part of the JBang-Tools ecosystem, designed for documentation and automation workflows.

## What This Project Does

- Captures full screen, active window, or region
- Applies AI-based annotations (bounding boxes, labels)
- Saves screenshots in various formats

## What This Project Does NOT Do

- Does not provide a GUI capture utility
- Does not require cloud services (offline AI models)
- Does not record video or live streams

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate capture logic, annotation engines, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OpenCV or JavaCV for object detection
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for blurring sensitive regions automatically."
- "Integrate with safeclip to scan screenshots for sensitive data."
- "Add a feature to upload screenshots to a remote server."
- "Write tests that simulate capture and annotation on sample images."

## How to Help the User

- Ensure capture APIs work across OSes
- Suggest annotation models for common objects
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's documentation or automation needs

## Gotchas

- Screen capture may require elevated permissions; document clearly
- Annotation models can be resource-intensive; manage performance
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full-featured GUI capture suite
- Cloud-based annotation services
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on annotation requirements!** 