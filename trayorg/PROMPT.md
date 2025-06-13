# PROMPT.md — LLM & Pair Programming Guide for Trayorg

## Project Context

Trayorg is a cross-platform system tray organizer with AI-based importance ranking. It is part of the JBang-Tools ecosystem and is designed to help users manage notifications and quick actions for maximum productivity.

## What This Project Does

- Organizes and prioritizes system tray icons and notifications
- Uses AI to rank importance and suggest actions
- CLI interface for scripting and automation

## What This Project Does NOT Do

- Does not provide a full notification center
- Does not require cloud APIs (offline/local AI preferred)
- Does not provide a GUI (CLI only)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate tray management, AI ranking, and action logic
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) Java AWT/Swing for tray APIs, local AI libraries for ranking
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom notification filters."
- "Integrate with winman to trigger window layouts from the tray."
- "Add a feature to snooze or mute notifications."
- "Write a test that verifies AI ranking of notifications."

## How to Help the User

- Ensure tray management works cross-platform
- Suggest ways to improve AI ranking accuracy
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's workflow or target use case

## Gotchas

- Tray APIs differ between OSes; use Java AWT/Swing for best compatibility
- Some features may require accessibility permissions; document this clearly
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full notification center
- Cloud-based notification management
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or a sample workflow!** 