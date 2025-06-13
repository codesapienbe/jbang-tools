# Quickfile

**Lightning-fast file search with learning algorithms**

## Project Goal

Quickfile provides a CLI utility for ultra-fast searching of files and directories using learning-based indexing and caching strategies to optimize search performance.

## Key Features

- Real-time file system indexing
- Intelligent learning algorithms for search ranking
- Fuzzy and exact search modes
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for integration and automation

## Usage

```sh
jbang quickfile/Quickfile.java --index /path/to/dir
jbang quickfile/Quickfile.java --search "report"
```

## Example Use Cases

- Quickly locate files in large codebases
- Automate periodic indexing for updated directories
- Integrate with sysmon to search logs or configuration files

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for index path, search query, and modes

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Quickfile can be used with:
- `safeclip` for searching clipboard history files
- `qproj` for navigating template directories
- `jobops` for locating job application files

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 