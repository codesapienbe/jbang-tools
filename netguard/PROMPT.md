# PROMPT.md — LLM & Pair Programming Guide for Netguard

## Project Context

Netguard is a cross-platform CLI network security monitoring tool that uses ML-driven threat detection. It is part of the JBang-Tools ecosystem, designed for security and automation workflows.

## What This Project Does

- Monitors network traffic (inbound/outbound) in real-time
- Uses ML models to detect anomalies and threats
- Generates alerts and detailed logs

## What This Project Does NOT Do

- Does not replace full SIEM platforms
- Does not provide a GUI dashboard
- Does not require cloud services (local ML models preferred)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate data capture, ML inference, and alert logic
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) Local ML libraries or model runtimes
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom ML model selection via config flags."
- "Integrate with sysmon to forward alerts to a central dashboard."
- "Add a feature to summarize network events over time."
- "Write tests that simulate network traffic patterns and verify alerts."

## How to Help the User

- Ensure packet capture works cross-platform
- Suggest ways to reduce false positives in ML detection
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's network monitoring scenarios

## Gotchas

- Packet capture requires elevated privileges; document permissions
- Handling high-throughput networks requires efficient parsing
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full network security dashboard
- Cloud-based model hosting
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on network scopes!** 