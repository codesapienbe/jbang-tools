# PROMPT.md — LLM & Pair Programming Guide for Filewatch

## Project Context

Filewatch is a cross-platform CLI tool for real-time file system monitoring with anomaly detection. It is part of the JBang-Tools ecosystem and designed for security, operational, and automation workflows.

## What This Project Does

- Monitors directories for file creation, modification, and deletion
- Detects anomalies based on thresholds or patterns
- Triggers alerts or custom actions via CLI hooks

## What This Project Does NOT Do

- Does not provide a GUI dashboard
- Does not require cloud services
- Does not replace full-featured SIEM tools

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate watch logic, anomaly detection, and action execution
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for pattern-based anomaly rules (e.g., regex on filenames)."
- "Integrate with sysmon to send alerts via email or Slack."
- "Add a feature to aggregate and summarize events over time."
- "Write tests that simulate file events and verify anomaly detection."

## How to Help the User

- Ensure file watching works on all OSes
- Suggest optimizations for handling large directories
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's monitoring use cases

## Gotchas

- File system APIs differ by OS; handle edge cases gracefully
- Avoid missing rapid sequences of events; document limitations
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full SIEM dashboard
- Cloud-based event storage
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on event handling policies!** 