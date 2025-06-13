# PROMPT.md — LLM & Pair Programming Guide for Inspeak

## Project Context

Inspeak is a cross-platform, instant text-to-speech (TTS) tool. It is part of the JBang-Tools ecosystem and is designed for accessibility, productivity, and automation workflows.

## What This Project Does

- Converts text to speech instantly using high-quality voices
- Monitors files or clipboard for new text to read aloud
- Outputs audio to speakers or saves to a file
- CLI interface for scripting and automation

## What This Project Does NOT Do

- Does not perform speech recognition (see `instyper`)
- Does not provide a GUI (CLI only)
- Does not require cloud APIs (offline/local TTS preferred)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate TTS engine, file/clipboard monitoring, and output logic
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) FreeTTS, MaryTTS, or other Java-accessible TTS engines
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for selecting the voice or language via a command-line flag."
- "Integrate with offlan to provide translation before speech."
- "Add a feature to save the spoken output as an audio file."
- "Write a test that verifies the correct text is spoken for a given input."

## How to Help the User

- Ensure TTS works offline and is cross-platform
- Suggest ways to improve voice quality or reduce latency
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's workflow or target use case

## Gotchas

- TTS engine support and voice quality may vary by OS
- Some engines may require model downloads; document this clearly
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full GUI
- Cloud-based TTS (unless specifically requested)
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or a sample workflow!** 