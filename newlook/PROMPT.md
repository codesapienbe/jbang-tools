# PROMPT.md — LLM & Pair Programming Guide for Newlook

## Project Context

Newlook is a cross-platform CLI tool for real-time video feed modification using computer vision. It is part of the JBang-Tools ecosystem and designed for streaming, conferencing, and content creation workflows.

## What This Project Does

- Processes live camera feed with effects and overlays
- Replaces backgrounds and applies filters in real-time
- Supports AR overlays like masks and animations

## What This Project Does NOT Do

- Does not record or save videos (focus on live feed)
- Does not rely on cloud services (offline CV models preferred)
- Does not provide a GUI interface

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate video capture, effect pipelines, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OpenCV or JavaCV for video processing
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom shader-based filters." 
- "Integrate with discord bot to stream modified feed." 
- "Add a feature to capture and save snapshots on keypress." 
- "Write tests that simulate effect pipeline on sample video." 

## How to Help the User

- Ensure real-time performance across platforms
- Suggest ways to optimize video processing pipelines
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's streaming or conferencing setups

## Gotchas

- Camera APIs vary by OS; document required permissions
- Video processing can be CPU/GPU intensive; test performance
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full-fledged streaming application
- Cloud-based video effects
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on performance requirements!** 