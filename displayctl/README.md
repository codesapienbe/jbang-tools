# Displayctl

**Advanced display control with adaptive ML settings**

## Project Goal

Displayctl provides command-line management of display settings, including brightness, color calibration, and adaptive ML-driven profiles based on ambient light and time of day.

## Key Features

- Adjust brightness and contrast
- Apply and manage color profiles
- Adaptive settings using ML and ambient sensor data
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for scripting and automation

## Usage

```sh
jbang displayctl/Displayctl.java --brightness 80
jbang displayctl/Displayctl.java --apply-profile "Movie"
```

## Example Use Cases

- Automatically dim display in low-light environments
- Switch color profiles for gaming, reading, or movie watching
- Integrate with sysmon to adjust display based on power status

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for brightness, profiles, and adaptive mode

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Displayctl can be used with:
- `sysmon` for adjusting display based on battery or temperature
- `winman` for workspace-specific display profiles
- `quickset` for loading environment-specific display settings

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 