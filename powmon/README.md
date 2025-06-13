# Powmon

**Battery and power management with learning optimization**

## Project Goal

Powmon provides command-line tools for monitoring battery and power usage, applying learning-driven optimizations to extend battery life and manage power consumption across platforms.

## Key Features

- Real-time battery and power usage monitoring
- Automated power-saving optimization suggestions
- Historical usage reporting and trends
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for integration and automation

## Usage

```sh
jbang powmon/Powmon.java --monitor
jbang powmon/Powmon.java --optimize
```

## Example Use Cases

- Extend laptop battery life during travel
- Automate power profile changes for different workloads
- Integrate with sysmon for power usage alerts

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for monitoring interval and optimization levels

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Powmon can be used with:
- `sysmon` for unified system health monitoring
- `diskman` for adjusting cleanup schedules based on power status
- `jobops` for optimizing power usage during job processing

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 