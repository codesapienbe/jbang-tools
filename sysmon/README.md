# Sysmon

**Comprehensive system monitoring with AI predictions**

## Project Goal

Sysmon provides real-time, comprehensive monitoring of system resources and AI-driven predictions to proactively identify and alert on potential issues, ensuring system health and reliability.

## Key Features

- Monitor CPU, memory, disk, and network metrics
- AI-based anomaly detection and predictive alerts
- Historical data logging and trend analysis
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for scripting and automation

## Usage

```sh
jbang sysmon/Sysmon.java --monitor
jbang sysmon/Sysmon.java --predict --interval 60
```

## Example Use Cases

- Track system load to prevent performance degradation
- Predict impending resource exhaustion based on trends
- Integrate with `filewatch` and `netguard` for unified alerts

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for metrics, thresholds, and intervals

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Sysmon can be used with:
- `netguard` for security and resource correlation
- `filewatch` for file and system event correlation
- `powmon` for power and battery monitoring alongside system metrics

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 