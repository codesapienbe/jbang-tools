# PROMPT.md — LLM & Pair Programming Guide for Quickset

## Project Context

Quickset is a cross-platform CLI tool for rapid system settings access with AI-powered contextual suggestions. It is part of the JBang-Tools ecosystem, designed for automation and personalized workflows.

## What This Project Does

- Lists and applies system settings quickly
- Provides AI-based suggestions based on context
- Supports a range of settings: DISPLAY, NETWORK, SOUND, etc.

## What This Project Does NOT Do

- Does not provide a GUI settings panel
- Does not require cloud services (offline AI preferred)
- Does not manage application-specific shortcuts

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate settings retrieval, suggestion logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom settings profiles via JSON files."
- "Integrate with trayorg to change settings from the system tray."
- "Add a feature to suggest settings based on time or location."
- "Write tests that simulate setting changes and verify outcomes."

## How to Help the User

- Ensure settings APIs work across OSes
- Suggest additional contextual triggers and logic
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's environment-switching workflows

## Gotchas

- OS settings APIs vary; document required permissions
- Some changes may require elevated privileges; handle errors gracefully
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full GUI settings manager
- Cloud-based configuration sync
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on context triggers!** 