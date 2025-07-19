![Header Image](../../misc/logo-with-text.png)
___

# MongoDB Example

## Example Overview

Welcome to the MongoDB Standalone Example. This demonstrates how to use Flamingock with MongoDB in a Java
standalone application. It highlights key functionalities such as auditing changes using MongoDB as storage backend.

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
    implementation(platform("io.flamingock:flamingock-ce-bom:$flamingockVersion"))
    implementation ("io.flamingock:flamingock-ce-mongodb-sync")
    annotationProcessor("io.flamingock:flamingock-processor")

### MongoDB dependencies
    implementation("org.mongodb:mongodb-driver-sync:5.5.1")
    implementation("org.mongodb:mongodb-driver-core:5.5.1")
    implementation("org.mongodb:bson:5.5.1")

## How to run this example

There are two ways to run this example:

### 1. Running tests (Recomended)
The recommended method to run this example is by executing the tests, which include a MongoDB TestContainer for testing
purposes.
```shell
./gradlew test
```

### 2. Running the main class
To run the main class, ensure you have MongoDB running. Configure the MongoDB client with your settings:
1. Open the main class file
2. Change the MongoDB endpoint with your own:
```java
public static void main(String[] args) {
    new CommunityStandaloneMongodbSyncApp()
            .run(getMongoClient("mongodb://localhost:27017/"), DATABASE_NAME); // Set your MongoDB endpoint
}
```
3. Run the example:
```shell
./gradlew run
```

## Proven functionalities

This example demonstrates how to audit Changes with MongoDB as the storage backend.

___

### Contributing
We welcome contributions! If you have an idea for a new example or improvement to an existing one, feel free to submit a
pull request. Check out our [CONTRIBUTING.md](../../CONTRIBUTING.md) for guidelines.

___

### Get involved
⭐ Star the [Flamingock repository](https://github.com/mongock/flamingock-project) to show your support!

🐞 Report issues or suggest features in the [Flamingock issue tracker](https://github.com/mongock/flamingock-project/issues).

💬 Join the discussion in the [Flamingock community](https://github.com/mongock/flamingock-project/discussions).

___

### License
This repository is licensed under the [Apache License 2.0](../../LICENSE.md).

___

### Explore, experiment, and empower your projects with Flamingock!
Let us know what you think or where you’d like to see Flamingock used next.
