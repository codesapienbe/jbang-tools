# Screengrab

**Professional screen capture with AI annotation**

## Project Goal

Screengrab provides command-line tools for capturing screenshots, with optional AI-powered annotation and object detection to highlight key elements.

## Key Features

- Capture full screen, window, or region
- AI-based annotation (bounding boxes, labels)
- Support for multiple image formats
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for automation and integration

## Usage

```sh
jbang screengrab/Screengrab.java --capture full
jbang screengrab/Screengrab.java --annotate
```

## Example Use Cases

- Capture annotated screenshots for tutorials
- Automate periodic screen captures with annotations
- Integrate with clipstack to store annotated images

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for capture mode, annotation, and output path

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Screengrab can be used with:
- `clipstack` for clipboard-based screenshot storage
- `jobops` for capturing annotated job descriptions
- `syncmate` for syncing captured images

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 