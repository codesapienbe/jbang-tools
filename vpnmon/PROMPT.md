# PROMPT.md — LLM & Pair Programming Guide for Vpnmon

## Project Context

Vpnmon is a cross-platform CLI tool for monitoring VPN connections and intelligently selecting optimal servers based on performance metrics. It is part of the JBang-Tools ecosystem, designed for network monitoring and secure connectivity workflows.

## What This Project Does

- Checks VPN connection status and throughput
- Automatically selects the best VPN server based on latency, load, or custom criteria
- Logs historical performance data for analysis

## What This Project Does NOT Do

- Does not provide a GUI VPN client
- Does not require cloud services (all local)
- Does not configure VPN credentials or tunnels (focus on monitoring)

## Coding Conventions

- Java 17+ (preferably Java 21 or 22)
- Use JBang script headers for dependencies
- Modular code: separate monitoring, selection logic, and CLI parsing
- Use clear, descriptive variable and method names

## Dependencies

- JBang for script execution
- No external dependencies required for basic operation

## Example LLM Prompts

- "Add support for custom server lists via config files."
- "Integrate with pingmon to correlate server latency data."
- "Add a feature to automatically reconnect on failures."
- "Write tests that simulate server selection criteria and verify selection."

## How to Help the User

- Ensure connection checks work across OSes
- Suggest additional selection criteria (e.g., jitter, packet loss)
- Propose integration points with other JBang-Tools
- When in doubt, ask for the user's VPN providers and workflows

## Gotchas

- VPN status commands may require elevated privileges; document this
- Server selection logic should handle unreachable servers gracefully
- Keep CLI interface simple and scriptable

## Out of Scope

- Building a full VPN client or tunnel configuration
- Cloud-based VPN management
- Anything requiring non-open-source dependencies

---

**When in doubt, ask the user for clarification on VPN workflows and providers!** 