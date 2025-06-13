# Audioctl

**Advanced audio control with device switching**

## Project Goal

Audioctl provides command-line control over system audio devices and settings, enabling quick device switching, volume adjustments, and routing for power users and automation scripts.

## Key Features

- List and switch audio input/output devices
- Adjust volume levels per device
- Mute/unmute devices programmatically
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for easy scripting and automation

## Usage

```sh
jbang audioctl/Audioctl.java --list
jbang audioctl/Audioctl.java --set-output "Headphones"
```

## Example Use Cases

- Quickly switch between speakers and headphones
- Automate volume adjustments for meetings or media playback
- Integrate with workflow tools to adjust audio on the fly

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for device selection, volume, and mute controls

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Audioctl can be used with:
- `instyper` for selecting input devices before speech transcription
- `inspeak` for routing TTS output to the desired device
- `trayorg` for quick switching via the system tray

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 