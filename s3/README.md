![Header Image](../misc/logo-with-text.png)
___

# AWS S3 Example

## Example Overview

Welcome to the AWS S3 Example. This demonstrates how to use Flamingock with AWS S3 to manage infrastructure as code.
It highlights key functionalities such as using ChangeUnits to configure a S3 bucket in a version-controlled, auditable way.

This example has 1 Flamingock Changes:
1. Creates a new bucked called `flamingock-test-bucket`.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Dependencies](#dependencies)
3. [How to run this example](#how-to-run-this-example)
4. [Proven functionalities](#proven-functionalities)

> **Note**: Flamingock requires a backend to maintain its metadata. This example uses the Flamingock CE with DynamoDB as backend.

## Prerequisites

- Docker (for running LocalStack)
- LocalStack (for local AWS service emulation)

## Dependencies

This example requires the following dependencies:
### Flamingock dependencies
    implementation(platform("io.flamingock:flamingock-ce-bom:$flamingockVersion"))
    implementation("io.flamingock:flamingock-ce-dynamodb")
    annotationProcessor("io.flamingock:flamingock-processor")

### AWS SDK dependencies
    implementation("software.amazon.awssdk:s3:$awsSdkVersion")
    implementation("software.amazon.awssdk:apache-client:${awsSdkVersion}")
    implementation("software.amazon.awssdk:dynamodb:$awsSdkVersion")
    implementation("software.amazon.awssdk:dynamodb-enhanced:$awsSdkVersion")

## How to run this example

### 1. Start localStack

First, start a LocalStack container to emulate AWS services locally:
```bash
docker run -d -p 4566:4566 -p 8000:8000 --name flamingock-localstack localstack/localstack
```
You can verify LocalStack is running properly with:
```bash
docker logs flamingock-localstack
```

### 2. Run the example
```bash
./gradlew run
```

### 3. Validating the results

After running the example, you can verify that the S3 bucket was successfully created by checking the LocalStack endpoint:
   ```bash
   curl http://localhost:4566/flamingock-test-bucket
   ```
You should see the following XML response, confirming that the bucket exists:
   ```xml
   <ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
     <IsTruncated>false</IsTruncated>
     <Marker/>
     <Name>flamingock-test-bucket</Name>
     <Prefix/>
     <MaxKeys>1000</MaxKeys>
   </ListBucketResult>
   ```

## Proven functionalities

This example demonstrates the following functionalities:
1. Configure an external piece of infrastructure
   - Demonstrates how to audit changes configuring an external piece of your infrastructure.

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
