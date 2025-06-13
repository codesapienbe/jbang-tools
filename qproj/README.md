# Qproj

**Project generator with 100k+ templates**

## Project Goal

Qproj provides a CLI interface to generate projects from a curated library of 100,000+ templates, enabling developers to bootstrap new applications quickly and consistently.

## Key Features

- Browse and search 100k+ project templates
- Generate projects with custom parameters
- Support for multiple languages and frameworks
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for automation and scripting

## Usage

```sh
jbang qproj/Qproj.java --search spring-boot
jbang qproj/Qproj.java --generate spring-boot --artifact my-app
```

## Example Use Cases

- Bootstrap a new microservice with Spring Boot
- Generate frontend projects with React or Angular
- Integrate into CI pipelines to standardize project structure

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for search, generation parameters, and output directories

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Qproj can be used with:
- `safeclip` for templating clipboard workflows
- `jobops` for generating project scaffolds for AI jobs
- `syncmate` for syncing generated projects

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 