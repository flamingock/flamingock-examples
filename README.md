![Header Image](misc/logo-with-text.png)
___

**Flamingock** is the evolution of Mongock, designed as a cloud-native solution for managing versioning, and auditing
changes in systems that evolve alongside your application.

Whilst Mongock focused on versioning NoSQL databases, Flamingock extends this concept to all technologies, systems, and
configurations, with built-in auditing and rollback capabilities. It ensures the application and its dependent
components evolve together, managing configuration changes during the application startup to enable seamless integration
and deployment.

Flamingock also introduces new mechanisms for defining changes in an extensible and customizable manner, beyond
traditional  code-based methods.

> Additionally, Flamingock offers multiple infrastructure setups for providing flexibility to users, as it introduces a
Cloud offering whilst still retaining existing supported databases such as MongoDB, DynamoDB, or Couchbase.

---

## Examples Overview

This repository is structured as **Individual Gradle Projects**, with each project demonstrating Flamingock's
integration with different frameworks, technologies, and use cases. Explore the examples to find the one that matches
your needs!

Each example is prepared to run from its own test with all infrastructure (databases, mocked servers, etc.) needed.
But you can also  run it with your own infrastructure.


## Migration from Mongock to Flamingock

If you're transitioning from Mongock to Flamingock, we have a [dedicated project](import-from-mongock/README_2.md) to guide you. 

---

## Index of Examples

| **Technology**        | **Example Project**                                                                  | **Description**                                                                                                                                           |
|-----------------------|--------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **MongoDB**           | **[mongodb-sync-standalone](mongodb/mongodb-sync-standalone/README.md)**             | Example of using Flamingock with the MongoDB synchronous driver in a Java standalone application.                                                         |
|                       | **[mongodb-springboot-springdata](mongodb/mongodb-springboot-springdata/README.md)** | Integration of Flamingock with MongoDB, Spring Boot 2.x, and Spring Data for seamless database migrations.                                                |
|                       | **[mongodb-springboot-sync](mongodb/mongodb-springboot-sync/README.md)**             | Demonstrates Flamingock with MongoDB sync driver and Spring Boot, without relying on Spring Data abstractions.                                            |
| **DynamoDB**          | **[dynamodb-standalone](dynamodb/dynamodb-standalone/README.md)**                    | Example showcasing Flamingock with DynamoDB in a Java standalone application.                                                                             |
| **S3**                | **[s3](s3/README.md)**                                                               | Example demonstrating Flamingock with AWS S3 for managing S3 bucket creation in a Java standalone application.                                            |
| **Couchbase**         | **[couchbase-standalone](couchbase/couchbase-standalone/README.md)**                 | Example of using Flamingock with Couchbase in a Java standalone application.                                                                              |
|                       | **[couchbase-springboot-v2](couchbase/couchbase-springboot-v2/README.md)**           | Demonstrates Flamingock with Couchbase and Spring Boot 2.x for database migrations.                                                                       |
| **GraalVM**           | **[graalvm](graalvm/README.md)**                                                     | Demonstrates how to use Flamingock with GraalVM native image compilation for building fast, lightweight applications.                                     |
| **Mongock Migration** | **[import-from-mongock](import-from-mongock/README_2.md)**                             | Example of migrating an existing Mongock project to Flamingock, preserving your existing change units while leveraging Flamingock's enhanced capabilities |
> 🚀 **New examples will be added regularly!** Stay tuned for updates as we expand the repository to cover even more
> systems and frameworks.

---

## How to Run Examples

**1. Clone this repository:**
```shell
   git clone https://github.com/mongock/flamingock-examples.git
   cd flamingock-examples
```

**2. Navigate to the example you want to explore:**
```shell
cd mongodb/mongodb-springboot-springdata
```

**3. Run example**

***3.a. Run the project test using Gradle***
```shell
./gradlew test
```

***3.b. Run the project using Gradle and your own infrastructure***
```shell
./gradlew run
```

**4. Follow the instructions in the specific project's README for further details.**

___

## Contributing
We welcome contributions! If you have an idea for a new example or improvement to an existing one, feel free to submit a
pull request. Check out our [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

___

## Get Involved
⭐ Star the [Flamingock repository](https://github.com/mongock/flamingock-project) to show your support!

🐞 Report issues or suggest features in the [Flamingock issue tracker](https://github.com/mongock/flamingock-project/issues).

💬 Join the discussion in the [Flamingock community](https://github.com/mongock/flamingock-project/discussions).

___

## License
This repository is licensed under the [Apache License 2.0](LICENSE.md).

___

## Explore, experiment, and empower your projects with Flamingock!
Let us know what you think or where you’d like to see Flamingock used next.
