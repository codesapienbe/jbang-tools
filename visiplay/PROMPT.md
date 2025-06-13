# PROMPT.md — LLM & Pair Programming Guide for Visiplay

## Project Context

Visiplay is a cross-platform CLI tool for touchless gaming with hand-tracking. It is part of the JBang-Tools ecosystem, designed for gesture-based interactive experiences.

## What This Project Does

- Tracks hand movements to generate input events
- Recognizes predefined gestures for game controls
- Supports live calibration and profile management

## What This Project Does NOT Do

- Does not provide a GUI controller
- Does not rely on cloud services (offline CV preferred)
- Does not handle VR rendering (focus on input)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate camera input, tracking logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OpenCV or JavaCV for computer vision
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom gesture definitions via config files."
- "Integrate with quickset to bind gestures to system settings."
- "Add a feature to log gesture events for analytics."
- "Write tests that simulate hand movement sequences and verify tracking." 

## How to Help the User

- Ensure tracking works in varied lighting conditions
- Suggest gesture sets for common game genres
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's gaming environment and hardware

## Gotchas

- Camera APIs vary by OS; document required permissions
- Real-time tracking can be CPU intensive; manage performance
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full VR input system
- Cloud-based gesture analysis
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on gesture requirements!** 