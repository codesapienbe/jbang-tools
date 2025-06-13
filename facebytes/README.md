# Facebytes

**Event photo management with privacy features**

## Project Goal

Facebytes provides command-line tools for organizing, tagging, and anonymizing event photos, automating face blurring and secure management to protect privacy at scale.

## Key Features

- Automated face detection and blurring
- Tagging by person, event date, or location
- Batch processing of image directories
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for scripting and integration

## Usage

```sh
jbang facebytes/Facebytes.java --blur-dir ./photos
jbang facebytes/Facebytes.java --tag "Conference_2025"
```

## Example Use Cases

- Protect privacy by anonymizing faces in event photos
- Organize photos into tagged folders for easy retrieval
- Integrate into automated workflows for periodic processing

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for blur, tag, and output directory settings

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Facebytes can be used with:
- `lazyme` for face recognition-based tagging
- `safeclip` for secure sharing of blurred images
- `offlan` for translating metadata or captions

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 