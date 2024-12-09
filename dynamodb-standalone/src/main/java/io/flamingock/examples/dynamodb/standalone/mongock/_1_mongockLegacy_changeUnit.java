package io.flamingock.examples.dynamodb.standalone.mongock;

import io.mongock.api.annotations.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@ChangeUnit(id = "mongock-initialise-table-legacy", order = "1", author = "mongock_author")
public class _1_mongockLegacy_changeUnit {

    @BeforeExecution
    public void beforeExecution(DynamoDbClient client) {
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(DynamoDbClient client) {
    }

    @Execution
    public void execution(DynamoDbClient client) {
    }

    @RollbackExecution
    public void rollbackExecution(DynamoDbClient client) {
    }
}
