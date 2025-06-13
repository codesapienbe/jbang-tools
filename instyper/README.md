# Instyper

**Ultra-low latency speech-to-text conversion**

## Project Goal

Instyper aims to provide real-time, ultra-low latency speech-to-text conversion for desktop users. It is designed to be cross-platform, lightweight, and easy to use, making it ideal for live transcription, accessibility, and productivity workflows.

## Key Features

- Real-time speech-to-text conversion
- Ultra-low latency (sub-second)
- Cross-platform (Windows, macOS, Linux)
- Simple, scriptable interface (via JBang)
- Designed for integration with other tools in the JBang-Tools ecosystem

## Usage

```sh
jbang instyper/Instyper.java
```

## Example Use Cases

- Live captioning for meetings or streams
- Accessibility for users with hearing impairments
- Quick voice notes and dictation
- Integration with clipboard managers or automation tools

## Configuration

- No external dependencies required (runs with JBang)
- Optional: configure input device or language via command-line flags (see code for details)

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Instyper can be used alongside other JBang-Tools, such as:

- `clipstack` for managing transcribed text
- `jobops` for AI-powered document processing
- `offlan` for translating transcribed speech

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools.
