# Offlan

**Completely offline translator (90+ languages)**

## Project Goal

Offlan provides fast, private, and fully offline translation between 90+ languages. It is designed for users who need reliable translation without an internet connection, ensuring privacy and speed for sensitive or remote use cases.

## Key Features

- Translate text between 90+ languages
- 100% offline operation (no network required)
- Fast, lightweight, and cross-platform
- CLI interface for scripting and automation
- Integrates with other JBang-Tools for speech or file translation

## Usage

```sh
jbang offlan/Offlan.java --from en --to es --text "Hello, world!"
```

## Example Use Cases

- Translate documents or text snippets without sending data to the cloud
- Use in remote locations with no internet
- Integrate with speech-to-text (instyper) or text-to-speech (inspeak) for full offline voice translation

## Configuration

- No external dependencies required (runs with JBang)
- Language models are bundled or downloaded once and cached
- Command-line flags for source/target language, input/output files, etc.

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Offlan can be used with:
- `instyper` for offline speech-to-text translation
- `inspeak` for offline text-to-speech
- `clipstack` for translating clipboard contents

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 