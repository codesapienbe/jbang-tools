# Vaultray

**Lightweight password manager with tray integration**

## Project Goal

Vaultray provides secure, local password management accessible via the system tray, enabling quick retrieval and autofill of credentials without compromising security.

## Key Features

- Store, retrieve, and manage passwords
- Access via system tray icon for quick lookup
- Clipboard auto-clear to protect credentials
- Cross-platform support (Windows, macOS, Linux)
- CLI interface for scripting and automation

## Usage

```sh
jbang vaultray/Vaultray.java --add
jbang vaultray/Vaultray.java --get example.com
```

## Example Use Cases

- Quick clipboard copying of credentials when logging into sites
- Secure password retrieval via hotkeys
- Integration with login scripts for automation

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for adding, retrieving, and deleting entries

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Vaultray can be used with:
- `safeclip` for secure handling of copied credentials
- `winman` for unlocking password-protected applications
- `syncmate` for syncing encrypted vaults between machines

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 