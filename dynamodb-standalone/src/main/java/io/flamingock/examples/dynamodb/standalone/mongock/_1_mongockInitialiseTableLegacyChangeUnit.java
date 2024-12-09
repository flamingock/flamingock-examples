package io.flamingock.examples.dynamodb.standalone.mongock;

import io.flamingock.examples.dynamodb.standalone.DynamoDBUtil;
import io.flamingock.examples.dynamodb.standalone.UserEntity;
import io.mongock.api.annotations.*;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.TransactWriteItemsEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;

import static java.util.Collections.emptyList;


@ChangeUnit(id = "mongock-initialise-table-legacy", order = "1", author = "mongock_author")
public class _1_mongockInitialiseTableLegacyChangeUnit {

    public final static int INITIAL_CLIENTS = 10;

    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<UserEntity> table;

    @BeforeExecution
    public void beforeExecution(DynamoDbClient client) {

        DynamoDBUtil.createTable(
                client,
                DynamoDBUtil.getAttributeDefinitions(UserEntity.pkName, UserEntity.skName),
                DynamoDBUtil.getKeySchemas(UserEntity.pkName, UserEntity.skName),
                DynamoDBUtil.getProvisionedThroughput(UserEntity.readCap, UserEntity.writeCap),
                UserEntity.tableName,
                emptyList(),
                emptyList()
        );
        client.describeTable(
                DescribeTableRequest.builder()
                        .tableName(UserEntity.tableName)
                        .build()
        );

        this.enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();
        this.table = this.enhancedClient.table(UserEntity.tableName, TableSchema.fromBean(UserEntity.class));
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(DynamoDbClient client) {
        client.deleteTable(
                DeleteTableRequest.builder()
                        .tableName(UserEntity.tableName)
                        .build()
        );
    }

    @Execution
    public void execution(DynamoDbClient client) {
        final TransactWriteItemsEnhancedRequest.Builder writeRequestBuilder = TransactWriteItemsEnhancedRequest.builder();

        for (int i = 0; i < INITIAL_CLIENTS; i++)
            writeRequestBuilder.addPutItem(table, new UserEntity("name-" + i, "surname-" + i));

        enhancedClient.transactWriteItems(writeRequestBuilder.build());
    }

    @RollbackExecution
    public void rollbackExecution(DynamoDbClient client) {
        final TransactWriteItemsEnhancedRequest.Builder writeRequestBuilder = TransactWriteItemsEnhancedRequest.builder();

        for (int i = 0; i < INITIAL_CLIENTS; i++)
            writeRequestBuilder.addDeleteItem(table, new UserEntity("name-" + i, "surname-" + i));

        enhancedClient.transactWriteItems(writeRequestBuilder.build());
    }
}
