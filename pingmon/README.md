# Pingmon

**Network connectivity monitor with predictive issue detection**

## Project Goal

Pingmon provides command-line monitoring of network connectivity by pinging hosts and using predictive analysis to forecast potential network issues before they occur.

## Key Features

- Periodic ping monitoring of hosts
- Predictive failure detection using ML algorithms
- Historical latency and packet loss reporting
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for scripting and automation

## Usage

```sh
jbang pingmon/Pingmon.java --host 8.8.8.8 --interval 5
jbang pingmon/Pingmon.java --predict --days 1
```

## Example Use Cases

- Monitor ISP connectivity and predict outages
- Alert on high latency before user impact
- Integrate with sysmon for unified system and network health monitoring

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for host, interval, and predictive windows

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Pingmon can be used with:
- `netguard` for combined security and connectivity monitoring
- `jobops` for ensuring network stability before job submissions
- `sysmon` for comprehensive system monitoring

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 