# Flamingock S3 Example

This example demonstrates how to use Flamingock with AWS S3 to manage infrastructure as code. It shows how to create a change unit that creates an S3 bucket in a version-controlled, auditable way.

> **Note**: Flamingock requires an audit repository to maintain its metadata. This example uses the Community compatible edition.

## Prerequisites

- Java 8 or higher
- Gradle 7.x+ or Maven 3.6+
- Docker (for running LocalStack)
- LocalStack (for local AWS service emulation)
- 
## What This Example Does

This example demonstrates how to use Flamingock to manage changes to AWS S3 infrastructure. It includes:

1. A change unit (`_0001_CreateS3BucketChange.java`) with:
    - An `@Execution` method that creates a bucket named "flamingock-test-bucket"
    - A `@RollbackExecution` method that deletes the bucket if execution fails

2. Utility classes:
    - `S3Util.java`: Configures the AWS S3 client with LocalStack endpoints
    - `DynamoDBUtil.java`: Configures the DynamoDB client for Flamingock's audit repository

3. Main application class:
    - `S3FlamingockExample.java`: Initializes Flamingock and executes the change units


## Running the Example

### 1. Start LocalStack

First, start a LocalStack container to emulate AWS services locally:
```bash
docker run -d -p 4566:4566 -p 8000:8000 --name flamingock-localstack localstack/localstack
```
You can verify LocalStack is running properly with:
```bash
docker logs flamingock-localstack
```

### 2. Run the Example

- Execute the example using **Gradle**:
```bash
./gradlew run
```

## Validating the Results

After running the example, you can verify that the S3 bucket was successfully created by checking the LocalStack endpoint:

1. Access the bucket information by visiting:
   ```bash
   curl http://localhost:4566/flamingock-test-bucket 
   ```

2. You should see the following XML response, confirming that the bucket exists:
   ```xml
   <ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01/">
     <IsTruncated>false</IsTruncated>
     <Marker/>
     <Name>flamingock-test-bucket</Name>
     <Prefix/>
     <MaxKeys>1000</MaxKeys>
   </ListBucketResult>
   ```

## Troubleshooting

- **LocalStack Connection Issues**: Make sure LocalStack is running and accessible at localhost:4566
- **AWS Credential Errors**: The example uses dummy credentials for LocalStack, which is expected
- **Bucket Already Exists**: If you run the example multiple times, you may see a warning that the bucket already exists

## Additional Resources

- [Flamingock Documentation](https://docs.flamingock.io)
- [AWS S3 SDK Documentation](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-s3.html)
- [LocalStack Documentation](https://docs.localstack.cloud)