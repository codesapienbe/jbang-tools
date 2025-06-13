# Voicebytes

**Audio analysis with real-time speaker isolation**

## Project Goal

Voicebytes provides CLI tools for analyzing audio streams and isolating individual speakers in real-time, enabling noise reduction, diarization, and speaker-specific processing for podcasts, meetings, and transcription workflows.

## Key Features

- Real-time speaker diarization and isolation
- Noise reduction and audio enhancement
- Support for live streams and audio files
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for integration and automation

## Usage

```sh
jbang voicebytes/Voicebytes.java --input mic
jbang voicebytes/Voicebytes.java --file recording.wav
```

## Example Use Cases

- Separate speakers in meeting recordings
- Enhance audio quality for podcasts
- Automate speaker-based notifications or transcripts

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for input source, output format, and model settings

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Voicebytes can be used with:
- `instyper` for feeding isolated streams into STT
- `offlan` for translating speaker segments
- `inspeak` for replaying speaker-specific audio

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 