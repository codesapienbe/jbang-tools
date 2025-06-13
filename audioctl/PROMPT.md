# PROMPT.md — LLM & Pair Programming Guide for Audioctl

## Project Context

Audioctl is a cross-platform command-line tool for managing system audio devices and settings. It belongs to the JBang-Tools ecosystem, designed to integrate with other utilities for seamless audio workflows.

## What This Project Does

- Lists and switches audio input/output devices
- Adjusts volume and mute settings
- Provides scripting hooks for automation

## What This Project Does NOT Do

- Does not provide a GUI (CLI only)
- Does not require cloud services
- Does not process audio streams (just device control)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate device discovery, control logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) Java Sound API or JNA for lower-level control
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for group-based volume control (e.g., all communication apps)."
- "Integrate with trayorg to provide device switch menu in the system tray."
- "Add a feature to save and restore audio profiles."
- "Write tests that simulate device switching calls and verify outcomes."

## How to Help the User

- Ensure device enumeration works on all OSes
- Suggest ways to improve detection of virtual/special audio devices
- Propose integration points with other JBang-Tools
- When in doubt, ask for the target automation scenarios

## Gotchas

- Audio device APIs differ by OS; use Java Sound API or JNA accordingly
- Some devices may have duplicate names; handle duplicates gracefully
- Keep CLI interface simple and scriptable

## Out of Scope

- Managing audio streams or effects
- Building a full GUI mixer
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or a sample workflow!** 