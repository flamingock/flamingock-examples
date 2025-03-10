package io.flamingock.examples.dynamodb.standalone.mongock;

import io.flamingock.examples.dynamodb.standalone.DynamoDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;

public class MongockLegacyDataProvisioner {

    private static final DynamoDbTable<ChangeLog> table;

    static {
        DynamoDbClient dynamoDbClient;
        try {
            dynamoDbClient = DynamoDBUtil.getClient();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Define attribute definitions for the table
        AttributeDefinition changeIdAttribute = AttributeDefinition.builder()
                .attributeName("changeId")
                .attributeType(ScalarAttributeType.S) // String type
                .build();
        AttributeDefinition executionIdAuthorAttribute = AttributeDefinition.builder()
                .attributeName("executionId#author")
                .attributeType(ScalarAttributeType.S) // String type
                .build();

        // Define key schema for the table
        KeySchemaElement changeIdKey = KeySchemaElement.builder()
                .attributeName("changeId")
                .keyType(KeyType.HASH)
                .build();
        KeySchemaElement executionIdAuthorKey = KeySchemaElement.builder()
                .attributeName("executionId#author")
                .keyType(KeyType.RANGE)
                .build();

        // Define provisioned throughput
        ProvisionedThroughput provisionedThroughput = ProvisionedThroughput.builder()
                .readCapacityUnits(50L)
                .writeCapacityUnits(50L)
                .build();

        // Call your createTable method
        DynamoDBUtil.createTable(dynamoDbClient,
                Arrays.asList(changeIdAttribute, executionIdAuthorAttribute),
                Arrays.asList(changeIdKey, executionIdAuthorKey),
                provisionedThroughput,
                "mongockChangeLog",
                Collections.emptyList(), // No local secondary indexes
                Collections.emptyList()  // No global secondary indexes
        );

        table = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build()
                .table("mongockChangeLog", TableSchema.fromBean(ChangeLog.class));
    }

    private MongockLegacyDataProvisioner() {
    }

    public static void provisionLegacyMongockData() {
        insertSystemChangeBefore();
        insertSystemChange();
        insertChangeUnitBefore();
        insertChangeUnit();

        System.out.println("Row added successfully!");
    }

    private static void insertChangeUnit() {
        ChangeLog changeLog = new ChangeLog();
        changeLog.setChangeId("mongock-initialise-table-legacy");
        changeLog.setExecutionIdAuthor("2024-12-09T10:54:59.394284-835#mongock_author");
        changeLog.setSystemChange(false);
        changeLog.setExecutionId("2024-12-09T10:54:59.394284-835");
        changeLog.setChangeSetMethod("execution");
        changeLog.setAuthor("mongock_author");
        changeLog.setChangeLogClass("io.flamingock.examples.dynamodb.standalone.mongock._1_mongockInitialiseTableLegacyChangeUnit");
        changeLog.setExecutionMillis(40);
        changeLog.setState("EXECUTED");
        changeLog.setType("EXECUTION");
        changeLog.setTimestamp(1733741700009L);
        changeLog.setExecutionHostname("Antonios-MacBook-Pro.local");
        table.putItem(changeLog);
    }

    private static void insertChangeUnitBefore() {
        ChangeLog changeLog = new ChangeLog();
        changeLog.setChangeId("mongock-initialise-table-legacy_before");
        changeLog.setExecutionIdAuthor("2024-12-09T09:16:20.696936-26#mongock_author");
        changeLog.setSystemChange(false);
        changeLog.setExecutionId("2024-12-09T09:16:20.696936-26");
        changeLog.setChangeSetMethod("beforeExecution");
        changeLog.setAuthor("mongock_author");
        changeLog.setChangeLogClass("io.flamingock.examples.dynamodb.standalone.mongock._1_mongockInitialiseTableLegacyChangeUnit");
        changeLog.setExecutionMillis(202);
        changeLog.setState("EXECUTED");
        changeLog.setType("BEFORE_EXECUTION");
        changeLog.setTimestamp(1733735781555L);
        changeLog.setExecutionHostname("Antonios-MacBook-Pro.local");
        table.putItem(changeLog);
    }

    private static void insertSystemChange() {
        ChangeLog changeLog = new ChangeLog();
        changeLog.setChangeId("system-change-00001");
        changeLog.setExecutionIdAuthor("2024-12-09T10:54:59.394284-835#mongock");
        changeLog.setSystemChange(true);
        changeLog.setExecutionId("2024-12-09T10:54:59.394284-835");
        changeLog.setChangeSetMethod("execution");
        changeLog.setAuthor("mongock");
        changeLog.setChangeLogClass("io.mongock.runner.core.executor.system.changes.SystemChangeUnit00001");
        changeLog.setExecutionMillis(6);
        changeLog.setState("EXECUTED");
        changeLog.setType("EXECUTION");
        changeLog.setTimestamp(1733741699778L);
        changeLog.setExecutionHostname("Antonios-MacBook-Pro.local");
        table.putItem(changeLog);
    }

    private static void insertSystemChangeBefore() {
        ChangeLog changeLog = new ChangeLog();
        changeLog.setChangeId("system-change-00001_before");
        changeLog.setExecutionIdAuthor("2024-12-09T10:54:59.394284-835#mongock");
        changeLog.setSystemChange(true);
        changeLog.setExecutionId("2024-12-09T10:54:59.394284-835");
        changeLog.setChangeSetMethod("beforeExecution");
        changeLog.setAuthor("mongock");
        changeLog.setChangeLogClass("io.mongock.runner.core.executor.system.changes.SystemChangeUnit00001");
        changeLog.setExecutionMillis(2);
        changeLog.setState("EXECUTED");
        changeLog.setType("BEFORE_EXECUTION");
        changeLog.setTimestamp(1733741699750L);
        changeLog.setExecutionHostname("Antonios-MacBook-Pro.local");
        table.putItem(changeLog);
    }

    @DynamoDbBean
    public static class ChangeLog {
        private String changeId;
        private String executionIdAuthor;
        private boolean systemChange;
        private String executionId;
        private String changeSetMethod;
        private String author;
        private String changeLogClass;
        private int executionMillis;
        private String state;
        private String type;
        private long timestamp;
        private String executionHostname;

        // Partition Key
        @DynamoDbPartitionKey
        @DynamoDbAttribute("changeId")
        public String getChangeId() {
            return changeId;
        }
        public void setChangeId(String changeId) {
            this.changeId = changeId;
        }

        // Sort Key
        @DynamoDbSortKey
        @DynamoDbAttribute("executionId#author")
        public String getExecutionIdAuthor() {
            return executionIdAuthor;
        }
        public void setExecutionIdAuthor(String executionIdAuthor) {
            this.executionIdAuthor = executionIdAuthor;
        }

        public boolean isSystemChange() {
            return systemChange;
        }
        public void setSystemChange(boolean systemChange) {
            this.systemChange = systemChange;
        }

        public String getExecutionId() {
            return executionId;
        }
        public void setExecutionId(String executionId) {
            this.executionId = executionId;
        }

        public String getChangeSetMethod() {
            return changeSetMethod;
        }
        public void setChangeSetMethod(String changeSetMethod) {
            this.changeSetMethod = changeSetMethod;
        }

        public String getAuthor() {
            return author;
        }
        public void setAuthor(String author) {
            this.author = author;
        }

        public String getChangeLogClass() {
            return changeLogClass;
        }
        public void setChangeLogClass(String changeLogClass) {
            this.changeLogClass = changeLogClass;
        }

        public int getExecutionMillis() {
            return executionMillis;
        }
        public void setExecutionMillis(int executionMillis) {
            this.executionMillis = executionMillis;
        }

        public String getState() {
            return state;
        }
        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

        public long getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getExecutionHostname() {
            return executionHostname;
        }
        public void setExecutionHostname(String executionHostname) {
            this.executionHostname = executionHostname;
        }
    }
}