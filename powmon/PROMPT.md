# PROMPT.md — LLM & Pair Programming Guide for Powmon

## Project Context

Powmon is a cross-platform CLI tool for battery and power management with learning-driven optimizations. It is part of the JBang-Tools ecosystem, designed for power efficiency and automation workflows.

## What This Project Does

- Monitors real-time battery and power usage
- Provides automated power-saving suggestions based on usage patterns
- Generates historical usage reports and trends

## What This Project Does NOT Do

- Does not provide a GUI power manager
- Does not require cloud services (local analysis preferred)
- Does not handle hardware-specific power adjustments beyond software settings

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate monitoring, optimization logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OS-specific power APIs or system utilities
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom power profiles via config files."
- "Integrate with sysmon to trigger optimization on low battery."
- "Add a feature to schedule power profiles based on time of day."
- "Write tests that simulate power usage patterns and verify suggestions."

## How to Help the User

- Ensure power monitoring works across different OSes
- Suggest metrics to track for power optimization
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's hardware and usage scenarios

## Gotchas

- Power APIs differ by OS; document required permissions and utilities
- Running optimizations may require elevated privileges; handle errors
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full GUI power management suite
- Cloud-based power analytics
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on their power optimization goals!** 