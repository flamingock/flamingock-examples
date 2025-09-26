/*
 * Copyright 2023 Flamingock (https://www.flamingock.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.flamingock.examples.dynamodb.standalone.changes;

import io.flamingock.api.annotations.Apply;
import io.flamingock.api.annotations.Change;
import io.flamingock.api.annotations.TargetSystem;
import io.flamingock.examples.dynamodb.standalone.entity.UserEntity;
import io.flamingock.examples.dynamodb.standalone.util.DynamoDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;

import static java.util.Collections.emptyList;

@Change(id = "create-user-table", author = "flamingock-team", transactional = false)
@TargetSystem(id = "dynamodb-target-system")
public class _0001__CreateUserTableChange {

    private DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<UserEntity> table;

    @Apply
    public void apply(DynamoDbClient client) {

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
}
