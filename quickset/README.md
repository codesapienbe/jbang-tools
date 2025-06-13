# Quickset

**Rapid system settings access with contextual suggestions**

## Project Goal

Quickset provides a CLI utility to quickly access and modify system settings, offering contextual suggestions based on active applications, time of day, or system state.

## Key Features

- Fast retrieval and application of system settings
- Contextual suggestions using AI models
- Support for system preferences, network, and display settings
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for automation and scripting

## Usage

```sh
jbang quickset/Quickset.java --list
jbang quickset/Quickset.java --apply "dark_mode"
```

## Example Use Cases

- Automatically switch to do-not-disturb during meetings
- Apply power-saving settings when on battery
- Integrate with sysmon to adjust settings based on resource usage

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for listing, applying, and suggesting settings

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Quickset can be used with:
- `sysmon` for resource-based setting adjustments
- `winman` for workspace-specific settings
- `jobops` for preparing environment before jobs

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 