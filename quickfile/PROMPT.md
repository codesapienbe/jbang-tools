# PROMPT.md — LLM & Pair Programming Guide for Quickfile

## Project Context

Quickfile is a cross-platform CLI tool for lightning-fast file searching using learning-based indexing. It is part of the JBang-Tools ecosystem and designed for productivity and automation workflows.

## What This Project Does

- Builds real-time file system indexes
- Uses learning algorithms to rank search results
- Supports both fuzzy and exact search modes

## What This Project Does NOT Do

- Does not provide a GUI file browser
- Does not require cloud services (all local)
- Does not manage file content indexing beyond metadata

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate indexing, search logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for indexing file contents for full-text search."
- "Integrate with sysmon to trigger index updates on file changes."
- "Add a feature to limit search to certain file extensions."
- "Write tests that simulate large directory indexing and verify search performance."

## How to Help the User

- Ensure indexing handles large directories efficiently
- Suggest caching strategies for recurring searches
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's search and indexing workflows

## Gotchas

- File system events may be missed; document limitations
- Index size can grow large; provide cleanup options
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a GUI file search tool
- Cloud-based search services
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on search indexing policies!** 