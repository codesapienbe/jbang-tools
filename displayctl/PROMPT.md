# PROMPT.md — LLM & Pair Programming Guide for Displayctl

## Project Context

Displayctl is a cross-platform command-line utility for managing display settings, calibration, and adaptive profiles. It is part of the JBang-Tools ecosystem and designed for automation and scripting workflows.

## What This Project Does

- Adjusts brightness, contrast, and color profiles
- Applies adaptive ML-driven display settings
- Supports scripting via CLI flags

## What This Project Does NOT Do

- Does not provide a GUI controls panel
- Does not require cloud services
- Does not manage window layouts (see `winman`)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate sensor data reading, profile management, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) JNA or OS-specific APIs for display control
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom display profiles via JSON config files."
- "Integrate with sysmon to adjust brightness based on battery level."
- "Add a feature to schedule profile changes at specific times."
- "Write tests that simulate brightness changes and verify system behavior."

## How to Help the User

- Ensure display adjustments work on all OSes
- Suggest ways to handle missing ambient sensors gracefully
- Propose integration points with other JBang-Tools
- When in doubt, ask for the target automation scenarios

## Gotchas

- Display control APIs differ by OS; document required permissions clearly
- Some systems may require elevated privileges; handle errors gracefully
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full GUI calibration tool
- Cloud-based display management
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or a sample workflow!** 