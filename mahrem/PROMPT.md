# PROMPT.md — LLM & Pair Programming Guide for Mahrem

## Project Context

Mahrem is a cross-platform CLI tool for end-to-end encryption of messages and files. It is part of the JBang-Tools ecosystem, focusing on privacy and security workflows.

## What This Project Does

- Encrypts and decrypts text or binary files
- Supports multiple algorithms (AES, RSA)
- Manages keys and secrets

## What This Project Does NOT Do

- Does not provide a GUI key manager
- Does not require cloud services
- Does not handle key exchange protocols (focus on local encryption)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate encryption, key management, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- (Optional) Bouncy Castle or Java Cryptography Architecture (JCA)
- No native dependencies required for basic operation

## Example LLM Prompts

- "Add support for GPG-compatible key formats."
- "Integrate with safeclip to copy encrypted data to clipboard."
- "Add a feature to rotate keys on a schedule."
- "Write tests that verify encryption-decryption round-trip for sample data."

## How to Help the User

- Ensure encryption works with different key types and sizes
- Suggest best practices for key storage and handling
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's security requirements and threat model

## Gotchas

- Cryptography APIs can be complex; handle exceptions and key errors gracefully
- Ensure default algorithms meet modern security standards
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full GUI key manager
- Cloud-based key vault integration (unless specifically requested)
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on security requirements!** 