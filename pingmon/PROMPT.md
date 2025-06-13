# PROMPT.md — LLM & Pair Programming Guide for Pingmon

## Project Context

Pingmon is a cross-platform CLI tool for network connectivity monitoring with predictive issue detection. It is part of the JBang-Tools ecosystem, designed to proactively detect network problems.

## What This Project Does

- Pings hosts at configurable intervals
- Uses predictive analysis to forecast connectivity issues
- Logs historical latency and packet loss data

## What This Project Does NOT Do

- Does not provide a GUI dashboard
- Does not require cloud services (local analysis preferred)
- Does not handle system-wide performance monitoring (see `sysmon`)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate ping logic, predictive analysis, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for multi-host ping batches with aggregated reporting."
- "Integrate with sysmon to trigger alerts on critical anomalies."
- "Add a feature to export historical data to CSV."
- "Write tests that simulate packet loss scenarios and verify detection."

## How to Help the User

- Ensure ping commands work with various network configurations
- Suggest ways to improve predictive model accuracy
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's monitoring requirements

## Gotchas

- ICMP may require elevated privileges; document permissions
- Predictive models need tuning; document default thresholds
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a network visualization UI
- Cloud-based analytics
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on network environments!** 