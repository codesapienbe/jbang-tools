# PROMPT.md — LLM & Pair Programming Guide for Netwatch

## Project Context

Netwatch is a cross-platform CLI tool for monitoring network traffic with built-in threat detection. It is part of the JBang-Tools ecosystem and designed for security and automation workflows.

## What This Project Does

- Monitors network interfaces for traffic patterns
- Detects anomalies and suspicious activity
- Logs events and generates alerts

## What This Project Does NOT Do

- Does not provide a GUI dashboard
- Does not require cloud services
- Does not replace full SIEM platforms

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate capture logic, detection algorithms, and alerting
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for packet-level inspection and filtering."
- "Integrate with sysmon to forward alerts to a webhook."
- "Add a feature to visualize traffic statistics over time."
- "Write tests that simulate traffic anomalies and verify detection."

## How to Help the User

- Ensure packet reading works on all OSes
- Suggest ways to fine-tune detection thresholds
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's network environments and requirements

## Gotchas

- Raw packet capture may require elevated privileges; document permissions
- Handling high-throughput traffic requires efficient parsing
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a network visualization UI
- Cloud-based event storage
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on network scopes and traffic volumes!** 