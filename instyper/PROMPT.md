# PROMPT.md — LLM & Pair Programming Guide for Instyper

## Project Context

Instyper is a cross-platform, ultra-low latency speech-to-text tool. It is part of the JBang-Tools ecosystem and is intended to be used both standalone and as a component in larger automation or productivity workflows.

## What This Project Does

- Listens to audio input (microphone or file)
- Converts speech to text in real time
- Outputs text to the console, clipboard, or a file

## What This Project Does NOT Do

- Does not perform translation (see `offlan`)
- Does not do text-to-speech (see `inspeak`)
- Does not provide a GUI (CLI only, but can be integrated with other tools)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22 for virtual threads)
- Use JBang script headers for dependencies
- Keep code modular: separate audio input, STT engine, and output logic
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) Vosk, DeepSpeech, or other Java-accessible STT engines
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for selecting the audio input device via a command-line flag."
- "Refactor the STT engine logic to allow easy swapping between Vosk and DeepSpeech."
- "Add a feature to output transcribed text to a file as well as the console."
- "Write a test that simulates a short audio clip and checks the transcription output."

## How to Help the User

- Always check for cross-platform compatibility
- Suggest ways to reduce latency (buffer sizes, threading)
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's workflow or target use case

## Gotchas

- Audio input APIs differ between OSes; use Java Sound API for best compatibility
- Some STT engines may require model downloads; document this clearly
- Keep memory usage low for long-running sessions

## Out of Scope

- Building a full GUI
- Cloud-based STT (unless specifically requested)
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification or a sample workflow!** 