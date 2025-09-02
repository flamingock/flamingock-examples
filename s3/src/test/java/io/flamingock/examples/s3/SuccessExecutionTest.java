package io.flamingock.examples.s3;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SuccessExecutionTest {

    private static final String BUCKET_NAME = "flamingock-test-bucket";
    private static final String DEFAULT_AUDIT_STORE_TABLE_NAME = "flamingockAuditLogs";
    private static LocalStackContainer localstack;
    private S3Client s3Client;
    private DynamoDbClient dynamoDbClient;

    @BeforeAll
    void setUp() throws Exception {
        localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.0.2"))
                .withServices(LocalStackContainer.Service.S3, LocalStackContainer.Service.DYNAMODB);
        localstack.start();

        s3Client = S3Client.builder()
                .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.S3))
                .region(Region.of(localstack.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .build();

        dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .region(Region.of(localstack.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .build();

        new S3FlamingockExample().run(s3Client, dynamoDbClient);
    }

    @AfterAll
    void tearDown() {
        if (localstack != null) {
            localstack.stop();
        }
    }

    @Test
    @DisplayName("SHOULD create S3 bucket and insert Flamingock audit logs")
    void testS3BucketAndAuditLogs() {
        // Assert S3 bucket exists
        ListBucketsResponse bucketsResponse = s3Client.listBuckets();
        List<String> bucketNames = bucketsResponse.buckets().stream()
                .map(Bucket::name)
                .toList();
        assertTrue(bucketNames.contains(BUCKET_NAME), "Bucket should be created");

        // Assert Flamingock audit logs
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(DEFAULT_AUDIT_STORE_TABLE_NAME)
                .build();
        ScanResponse response = dynamoDbClient.scan(scanRequest);

        List<Map<String, AttributeValue>> items = response.items();
        List<String> changeIds = items.stream()
                .map(item -> item.get("changeId"))
                .filter(Objects::nonNull)
                .map(AttributeValue::s)
                .toList();

        assertTrue(changeIds.contains("create-bucket"), "Audit log for bucket creation should exist");

        items.stream()
                .map(item -> item.get("state").s())
                .forEach(state -> assertTrue(
                        state.equals("STARTED") || state.equals("EXECUTED"),
                        "State should be STARTED or EXECUTED but was: " + state
                ));

        List<String> classes = items.stream()
                .map(item -> item.get("changeUnitClass"))
                .filter(Objects::nonNull)
                .map(AttributeValue::s)
                .toList();
        assertTrue(classes.contains("io.flamingock.examples.s3.changes._0001_CreateS3BucketChange"));

        assertFalse(items.isEmpty(), "Audit log should not be empty");
    }
}
