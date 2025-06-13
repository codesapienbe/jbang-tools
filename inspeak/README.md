# Inspeak

**Instant text-to-speech with file monitoring**

## Project Goal

Inspeak provides instant, high-quality text-to-speech (TTS) conversion for any text file or clipboard content. It is designed for accessibility, productivity, and automation, enabling users to listen to documents, notifications, or any text content on demand.

## Key Features

- Instant text-to-speech conversion
- Monitors files or clipboard for new text
- High-quality, natural-sounding voices
- Cross-platform (Windows, macOS, Linux)
- CLI interface for scripting and automation
- Integrates with other JBang-Tools for speech workflows

## Usage

```sh
jbang inspeak/Inspeak.java --file notes.txt
```

## Example Use Cases

- Listen to documents or notes while multitasking
- Accessibility for users with visual impairments
- Automated reading of notifications or logs
- Integration with translation (offlan) or speech-to-text (instyper)

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for input file, clipboard, voice selection, etc.

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Inspeak can be used with:
- `offlan` for offline translation and then speech
- `instyper` for speech-to-speech workflows
- `clipstack` for reading clipboard contents aloud

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 