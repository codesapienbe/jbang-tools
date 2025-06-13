# PROMPT.md — LLM & Pair Programming Guide for Sysmon

## Project Context

Sysmon is a cross-platform CLI tool for comprehensive system monitoring with AI-driven predictions. It is part of the JBang-Tools ecosystem, focusing on proactive system health management.

## What This Project Does

- Monitors CPU, memory, disk, and network metrics in real-time
- Uses AI to detect anomalies and predict potential resource issues
- Logs historical data and supports trend analysis

## What This Project Does NOT Do

- Does not provide a GUI dashboard
- Does not require cloud services (offline AI preferred)
- Does not manage application-specific metrics

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate metric collection, prediction logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for exporting system metrics to JSON or CSV."
- "Integrate with jobops to verify system readiness before job execution."
- "Add a feature to send email alerts on predictive failures."
- "Write tests that simulate resource exhaustion and verify alerts."

## How to Help the User

- Ensure metric collection works across OSes
- Suggest additional metrics or thresholds to monitor
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's monitoring requirements

## Gotchas

- Metric APIs differ by OS; document required permissions
- Prediction models need tuning; provide clear config options
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full SIEM platform
- Cloud-based analytics
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on monitoring policies!** 