# Pertun

**Performance tuning and OS optimization**

## Project Goal

Pertun provides command-line tools for performance tuning and OS optimization, enabling users to analyze system metrics and apply optimizations for CPU, memory, and disk performance.

## Key Features

- Analyze CPU, memory, and disk performance metrics
- Automated optimization suggestions
- Benchmarking and profiling utilities
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for scripting and automation

## Usage

```sh
jbang pertun/Pertun.java --analyze
jbang pertun/Pertun.java --optimize --target cpu
```

## Example Use Cases

- Profile system performance to identify bottlenecks
- Automatically tune system parameters for specific workloads
- Integrate into CI pipelines for performance regression tests

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for metrics, targets, and thresholds

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Pertun can be used with:
- `sysmon` for continuous performance monitoring
- `procman` for managing optimized processes
- `jobops` for ensuring performance during AI tasks

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 