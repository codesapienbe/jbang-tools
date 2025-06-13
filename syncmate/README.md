# Syncmate

**Intelligent file synchronization with smart conflict resolution**

## Project Goal

Syncmate provides a CLI utility to synchronize files and directories between locations, using AI-based conflict resolution to handle edits and merges automatically.

## Key Features

- One-way and two-way file synchronization
- AI-driven conflict detection and resolution
- Support for local and remote destinations
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for integration and automation

## Usage

```sh
jbang syncmate/Syncmate.java --sync /src /dest
jbang syncmate/Syncmate.java --dry-run
```

## Example Use Cases

- Keep local and remote backup directories in sync
- Automate synchronization of project directories across machines
- Integrate with jobops for backing up generated documents

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for source, destination, sync mode, and conflict strategy

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Syncmate can be used with:
- `diskman` for synchronizing cleaned directories
- `qproj` for syncing generated project templates
- `safeclip` for backing up clipboard histories

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 