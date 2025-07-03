![Header Image](../misc/logo-with-text.png)
___

# Flamingock with GraalVM Example

## Example Overview

This project demonstrates how to integrate [Flamingock](https://github.com/mongock/flamingock-project) and
GraalVM native image, offering significantly faster startup times and reduced memory footprint compared to traditional JVM applications. It includes examples for both Maven and Gradle.

> **Note**: This is a simplified example to demonstrate Flamingock integration with GraalVM native image. For production applications, please refer to the official GraalVM documentation for comprehensive native image configuration guidelines.


## Why GraalVM with Flamingock?

- **Reduced Startup Time**: GraalVM native images compile your application ahead-of-time into a standalone executable, resulting in near-instant startup times. This is particularly beneficial for cloud-native applications and microservices.
- **Lower Memory Footprint**: Native images typically consume less memory compared to JVM-based applications, allowing for more efficient resource utilization.
- **Improved Performance**: In some cases, native images can provide performance improvements due to optimizations performed during the native image build process.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Setup](#setup)
4. [Common issues and solutions](#common-issues-and-solutions)
5. [Proven functionalities](#proven-functionalities)

## Prerequisites

Before you start, ensure you have the following installed:

- **Java Development Kit (JDK)**: Ensure you have a compatible JDK installed. GraalVM provides its own JDK, which is recommended.
- **GraalVM**: Install GraalVM using SDKMAN or manually.
- **Native Image Tool**: Install the GraalVM `native-image` tool.
- **Maven and Gradle**: Both are supported, and you can choose your preferred build tool.
- **Docker**: If you plan to use a Docker-based native image build.

## Installation

### 1. Install GraalVM using SDKMAN (Recommended)

SDKMAN is a convenient tool for managing parallel versions of multiple Software Development Kits on most Unix-based systems.

```bash
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash

# Open a new terminal or source SDKMAN
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install GraalVM (e.g., version 21-java17)
sdk install java 21-graalce

# Set GraalVM as the default Java version
sdk use java 21-graalce

# Verify installation
java -version
```

### 2. Install Native Image Tool

After installing GraalVM, install the `native-image` tool using the GraalVM Updater.

```bash
# Install native-image tool
gu install native-image

# Verify installation
native-image --version
```

## Setup
Refer to the setup instructions for your chosen build tool:

- [Maven Setup](SETUP_MAVEN.md)
- [Gradle Setup](SETUP_GRADLE.md)

## Common issues and solutions

- **`java.lang.UnsupportedClassVersionError`**:
  - **Problem**: This error typically occurs when the class file is compiled with a newer JDK version than the one used at runtime.
  - **Solution**: Ensure that your `JAVA_HOME` environment variable points to the correct GraalVM JDK and that your project is configured to use the same JDK version.

- **Missing Dependencies**:
  - **Problem**: The native image build may fail due to missing dependencies or reflection configurations.
  - **Solution**: Review the build output for missing dependencies and add them to your project. For reflection issues, create a `reflect.config.json` file with the necessary reflection configurations.

- **Build Errors During Native Image Generation**:
  - **Problem**: Native image generation may fail due to various reasons, such as unsupported features or incorrect configurations.
  - **Solution**: Consult the GraalVM documentation and error messages for guidance. Ensure that your application is compatible with native image compilation.

## Proven functionalities

This example demonstrates how to use Flamingock in a Graalvm project.

___

### Contributing
We welcome contributions! If you have an idea for a new example or improvement to an existing one, feel free to submit a
pull request. Check out our [CONTRIBUTING.md](../CONTRIBUTING.md) for guidelines.

___

### Get involved
⭐ Star the [Flamingock repository](https://github.com/mongock/flamingock-project) to show your support!

🐞 Report issues or suggest features in the [Flamingock issue tracker](https://github.com/mongock/flamingock-project/issues).

💬 Join the discussion in the [Flamingock community](https://github.com/mongock/flamingock-project/discussions).

___

### License
This repository is licensed under the [Apache License 2.0](../LICENSE.md).

___

### Explore, experiment, and empower your projects with Flamingock!
Let us know what you think or where you’d like to see Flamingock used next.
