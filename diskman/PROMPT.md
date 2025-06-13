# PROMPT.md — LLM & Pair Programming Guide for Diskman

## Project Context

Diskman is a cross-platform CLI tool for disk analysis and safe cleanup. It is part of the JBang-Tools ecosystem and designed for monitoring and automation workflows.

## What This Project Does

- Analyzes disk usage and identifies large files
- Detects duplicate files and suggests cleanup
- Provides preview and undo for cleanup actions

## What This Project Does NOT Do

- Does not manage file backups (see `syncmate`)
- Does not require cloud services
- Does not provide a GUI interface

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate analysis, cleanup logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for targeted cleanup by file extension."
- "Integrate with sysmon to trigger cleanup on low disk space alerts."
- "Add a feature to schedule automatic cleanup jobs."
- "Write tests that simulate disk analysis on a sample directory."

## How to Help the User

- Ensure disk operations are safe and reversible
- Suggest optimizations for scanning large filesystems
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's typical cleanup scenarios

## Gotchas

- Handle permissions and read-only files gracefully
- Avoid deleting system-critical files; provide safe defaults
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a GUI disk management tool
- Cloud-based storage management
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on cleanup policies!** 