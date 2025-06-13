# PROMPT.md — LLM & Pair Programming Guide for Voicebytes

## Project Context

Voicebytes is a cross-platform CLI tool for real-time audio analysis and speaker isolation. It is part of the JBang-Tools ecosystem, designed for meeting transcription, podcasting, and audio processing workflows.

## What This Project Does

- Performs real-time speaker diarization and isolation
- Applies noise reduction and audio enhancement
- Processes both live streams (mic) and audio files

## What This Project Does NOT Do

- Does not provide a GUI audio editor
- Does not require cloud services (offline models preferred)
- Does not handle full speech-to-text (see `instyper`)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate audio capture, processing pipeline, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) Libraries like TarsosDSP or JavaCV for audio processing
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for multi-channel audio input."
- "Integrate with jobops to annotate meeting transcripts by speaker."
- "Add a feature to output isolated speaker tracks as separate files."
- "Write tests that simulate audio streams and verify speaker isolation."

## How to Help the User

- Ensure audio capture works across OSes
- Suggest processing parameters for different environments
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's audio processing workflows

## Gotchas

- Audio APIs differ by OS; document required permissions
- Real-time processing can be CPU-intensive; manage performance
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full audio editing suite
- Cloud-based audio processing
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on audio use cases!** 