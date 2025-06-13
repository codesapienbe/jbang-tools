# PROMPT.md — LLM & Pair Programming Guide for Vaultray

## Project Context

Vaultray is a cross-platform CLI password manager with system tray integration. It is part of the JBang-Tools ecosystem and designed for secure, quick access to credentials.

## What This Project Does

- Stores and retrieves passwords securely
- Provides quick access via system tray icon
- Clears clipboard automatically after copying

## What This Project Does NOT Do

- Does not provide a full GUI vault manager
- Does not require cloud services (local vault only)
- Does not automate login workflows beyond clipboard copy

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate vault storage, tray integration, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for encrypted vault export and import."
- "Integrate with safeclip to scan clipboard for password leaks."
- "Add a feature to generate strong passwords on the fly."
- "Write tests that verify vault encryption and clipboard clearing."

## How to Help the User

- Ensure vault works across OSes
- Suggest best practices for secure key storage
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's security requirements

## Gotchas

- Clipboard operations may require permissions; document clearly
- Vault encryption algorithms should follow best practices
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full-featured GUI password manager
- Cloud-based vault sync (unless explicitly requested)
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on security policies!** 