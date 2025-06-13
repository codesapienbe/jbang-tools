# Newlook

**Real-time appearance modification with computer vision**

## Project Goal

Newlook provides command-line tools for real-time modifications of live camera feeds, enabling effects like background replacement, filters, and AR overlays using computer vision models.

## Key Features

- Real-time video feed processing
- Background replacement and filters
- AR overlays (e.g., masks, hats)
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for scripting and integration

## Usage

```sh
jbang newlook/Newlook.java --background "beach.jpg"
jbang newlook/Newlook.java --filter grayscale
```

## Example Use Cases

- Enhance live streaming with virtual backgrounds
- Apply fun filters for video calls
- Integrate into automated content creation workflows

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for effects, models, and performance settings

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Newlook can be used with:
- `visiplay` for interactive gaming overlays
- `facebytes` for face-based effect tagging
- `clipstack` for capturing and sharing snapshots

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 