# Visiplay

**Touchless gaming platform using hand-tracking**

## Project Goal

Visiplay provides a hands-free gaming experience using computer vision-based hand-tracking, enabling gesture-based control for interactive gameplay without physical controllers.

## Key Features

- Real-time hand-tracking for input
- Gesture recognition for controls
- Support for multiple game genres
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for configuring gestures and profiles

## Usage

```sh
jbang visiplay/Visiplay.java --calibrate
jbang visiplay/Visiplay.java --profile gaming
```

## Example Use Cases

- Play games using hand gestures without controllers
- Integrate with VR or AR setups for immersive experiences
- Automate hand-tracking calibration in batch mode

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for calibration, profiles, and sensitivity

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Visiplay can be used with:
- `newlook` for AR overlays in gaming
- `screengrab` for capturing gameplay with annotations
- `syncmate` for syncing gesture profiles across machines

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 