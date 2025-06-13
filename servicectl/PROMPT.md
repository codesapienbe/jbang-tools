# PROMPT.md — LLM & Pair Programming Guide for Servicectl

## Project Context

Servicectl is a cross-platform CLI tool for system service management with predictive failure detection. It is part of the JBang-Tools ecosystem, focusing on proactive service reliability.

## What This Project Does

- Lists, starts, stops, and enables system services
- Uses ML-based models to predict service failures
- Performs health checks and monitoring

## What This Project Does NOT Do

- Does not provide a GUI service manager
- Does not require cloud services (local ML models preferred)
- Does not handle container orchestration services

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate control logic, prediction models, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) Local ML libraries for prediction
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for scheduling service restarts on failure predictions."
- "Integrate with sysmon to correlate service failures with system metrics."
- "Add a feature to export service health reports in JSON."
- "Write tests that simulate service failure patterns and verify predictions."

## How to Help the User

- Ensure service control commands work across platforms
- Suggest key metrics for service health monitoring
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's service management workflows

## Gotchas

- Service management may require elevated privileges; document required permissions
- Prediction models need tuning; provide clear configuration
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full GUI service dashboard
- Cloud-based service analytics
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on service reliability requirements!** 