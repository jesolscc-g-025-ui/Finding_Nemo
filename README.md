# Finding_Nemo

# Java IoT Security Scanner

A Java-based IoT Security Scanner that scans network ports, discovers device MAC addresses, identifies device manufacturers, and generates JSON security reports.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Makefile Commands](#makefile-commands)
- [JSON Reports](#json-reports)
- [Running Tests](#running-tests)
- [Cross-Platform Compatibility](#cross-platform-compatibility)
- [Troubleshooting](#troubleshooting)
- [Security and Responsible Use](#security-and-responsible-use)

## Overview

The Java IoT Security Scanner is designed to help identify exposed network services on IoT devices and provide basic device identification information.

The application accepts a target IP address, scans its configured TCP ports, attempts to discover its MAC address, looks up the associated manufacturer, and saves the scan results in a JSON report.

The project demonstrates object-oriented programming, automated testing, dependency management, network programming, API integration, local registry lookup, and report generation.

## Features

### Port Scanning
- Checks configured TCP ports on a target IP address.
- Identifies open ports and their associated service labels.
- Collects scan results for reporting.

### MAC Address Discovery
- Uses system network utilities and the ARP cache to attempt to discover a device's MAC address.
- Handles unsuccessful discovery attempts.

### Manufacturer Lookup
- Uses an online manufacturer lookup API when the device is reachable.
- Uses a local IEEE OUI registry CSV file as a fallback.
- Falls back to the local registry if the API lookup fails.

### JSON Security Reports
- Generates a JSON report after a scan.
- Includes the target IP address, device information, scan summary, and port results.
- Saves reports in the `Reports` directory.

### Automated Testing
- Uses Maven to compile the application and run automated tests.
- Tests scanner logic, manufacturer lookup, fallback behavior, and report generation.

## Technologies

- **Java 21** — application development
- **Maven** — dependency management and build automation
- **JUnit 5** — automated testing
- **Gson** — JSON serialization
- **GNU Make** — convenient build and execution commands
- **TCP sockets** — port connectivity checks
- **ARP and ping utilities** — device discovery
- **CSV registry** — offline manufacturer lookup

## Project Structure

The project follows a Maven directory structure.

```text
IoT-Security-Scanner/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── project1/
│   │   │       ├── IoTScannerApp.java
│   │   │       ├── IoTScanner/
│   │   │       ├── MacDiscovery/
│   │   │       └── SecurityReport/
│   │   └── resources/
│   │       └── oui36.csv
│   └── test/
│       └── java/
├── Reports/
├── target/
├── Makefile
├── pom.xml
└── README.md
```

The package and resource names above reflect the intended organization. Adjust the tree if your actual source directories or CSV filename differ.

## Prerequisites

Install the following before running the application:

1. **Java Development Kit (JDK) 21 or compatible configured Java version.**
2. **Apache Maven** to compile, test, and package the project.
3. **GNU Make** to use the Makefile commands.
4. **Network utilities** required for MAC address discovery, such as `ping` and `arp` or the operating system's equivalent.
5. An internet connection for online manufacturer lookups. Local CSV lookup may still work offline.

Verify your Java and Maven installations:

```bash
java -version
javac -version
mvn -version
```

Verify Make is installed:

```bash
make --version
```

## Installation

### 1. Obtain the project

Clone the repository if it is hosted on Git:

```bash
git clone <repository-url>
cd <repository-directory>
```

Alternatively, open the project directory containing `pom.xml`.

### 2. Check the manufacturer registry

Ensure the local OUI CSV registry is stored in the application's resources directory and that its filename matches the one used by the code.

The registry supports manufacturer lookup when the online API is unavailable or cannot provide a valid result.

### 3. Compile the project

Using Maven:

```bash
mvn compile
```

Using Make:

```bash
make compile
```

### 4. Run the tests

```bash
make test
```

Or use Maven directly:

```bash
mvn test
```

## Running the Application

The application accepts one argument: the target IP address.

### Using Make

```bash
make run IP=127.0.0.1
```

Replace `127.0.0.1` with the IP address of a device you own or are authorized to scan.

### Using Maven

```bash
mvn compile exec:java "-Dexec.args=127.0.0.1"
```

The Maven Exec Plugin must be configured in `pom.xml` with the main class:

```text
project1.IoTScannerApp
```

### Packaging the application

To build the project and its configured artifacts:

```bash
make package
```

Or:

```bash
mvn package
```

The generated JAR files are placed in the `target` directory.

If the Maven Assembly Plugin is configured to create a JAR containing dependencies, the resulting `jar-with-dependencies` artifact can be used to launch the application with Java. Use the actual generated filename:

```bash
java -jar target/<generated-jar-name>.jar 127.0.0.1
```

## Makefile Commands

| Command | Description |
|---|---|
| `make help` | Displays available commands |
| `make compile` | Compiles the source code |
| `make test` | Runs automated tests |
| `make package` | Builds the application and configured artifacts |
| `make verify` | Runs Maven verification, including tests |
| `make run IP=127.0.0.1` | Runs the scanner against the specified IP |
| `make clean` | Removes generated Maven build files |
| `make` | Runs the default target, configured as `verify` |

The Makefile requires GNU Make and Maven to be installed and accessible through the system's `PATH`.

## JSON Reports

After a successful scan and report-writing operation, the application saves a JSON report in the `Reports` directory.

The filename follows this pattern:

```text
Reports/Scan-Report-<target-ip>.json
```

For example:

```text
Reports/Scan-Report-127.0.0.1.json
```

The report contains information such as:

- Scan date and time
- Target IP address
- Device MAC address
- Manufacturer information
- Total configured ports checked
- Number of open ports
- Scan status
- Details of detected open ports

Example report structure:

```json
{
  "scanDate": "2026-10-29T10:30:00",
  "target": "192.168.1.10",
  "device": {
    "ipAddress": "192.168.1.10",
    "macAddress": "00:11:22:33:AA:BB",
    "manufacturer": "Example Manufacturer"
  },
  "summary": {
    "totalPortsChecked": 9,
    "openPorts": 2,
    "scanStatus": "COMPLETED"
  },
  "ports": []
}
```

This is an illustrative example, not a report from an actual scan. The real port list and values depend on the application and scan results.

## Running Tests

Run the complete automated test suite:

```bash
make test
```

Or:

```bash
mvn test
```

To perform the full Maven verification lifecycle:

```bash
make verify
```

A successful run should finish with:

```text
BUILD SUCCESS
```

The tests should cover the implemented functionality, including port scanning, manufacturer lookup, fallback behavior, and JSON report generation.

## Cross-Platform Compatibility

The application is written in Java and uses Maven, making it suitable for Windows, Ubuntu, Kali Linux, and other operating systems with compatible Java and Maven installations.

| Operating system | Recommended environment |
|---|---|
| Windows 10/11 | PowerShell or Command Prompt with Java, Maven, and GNU Make installed |
| Ubuntu | Terminal with Java, Maven, and Make installed |
| Kali Linux | Terminal with Java, Maven, and Make installed |

The Java application uses Java's `Path` API for report paths, which helps avoid hard-coded operating-system-specific file separators.

However, MAC discovery depends on the available system commands and ARP output format. These can differ between Windows and Linux, so discovery should be tested on each supported platform.

The Makefile also depends on the installed Make implementation and shell. On Windows, using Ubuntu through WSL is one option for a consistent GNU Make environment.

## Troubleshooting

### Maven is not recognized

If the terminal reports that `mvn` is not recognized, install Apache Maven and ensure its `bin` directory is on your `PATH`.

Verify with:

```bash
mvn -version
```

### Make is not recognized

Install GNU Make for your operating system or use an environment that provides it, such as Ubuntu or Kali Linux.

### The online manufacturer lookup fails

Check your internet connection and API availability. The application should use the local CSV registry when the API lookup fails, provided the fallback is configured correctly.

### The MAC address cannot be discovered

Confirm that the target is reachable, that the relevant network utility is installed, and that the system's ARP cache contains an entry for the device. A successful ping does not guarantee that a MAC address will be available, particularly when scanning across network boundaries.

### No open ports are found

An empty list of open ports does not necessarily mean the scan failed. The target may have no open ports among those configured, or a firewall may block connection attempts.

### The JSON report is missing

Check the application's console output, report-writing errors, and the permissions of the project directory. Confirm that the report path is correct and that the application completed the report-writing operation.

## Security and Responsible Use

This tool is intended for learning, testing, and authorized network assessment.

- Scan only devices and networks you own or have explicit permission to assess.
- Only the configured ports are checked; the scanner is not a complete security audit.
- An open port does not automatically mean a vulnerability exists.
- A closed or filtered port does not guarantee that a device is secure.
- Treat generated reports as potentially sensitive because they contain network and device information.
- Keep API credentials out of source control if the manufacturer service requires them.

## Future Improvements

Possible enhancements include:

- Adding configurable port lists.
- Supporting more manufacturer registries and APIs.
- Improving MAC discovery across different operating systems.
- Adding scan timeouts and configurable concurrency.
- Improving error reporting and scan-status handling.
- Adding report formats such as CSV or HTML.
- Supporting more detailed service and vulnerability checks.



## Design Choices

The IoT Security Scanner was designed using object-oriented programming principles, separation of responsibilities, and testable components. The following design decisions were made to improve maintainability, reliability, and extensibility.

### 1. Separation of Responsibilities

The application is divided into separate classes and packages, with each component responsible for a specific task.

- **`IoTScannerApp`** — serves as the entry point and coordinates the scanning and reporting workflow.
- **`IoTScanner`** — manages configured ports and performs TCP port scanning.
- **MAC discovery component** — attempts to discover a target device's MAC address using network utilities.
- **Manufacturer lookup components** — retrieve manufacturer information from an online API or local CSV registry.
- **`ScanReport`** — represents the information collected during a scan.
- **`ReportWriter`** — serializes the report to JSON and writes it to a file.

This separation makes individual components easier to understand, test, maintain, and modify without unnecessarily affecting the rest of the application.

### 2. Dependency Injection and Testability

Dependency injection is used to allow certain components to receive their dependencies through constructors rather than creating all dependencies internally.

For example, the port scanner accepts a `PortChecker` implementation. This allows the scanner to use real TCP connection checks during normal operation and a controlled test implementation during unit testing.

The MAC discovery functionality also uses an injectable command runner, allowing command execution and output parsing to be tested without depending entirely on real system commands.

**Why this decision was made:**

- Reduces dependence on real network conditions during tests.
- Makes tests more predictable and repeatable.
- Allows failures and edge cases to be simulated.
- Makes it easier to replace implementations in the future.

### 3. Online API and Local CSV Fallback
===========================================
Manufacturer identification uses a fallback strategy rather than relying on a single information source.

The application first checks device reachability. When the device is reachable, it attempts an online manufacturer lookup. If the API fails or returns an unusable result, the application falls back to the local CSV registry.

When the device is unreachable, the application can skip the online lookup and use the local registry directly, provided a MAC address is available.

**Why this decision was made:**

- Improves resilience when the API or internet connection is unavailable.
- Allows manufacturer lookup to continue using locally available data.
- Separates online service failures from the rest of the scanning workflow.

The local registry depends on having a valid MAC address and a matching manufacturer entry. A fallback therefore cannot guarantee that every device will be identified.

### 4. Local OUI Registry
==========================
A local CSV file is used as a source of manufacturer information based on MAC address prefixes.

The registry is loaded from the application's resources and mapped into a lookup data structure for efficient access.

MAC addresses may be normalized before lookup to handle differences in letter case and separators.

**Why this decision was made:**

- Avoids requiring an internet connection for every manufacturer lookup.
- Provides a local alternative when the online API is unavailable.
- Keeps manufacturer data separate from application logic.
- Makes it possible to update the registry without redesigning the lookup algorithm.

The registry must be kept up to date, and a MAC prefix may identify an address block's registrant rather than the manufacturer of the complete device.

### 5. Configurable Port Definitions
=============================================
The scanner stores configured ports and their service labels in a map. This keeps port numbers associated with meaningful names and allows the scan logic to iterate through the configured collection.

**Why this decision was made:**

- Avoids scattering port definitions throughout the scanning logic.
- Makes the list of ports easier to maintain.
- Allows the scanner to report how many ports were configured for checking.

The configured port count represents the number of entries in the map. It should not be confused with the number of open ports or the number of successful connections.

### 6. JSON Report Model and Writer
======================================
The report is represented by a dedicated `ScanReport` object containing device information, scan metadata, a summary, and port results.

Gson is used to serialize the report object into readable JSON, while `ReportWriter` handles writing the serialized content to a file.

The report is saved in a dedicated `Reports` directory, which the writer creates when necessary.

**Why this decision was made:**

- Separates report data from file-writing logic.
- Produces a structured format that other tools can read.
- Makes report contents easier to validate in automated tests.
- Supports future integration with dashboards, data analysis tools, or other reporting systems.

### 7. Automated Testing
=========================
JUnit 5 is used to test individual components and important failure scenarios.

Tests cover scanner behavior, manufacturer lookup, fallback behavior, and report generation. Dependencies such as network checkers and command runners can be replaced with controlled implementations to reduce reliance on external systems.

Temporary directories can be used to test report writing without depending on a permanent output directory.

**Why this decision was made:**

- Detects regressions when the implementation changes.
- Allows edge cases to be tested without requiring every external service to be available.
- Provides evidence that individual components behave as expected.
- Makes the project safer to refactor and extend.

### 8. Maven and Makefile Build Automation
===========================================
Maven manages project dependencies, compilation, testing, and packaging. A Makefile provides shorter, consistent commands for common development tasks.

For example:

```bash
make test
make package
make run IP=127.0.0.1
make clean
```

**Why this decision was made:**

- Reduces repetitive build commands.
- Makes the project easier for another developer to set up.
- Provides a consistent workflow across supported environments.
- Separates build automation from the Java application itself.

Maven and GNU Make must be installed on the target system, and system-dependent MAC discovery commands may still require platform-specific handling.

### 9. Cross-Platform Considerations
========================================
The application uses Java APIs for operations such as file path construction and report writing, rather than relying on hard-coded operating-system-specific path separators.

However, MAC discovery uses external network utilities whose names, arguments, and output formats can differ between Windows and Linux.

**Design implication:** portability is addressed at the application and build levels, while operating-system-specific network behavior must be handled and tested separately.

Summary
-------
These design choices aim to produce a scanner that is modular, testable, maintainable, and resilient to common failures. Separating responsibilities, injecting dependencies, providing a local manufacturer fallback, and automating builds and tests also makes it easier to extend the project with additional scanning and reporting features.

-------------------|
#VERIFICATION CODE |
-------------------|
WTC-F7YDP5XL       |
--------------------