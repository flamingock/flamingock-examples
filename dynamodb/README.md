![Header Image](../misc/logo-with-text.png)
___

# DynamoDB Example

## 📖 Example overview

Welcome to the DynamoDB Example. This demonstrates how to use Flamingock with DynamoDB in a Java
standalone application. It highlights key functionalities such as auditing changes using DynamoDB as storage backend.

This example has 3 Flamingock Changes:
1. Creates a new table called *test_table* in DynamoDB.
2. Adds one entry to table.
3. Adds another entry to table.

## Table of Contents

1. [📌 Dependencies](#-dependencies)
2. [🛠 How to run this example](#-how-to-run-this-example)
3. [✅ Proven functionalities](#-proven-functionalities)

## 📌 Dependencies

This example requires the following dependencies:
### Flamingock dependencies
    implementation(platform("io.flamingock:flamingock-ce-bom:$flamingockVersion"))
    implementation("io.flamingock:flamingock-ce-dynamodb")
    annotationProcessor("io.flamingock:flamingock-processor")

### DynamoDB dependencies
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.25.28")
    implementation("software.amazon.awssdk:url-connection-client:2.24.11")

## 🛠 How to run this example

There are two ways to run this example:

### 1. Running tests (Recomended)
The recommended method to run this example is by executing the tests, which include a DynamoDB Local Service for testing
purposes.
```shell
./gradlew test
```

### 2. Running the main class
For this option, you'll need to configure your own DynamoDB connection:
1. Open the main class file
2. Configure the DynamoDB client builder with your settings in DynamoDBUtil class:
```java
    public static DynamoDbClient getClient() throws URISyntaxException {
   return DynamoDbClient.builder()
           .region(Region.EU_WEST_1) // Set your AWS region
           .endpointOverride(new URI("http://localhost:8000")) // Set your DynamoDB endpoint
           .credentialsProvider(
                   StaticCredentialsProvider.create(
                           AwsBasicCredentials.create("dummye", "dummye") // Set your AWS credentials
                   )
           )
           .build();
}
```
3. Run the example:
```shell
./gradlew run
```

## ✅ Proven functionalities

This example demonstrates the following functionalities:
1. Auditing Changes with DynamoDB
   - Demonstrates how to audit changes using DynamoDB as the storage backend.

___

### 📢 Contributing
We welcome contributions! If you have an idea for a new example or improvement to an existing one, feel free to submit a
pull request. Check out our [CONTRIBUTING.md](../CONTRIBUTING.md) for guidelines.

___

### 🤝 Get involved
⭐ Star the [Flamingock repository](https://github.com/mongock/flamingock-project) to show your support!

🐞 Report issues or suggest features in the [Flamingock issue tracker](https://github.com/mongock/flamingock-project/issues).

💬 Join the discussion in the [Flamingock community](https://github.com/mongock/flamingock-project/discussions).

___

### 📜 License
This repository is licensed under the [Apache License 2.0](../LICENSE.md).

___

### 🔥 Explore, experiment, and empower your projects with Flamingock!
Let us know what you think or where you’d like to see Flamingock used next.
