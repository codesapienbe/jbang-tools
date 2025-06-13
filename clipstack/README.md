# Clipstack

**Advanced clipboard manager with AI categorization**

## Project Goal

Clipstack aims to provide a powerful, scriptable clipboard history manager that categorizes and searches clipboard entries using AI, enabling seamless access to past snippets and quick actions.

## Key Features

- Clipboard history with unlimited entries
- AI-based categorization and tagging
- Search and filter clipboard snippets
- Persistent storage and retrieval
- Cross-platform (Windows, macOS, Linux)
- CLI interface for integration and automation

## Usage

```sh
jbang clipstack/Clipstack.java --list
jbang clipstack/Clipstack.java --search "invoice"
```

## Example Use Cases

- Quickly retrieve previously copied text or code snippets
- Automate insertion of boilerplate content based on tags
- Integrate with workflow tools for smart clipboard actions

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for listing, searching, and categorization

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Clipstack can be used with:
- `instyper` for storing and managing transcribed text
- `inspeak` for reading clipboard entries aloud
- `jobops` for collecting URLs or job descriptions

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 