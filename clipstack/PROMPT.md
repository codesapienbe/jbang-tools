# PROMPT.md — LLM & Pair Programming Guide for Clipstack

## Project Context

Clipstack is a cross-platform CLI clipboard manager that records history, categorizes entries using AI, and provides powerful search and retrieval. It is part of the JBang-Tools ecosystem, designed to boost productivity and automation.

## What This Project Does

- Records clipboard history with unlimited entries
- Uses AI to categorize and tag clipboard snippets
- Provides search and filter capabilities
- Persists history across sessions

## What This Project Does NOT Do

- Does not provide a GUI interface
- Does not require cloud services (offline AI preferred)
- Does not monitor clipboard in secure environments (clipboard only)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate history storage, AI categorization, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) Local AI model libraries for categorization
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for exporting clipboard history to a JSON file."
- "Integrate with jobops to automatically save job URLs from clipboard."
- "Add a feature to blacklist certain clipboard entries."
- "Write tests that simulate clipboard events and verify history logging."

## How to Help the User

- Ensure clipboard monitoring works cross-platform
- Suggest ways to improve AI categorization accuracy
- Propose integration points with other JBang-Tools
- When in doubt, ask for use cases or workflows

## Gotchas

- Clipboard APIs vary by OS; handle exceptions gracefully
- Sensitive data may be copied; document privacy considerations
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a GUI clipboard manager
- Cloud-based clipboard synchronization
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on their clipboard workflows!** 