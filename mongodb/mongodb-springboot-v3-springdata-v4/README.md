![Header Image](../../misc/logo-with-text.png)
___

# MongoDB Springboot v3 with Springdata v4 Example

## 📖 Example Overview

Welcome to the MongoDB Springboot v3 with Springdata v4 Example. This demonstrates how to use Flamingock with MongoDB in
a Java Springboot v3 application with Springdata v4. It highlights key functionalities such as configuring Flamingock in
a Springboot v3 application using Springdata v4 as storage driver.

As Springboot application, Flamingock configuration was in *resources/application.yml*:
```yaml
flamingock:
   stages:
      - name: stage1
        code-packages:
           - io.flamingock.examples.community.changes
   transactionEnabled: true
```

This example has 3 Flamingock Changes:
1. Creates a new collection called *clientCollection*.
2. Adds one document to collection.
3. Adds another document to collection.

## Table of Contents

1. [📌 Dependencies](#-dependencies)
2. [🛠 How to Run this Example](#-how-to-run-this-example)
3. [✅ Proven Functionalities](#-proven-functionalities)

---

## 📌 Dependencies

This example requires the following dependencies:
### Flamingock Dependencies
    implementation("io.flamingock:flamingock-springboot-v3-runner:0.0.30-beta")
    implementation("io.flamingock:mongodb-springdata-v4-driver:0.0.30-beta")

### Springboot Dependency
    implementation("org.springframework.boot:spring-boot-starter-web")

### Springdata for MongoDB Dependency
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

Also, it requieres the following plugins:
### Springboot plugins
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"

## 🛠 How to Run this Example

There are two ways to run this example:

### 1. Run Test (Recomended)
The recommended method to run this example is by executing the tests, which include a MongoDB TestContainer for testing
purposes.
```shell
./gradlew test
```

### 2. Run Main Class
To run the main class, ensure you have MongoDB running. Configure Springdata to use your own endpoint. And run example
with:
```shell
./gradlew run
```

## ✅ Proven functionalities

This example demonstrates the following functionalities:
1. Configuring Flamingock in a Springboot v3 application for use Springdata v4 as storage driver
   - Set Stages and other configuration in *resources/application.yml*
   - Set Listeners as Springboot Beans

___

### 📢 Contributing
We welcome contributions! If you have an idea for a new example or improvement to an existing one, feel free to submit a
pull request. Check out our [CONTRIBUTING.md](../../CONTRIBUTING.md) for guidelines.

___

### 🤝 Get Involved
⭐ Star the [Flamingock repository](https://github.com/mongock/flamingock-project) to show your support!

🐞 Report issues or suggest features in the [Flamingock issue tracker](https://github.com/mongock/flamingock-project/issues).

💬 Join the discussion in the [Flamingock community](https://github.com/mongock/flamingock-project/discussions).

___

### 📜 License
This repository is licensed under the [Apache License 2.0](../../LICENSE.md).

___

### 🔥 Explore, experiment, and empower your projects with Flamingock!
Let us know what you think or where you’d like to see Flamingock used next.
