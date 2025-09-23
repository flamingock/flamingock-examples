![Header Image](../../misc/logo-with-text.png)
___

# MongoDB Springboot with Springdata Example

## Example Overview

Welcome to the MongoDB Springboot with Springdata Example. This demonstrates how to use Flamingock with MongoDB in a
Java Springboot application with Springdata. It highlights key functionalities such as configuring Flamingock to use
Springdata as storage driver.

This example has 3 Flamingock Changes:
1. Creates a new collection called *clientCollection*.
2. Adds one document to collection.
3. Adds another document to collection.

## Table of Contents

1. [Dependencies](#dependencies)
2. [How to run this example](#how-to-run-this-example)
3. [Proven functionalities](#proven-functionalities)

---

## Dependencies

This example requires the following dependencies:
### Flamingock dependencies
    implementation(platform("io.flamingock:flamingock-community-bom:$flamingockVersion"))
    implementation("io.flamingock:flamingock-community")
    implementation("io.flamingock:flamingock-springboot-integration")
    annotationProcessor("io.flamingock:flamingock-processor:$flamingockVersion")

### Springboot dependency
    implementation("org.springframework.boot:spring-boot-starter-web")

### Springdata for MongoDB dependency
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

Also, it requieres the following plugins:
### Springboot plugins
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"

### Compatibility versions

Check out the [compatibility documentation](compatibility_versions.md) for using Flamingock with Spring Boot and MongoDB Spring Data.

## How to run this example

There are two ways to run this example:

### 1. Running tests (Recomended)
The recommended method to run this example is by executing the tests, which include a MongoDB TestContainer for testing
purposes.
```shell
./gradlew test
```

### 2. Running the main class
To run the main class, ensure you have MongoDB running. Configure Springdata to use your own endpoint.
Run the example:
```shell
./gradlew run
```

## Proven functionalities

This example demonstrates the following functionalities:
1. Configuring Flamingock in a Springboot application
   - Demonstrates how to use Flamingock with Springboot.
2. Auditing Changes with Springdata
   - Demonstrates how to audit changes using Springdata as the storage backend.

___

### Contributing
We welcome contributions! If you have an idea for a new example or improvement to an existing one, feel free to submit a
pull request. Check out our [CONTRIBUTING.md](../../CONTRIBUTING.md) for guidelines.

___

### Get involved
⭐ Star the [Flamingock repository](https://github.com/flamingock/flamingock-java) to show your support!

🐞 Report issues or suggest features in the [Flamingock issue tracker](https://github.com/flamingock/flamingock-java/issues).

💬 Join the discussion in the [Flamingock community](https://github.com/flamingock/flamingock-java/discussions).

___

### License
This repository is licensed under the [Apache License 2.0](../../LICENSE.md).

___

### Explore, experiment, and empower your projects with Flamingock!
Let us know what you think or where you’d like to see Flamingock used next.
