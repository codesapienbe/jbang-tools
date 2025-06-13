# PROMPT.md — LLM & Pair Programming Guide for Syncmate

## Project Context

Syncmate is a cross-platform CLI tool for intelligent file synchronization with AI-driven conflict resolution. It is part of the JBang-Tools ecosystem and designed for backup and multi-location workflows.

## What This Project Does

- Synchronizes files and directories one-way or two-way
- Uses AI to detect and resolve merge conflicts
- Supports local and remote destinations (e.g., network shares)

## What This Project Does NOT Do

- Does not provide a GUI sync manager
- Does not require cloud services (local/remote file systems only)
- Does not handle database or non-file synchronization

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate sync engine, conflict resolution, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for syncing file metadata (permissions, timestamps)."
- "Integrate with jobops to automatically backup generated files."
- "Add a feature to exclude files by pattern during sync."
- "Write tests that simulate file conflicts and verify resolution."

## How to Help the User

- Ensure sync logic handles large file sets efficiently
- Suggest conflict resolution strategies for specific use cases
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's synchronization workflows

## Gotchas

- Network errors or permissions can disrupt sync; provide retries
- Conflict resolution decisions should be logged; allow user overrides
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a GUI sync dashboard
- Cloud-based sync services
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on conflict resolution policies!** 