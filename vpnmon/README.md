# Vpnmon

**VPN connection monitor with intelligent server selection**

## Project Goal

Vpnmon provides CLI utilities to monitor VPN connections, measure performance, and automatically select the optimal server using intelligent analysis.

## Key Features

- Monitor VPN connection status and throughput
- Auto-select best VPN server based on latency and load
- Historical connection performance logging
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for integration and automation

## Usage

```sh
jbang vpnmon/Vpnmon.java --status
jbang vpnmon/Vpnmon.java --select --criteria latency
```

## Example Use Cases

- Ensure consistent VPN performance during remote work
- Automate server selection for best speeds
- Integrate with sysmon for network health monitoring

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for status checks, selection criteria, and logging

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Vpnmon can be used with:

- `netwatch` for VPN traffic monitoring
- `pingmon` for comparing server response times
- `jobops` for ensuring secure connections before job submissions

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools.
