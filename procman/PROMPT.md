# PROMPT.md — LLM & Pair Programming Guide for Procman

## Project Context

Procman is a cross-platform CLI tool for advanced process management with AI-driven recommendations. It is part of the JBang-Tools ecosystem, focusing on system resource optimization.

## What This Project Does

- Lists, starts, stops, and kills processes
- Provides AI-based suggestions for resource optimization
- Monitors CPU and memory usage per process

## What This Project Does NOT Do

- Does not provide a GUI process manager
- Does not require cloud services (offline AI preferred)
- Does not handle containerized processes specifically

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate process control, monitoring, and AI logic
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OS-specific process APIs or libraries
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for generating process usage reports in JSON."
- "Integrate with sysmon to automatically kill runaway processes."
- "Add a feature to adjust process priorities based on AI score."
- "Write tests that simulate high-CPU processes and verify recommendations."

## How to Help the User

- Ensure process APIs work across OSes
- Suggest key metrics for process health and stability
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's process management workflows

## Gotchas

- Killing processes may require elevated privileges; document permissions
- AI recommendations should be validated; provide safe defaults
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full GUI process monitoring suite
- Cloud-based process management
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on process management policies!** 