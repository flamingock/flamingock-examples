![Header Image](../../misc/logo-with-text.png)
___

# Couchbase Springboot with Springdata Example

## 📖 Example Overview

Welcome to the Couchbase Springboot with Springdata Example. This demonstrates how to use Flamingock with Couchbase in a
Java Springboot application with Springdata. It highlights key functionalities such as configuring Flamingock to use
Springdata for Couchbase as storage driver and defining a Rollback execution to your changes.

As Springboot application, Flamingock configuration was in *resources/application.yml*:
```yaml
flamingock:
   couchbase:
      scope: examplescope
      collection: examplecollection
   stages:
      - name: stage1
        code-packages:
           - io.flamingock.examples.community.couchbase.changes
   transactionEnabled: false
```

This example has 1 Flamingock Change:
1. Initialize an index in a bucket called *bucket*. With Rollback that drops that bucket.
```java
    @RollbackExecution
    public void rollbackExecution(Cluster cluster) {
        cluster.queryIndexes().dropIndex("bucket", "idx_springboot_index", DropQueryIndexOptions.dropQueryIndexOptions().ignoreIfNotExists(true));
    }
```

## Table of Contents

1. [📌 Dependencies](#-dependencies)
2. [🛠 How to Run this Example](#-how-to-run-this-example)
3. [✅ Proven Functionalities](#-proven-functionalities)

---

## 📌 Dependencies

This example requires the following dependencies:
### Flamingock Dependencies
    implementation("io.flamingock:flamingock-springboot-v2-runner:0.0.32-beta")
    implementation("io.flamingock:couchbase-springboot-v2-driver:0.0.32-beta")

### Springboot Dependency
    implementation("org.springframework.boot:spring-boot-starter-web")

### Springdata for Couchbase dependency
    implementation("org.springframework.data:spring-data-couchbase:4.4.8")

Also, it requieres the following plugins:
### Springboot plugins
    id("org.springframework.boot") version "2.7.12"
    id("io.spring.dependency-management") version "1.1.0"

## 🛠 How to Run this Example

There are two ways to run this example:

### 1. Run Test (Recomended)
The recommended method to run this example is by executing the tests, which include a MongoDB TestContainer for testing
purposes.
```shell
./gradlew test
```

### 2. Run Main Class
To run the main class, ensure you have Couchbase running. Configure Springdata to use your own endpoint. And run example
with:
```shell
./gradlew run
```

## ✅ Proven functionalities

This example demonstrates the following functionalities:
1. Configuring Flamingock in a Springboot application for use Springdata as storage driver
   - Set Stages and other configuration in *resources/application.yml*
   - Set Listeners as Springboot Beans
2. Defining a Rollback function in your changes.

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
