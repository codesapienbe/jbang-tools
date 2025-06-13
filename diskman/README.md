# Diskman

**Intelligent disk analyzer with smart cleanup**

## Project Goal

Diskman provides command-line tools to analyze disk usage, identify large or duplicate files, and perform safe cleanup operations to reclaim space on any platform.

## Key Features

- Analyze disk usage and report top directories/files
- Identify and clean duplicate files
- Preview and undo cleanup actions
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for automation and scripting

## Usage

```sh
jbang diskman/Diskman.java --analyze
jbang diskman/Diskman.java --cleanup --target /path/to/dir
```

## Example Use Cases

- Reclaim disk space by removing temporary or duplicate files
- Monitor disk usage trends over time
- Integrate with sysmon to alert on low disk space

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for analysis, cleanup, and report format

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Diskman can be used with:
- `sysmon` for disk space alerts
- `safeclip` for securely clearing clipboard cache
- `syncmate` for synchronizing cleaned directories

## More info

See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 