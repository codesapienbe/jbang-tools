# PROMPT.md — LLM & Pair Programming Guide for Lazyme

## Project Context

Lazyme is a cross-platform, face recognition authentication tool. It is part of the JBang-Tools ecosystem and is designed for privacy, convenience, and secure automation workflows.

## What This Project Does

- Authenticates users via face recognition
- Provides passwordless login and access control
- CLI interface for scripting and automation

## What This Project Does NOT Do

- Does not perform general image recognition
- Does not provide a GUI (CLI only)
- Does not require cloud APIs (offline/local face recognition preferred)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate face recognition engine, camera input, and authentication logic
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OpenCV, JavaCV, or other Java-accessible face recognition libraries
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for multiple user profiles and face enrollment."
- "Integrate with vaultray for unlocking password vaults."
- "Add a feature to log authentication attempts."
- "Write a test that verifies authentication with a sample image."

## How to Help the User

- Ensure face recognition works offline and is cross-platform
- Suggest ways to improve recognition accuracy or speed
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's workflow or target use case

## Gotchas

- Camera APIs and image formats may vary by OS
- Some engines may require model downloads; document this clearly
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full GUI
- Cloud-based face recognition (unless specifically requested)
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or a sample workflow!** 