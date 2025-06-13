# PROMPT.md — LLM & Pair Programming Guide for Safeclip

## Project Context

Safeclip is a cross-platform CLI tool for AI-powered clipboard security scanning. It is part of the JBang-Tools ecosystem and designed to detect sensitive or malicious data in clipboard workflows.

## What This Project Does

- Scans clipboard content (text and images) for sensitive data patterns
- Uses AI to classify potential security risks
- Provides real-time alerts for new clipboard entries

## What This Project Does NOT Do

- Does not provide a GUI interface
- Does not require cloud services (offline AI preferred)
- Does not rewrite clipboard content; only scans

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate clipboard monitoring, scanning logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) AI model libraries for classification
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom data pattern rules via YAML config."
- "Integrate with jobops to sanitize clipboard URLs before processing."
- "Add a feature to whitelist specific data types or domains."
- "Write tests that simulate sensitive data detection scenarios."

## How to Help the User

- Ensure clipboard APIs work across OSes
- Suggest patterns to improve classification accuracy
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's security requirements

## Gotchas

- Clipboard access may need permissions; document requirements
- AI models need tuning; document default model behaviors
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a GUI clipboard security suite
- Cloud-based scanning services
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on security policies!** 