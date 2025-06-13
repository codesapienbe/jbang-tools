# Feedme

**AI-powered food inspection and nutrition analysis**

## Project Goal

Feedme provides command-line tools to analyze food images and nutritional data, offering insights into ingredients, calorie estimates, and safety checks using AI models.

## Key Features

- Inspect food images for ingredient detection
- Estimate nutritional values (calories, macros)
- Allergy and spoilage warnings
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for batch analysis and scripting

## Usage

```sh
jbang feedme/Feedme.java --image salad.jpg
jbang feedme/Feedme.java --nutrition "apple, banana"
```

## Example Use Cases

- Analyze meal photos and log nutrition data
- Automate food quality inspections for catering
- Integrate into health tracking workflows

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for input image, text, and output format

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Feedme can be used with:
- `clipstack` for copying analysis results to clipboard
- `inspeak` for reading nutritional summaries aloud
- `jobops` for automating job descriptions in food industry roles

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 