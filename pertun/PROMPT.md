# PROMPT.md — LLM & Pair Programming Guide for Pertun

## Project Context

Pertun is a cross-platform CLI tool for performance tuning and OS optimization, providing automated analysis and optimization suggestions for CPU, memory, and disk. It is part of the JBang-Tools ecosystem, designed for performance and automation workflows.

## What This Project Does

- Profiles system performance metrics (CPU, memory, disk)
- Provides automated optimization suggestions
- Offers benchmarking and profiling utilities

## What This Project Does NOT Do

- Does not provide a GUI dashboard
- Does not require cloud services
- Does not handle application-specific tuning (focus on OS-level)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate profiling, optimization, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) OS-specific performance counters or metrics libraries
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for GPU performance analysis."
- "Integrate with jobops to pre-warm system before intensive tasks."
- "Add a feature to compare performance across system snapshots."
- "Write tests that simulate CPU load and verify profiling results."

## How to Help the User

- Ensure metrics collection works across different OSes
- Suggest metrics to monitor for performance regressions
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's performance goals and workloads

## Gotchas

- Metrics APIs differ by OS; document required permissions
- Running optimizations may require elevated privileges; handle errors gracefully
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a GUI performance tuning suite
- Cloud-based metrics storage
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on performance targets and metrics!** 