# Filewatch

**Real-time file system monitoring with anomaly detection**

## Project Goal

Filewatch provides a command-line utility to monitor file system changes in real time, detect anomalies, and trigger alerts or automation scripts for security and operational workflows.

## Key Features

- Watch directories for file creation, modification, and deletion
- Detect anomalous patterns or unexpected changes
- Trigger custom actions or notifications
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for integration with scripts and automation tools

## Usage

```sh
jbang filewatch/Filewatch.java --watch /path/to/dir
jbang filewatch/Filewatch.java --anomaly-threshold 10%
```

## Example Use Cases

- Monitor configuration files for unauthorized changes
- Track log directories for unexpected file growth
- Automate backup or alert processes on file updates

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for directories, thresholds, and actions

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Filewatch can be used with:
- `sysmon` for system monitoring and alerting
- `diskman` for cleaning up logs or temp files
- `jobops` for monitoring application directories

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 