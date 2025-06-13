# PROMPT.md — LLM & Pair Programming Guide for Winman

## Project Context

Winman is a cross-platform window management and automation tool for power users. It is part of the JBang-Tools ecosystem and is designed to boost productivity through advanced window tiling, snapping, and workspace organization.

## What This Project Does

- Provides window tiling, snapping, and workspace management
- Supports customizable keyboard shortcuts
- CLI interface for scripting and automation

## What This Project Does NOT Do

- Does not provide a full desktop environment
- Does not require cloud APIs
- Does not provide a GUI (CLI only)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate window management, hotkey handling, and automation logic
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) JNA or JavaFX for window management APIs
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom window layouts via a config file."
- "Integrate with trayorg to trigger window layouts from the system tray."
- "Add a feature to save and restore workspace states."
- "Write a test that verifies window snapping on multiple monitors."

## How to Help the User

- Ensure window management works cross-platform
- Suggest ways to improve tiling/snap performance
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's workflow or target use case

## Gotchas

- Window APIs differ between OSes; use JNA or JavaFX for best compatibility
- Some features may require accessibility permissions; document this clearly
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full desktop environment
- Cloud-based window management
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or a sample workflow!** 