![Header Image](../misc/logo-with-text.png)
___

# Couchbase Example

## Example overview

Welcome to the Couchbase Example. This demonstrates how to use Flamingock with Couchbase in a Java
standalone application. It highlights key functionalities such as auditing changes using Couchbase as storage backend.

This example has 1 Flamingock Changes:
1. Initialize an index in a bucket called *bucket*. With Rollback that drops that bucket.
```java
    @RollbackExecution
    public void rollbackExecution(Cluster cluster) {
       cluster.queryIndexes().dropIndex("bucket", "idx_standalone_index", DropQueryIndexOptions.dropQueryIndexOptions().ignoreIfNotExists(true));
    }
```

## Table of Contents

1. [Dependencies](#dependencies)
2. [How to run this example](#how-to-run-this-example)
3. [Proven functionalities](#proven-functionalities)

---

## Dependencies

This example requires the following dependencies:
### Flamingock dependencies
    implementation(platform("io.flamingock:flamingock-ce-bom:$flamingockVersion"))
    implementation("io.flamingock:flamingock-ce-couchbase")
    annotationProcessor("io.flamingock:flamingock-processor")

### Couchbase dependency
    implementation("com.couchbase.client:java-client:$couchbaseVersion")

## How to run this example

There are two ways to run this example:

### 1. Running tests (Recomended)
The recommended method to run this example is by executing the tests, which include a Couchbase TestContainer for testing
purposes.
```shell
./gradlew test
```

### 2. Running the main class
To run the main class, ensure you have Couchbase running. Configure the Couchbase client with your settings:

1. Open the main class file
2. Change the Couchbase endpoint with your own:
```java
private static Cluster connect() {
   return Cluster.connect("couchbase://localhost:11210", // Set your Couchbase endpoint
           "Administrator", // Set your Couchbase username
           "password"); // Set your Couchbase password
}
```
3. Run the example:
```shell
./gradlew run
```

## Proven functionalities

This example demonstrates the following functionalities:
1. Auditing Changes with Couchbase
   - Demonstrates how to audit changes using Couchbase as the storage backend.

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
