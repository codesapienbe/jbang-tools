# PROMPT.md — LLM & Pair Programming Guide for Qproj

## Project Context

Qproj is a cross-platform CLI tool for generating projects from a large library of 100,000+ templates. It is part of the JBang-Tools ecosystem, designed for rapid project scaffolding and standardization.

## What This Project Does

- Searches a vast template library by keywords
- Generates project skeletons with custom parameters
- Supports multiple languages, frameworks, and protocols

## What This Project Does NOT Do

- Does not provide a GUI template browser
- Does not include cloud-based templates (local library)
- Does not handle project dependencies installation

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate template search, generation, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for filtering templates by license type."
- "Integrate with syncmate to clone generated projects immediately."
- "Add a feature to preview template contents before generation."
- "Write tests that simulate project generation and verify structure."

## How to Help the User

- Ensure search and generation performance scales with template library
- Suggest metadata to enhance template discovery
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's project scaffolding workflows

## Gotchas

- Template rendering errors should be caught and reported clearly
- Ensure file system permissions are handled correctly
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a GUI template browser
- Cloud-based template hosting
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on template requirements or preferences!** 