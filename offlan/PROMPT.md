# PROMPT.md — LLM & Pair Programming Guide for Offlan

## Project Context

Offlan is a fully offline, cross-platform text translation tool supporting 90+ languages. It is part of the JBang-Tools ecosystem and is designed for privacy, speed, and reliability in any environment.

## What This Project Does

- Translates text between supported languages
- Works 100% offline (no network calls)
- CLI interface for scripting, automation, and integration
- Can be used as a backend for speech or file translation

## What This Project Does NOT Do

- Does not require or use cloud APIs
- Does not provide a GUI (CLI only)
- Does not perform speech recognition or synthesis (see `instyper` and `inspeak`)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate translation engine, CLI parsing, and I/O
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OpenNMT, MarianNMT, or other Java-accessible translation engines
- Language models should be bundled or downloaded once and cached

## Example LLM Prompts

- "Add support for batch translation of multiple files."
- "Integrate with instyper to provide speech-to-speech translation."
- "Add a feature to auto-detect the source language."
- "Write a test that verifies translation accuracy for a set of sample phrases."

## How to Help the User

- Ensure all translation is performed offline
- Suggest ways to reduce model size or memory usage
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's workflow or target use case

## Gotchas

- Language models can be large; document download and storage clearly
- Some translation engines may require specific model formats
- Keep CLI interface simple and scriptable

## Out of Scope

- Cloud-based translation
- Building a full GUI
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or a sample workflow!** 