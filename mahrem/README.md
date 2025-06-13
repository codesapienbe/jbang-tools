# Mahrem

**Universal end-to-end message encryption**

## Project Goal

Mahrem provides a simple, command-line interface for encrypting and decrypting messages and files with strong, end-to-end encryption, ensuring privacy and security across platforms.

## Key Features

- Encrypt and decrypt text messages or files
- Support for multiple encryption algorithms (AES, RSA)
- Key generation and management
- Password or key-based access control
- Cross-platform (Windows, macOS, Linux)

## Usage

```sh
jbang mahrem/Mahrem.java --encrypt "message.txt" --out encrypted.bin
jbang mahrem/Mahrem.java --decrypt encrypted.bin --out message_decrypted.txt
```

## Example Use Cases

- Securely share sensitive documents via email or chat
- Automate encryption of backups or logs
- Integrate into CI/CD pipelines for secret management

## Configuration

- No external dependencies required (runs with JBang)
- Command-line flags for algorithm selection, keys, and output paths

## Contribution

- Fork the repo and submit pull requests
- See PROMPT.md for LLM and pair programming guidelines
- Issues and feature requests welcome!

## Ecosystem Integration

Mahrem can be used with:
- `safeclip` for encrypted clipboard data
- `qproj` for generating encrypted project templates
- `syncmate` for secure synchronization of encrypted files

## More info
See the main [jbang-catalog.json](../jbang-catalog.json) for all available tools. 