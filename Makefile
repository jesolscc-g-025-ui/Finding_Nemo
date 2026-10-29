.PHONY: all compile test package verify run clean help
# Java IoT Security Scanner

# Cross-platform Makefile using Maven

MVN ?= mvn
APP_MAIN = project1.IoTScannerApp



# Default: run tests and package the application
all: verify

# Compile the source code
compile:
	$(MVN) compile

# Run the unit tests
test:
	$(MVN) test

# Build the application and its dependencies
package:
	$(MVN) package

# Compile, test, and verify the project
verify:
	$(MVN) verify

# Run the scanner against a supplied IP address
run:
	$(MVN) compile exec:java "-Dexec.args=$(IP)"

# Remove generated build files
clean:
	$(MVN) clean

# Display available commands
help:
	@echo IoT Security Scanner - Available Commands
	@echo make compile
	@echo make test
	@echo make package
	@echo make verify
	@echo make run IP=127.0.0.1
	@echo make clean
