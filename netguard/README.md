# Netguard

**Network security monitor with ML threat detection**

## Project Goal

Netguard offers real-time monitoring of network traffic with machine learning-driven threat detection, providing insights and alerts to secure networks against anomalies and attacks.

## Key Features

- Monitor inbound and outbound network traffic
- ML-based threat detection and anomaly alerts
- Detailed logging and reporting
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for integration and automation

## Usage

```sh
jbang netguard/Netguard.java --scan 192.168.1.0/24
jbang netguard/Netguard.java --alert-threshold high
```

## Example Use Cases

- Detect suspicious traffic patterns in corporate networks
- Automate security alerts for critical events
- Integrate with sysmon for unified monitoring dashboards

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for network ranges, models, and thresholds

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Netguard can be used with:
- `sysmon` for system-wide monitoring
- `filewatch` for correlating network and file events
- `jobops` for securing application deployments

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 