# Safeclip

**AI-powered clipboard security scanner**

## Project Goal

Safeclip provides command-line scanning of clipboard content to detect and warn about sensitive or malicious data, ensuring privacy and security in clipboard workflows.

## Key Features

- Scan clipboard text and images for sensitive data patterns
- AI-based classification of potential security risks
- Real-time scanning and alerting of new clipboard entries
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for integration and automation

## Usage

```sh
jbang safeclip/Safeclip.java --scan
jbang safeclip/Safeclip.java --whitelist "example.com"
```

## Example Use Cases

- Detect accidental copying of credentials or secrets
- Automate checks before sharing clipboard content
- Integrate with jobops to validate clipboard-based job URLs

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for scan modes, whitelists, and alert thresholds

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Safeclip can be used with:
- `clipstack` for secure clipboard history management
- `jobops` for sanitizing job URLs
- `instyper` for checking transcribed text before saving

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 