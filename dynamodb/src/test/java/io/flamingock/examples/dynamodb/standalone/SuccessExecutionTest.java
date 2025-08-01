/*
 * Copyright 2023 Flamingock (https://oss.flamingock.io)
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

package io.flamingock.examples.dynamodb.standalone;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import io.flamingock.community.dynamodb.internal.entities.AuditEntryEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SuccessExecutionTest {

    private static final Logger logger = LoggerFactory.getLogger(SuccessExecutionTest.class);

    private static DynamoDBProxyServer dynamoDBLocal;
    private static DynamoDbClient client;
    private static DynamoDbEnhancedClient enhancedClient;

    @BeforeAll
    static void beforeAll() throws Exception {
        logger.info("Starting DynamoDB Local...");
        dynamoDBLocal = ServerRunner.createServerFromCommandLineArgs(
                new String[]{
                        "-inMemory",
                        "-port",
                        "8000"
                }
        );
        dynamoDBLocal.start();

        client = DynamoDbClient.builder()
                .region(Region.EU_WEST_1)
                .endpointOverride(new URI("http://localhost:8000"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("dummye", "dummye")
                        )
                )
                .build();

        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build();

        new CommunityStandaloneDynamoDBApp().run(client);
    }

    @AfterAll
    static void afterAll() throws Exception {
        logger.info("Stopping DynamoDB Local...");
        dynamoDBLocal.stop();
    }

    @Test
    @DisplayName("SHOULD create 'test_table' table and insert two users")
    void functionalTest() {
        List<String> tables = client.listTables().tableNames();
        assertTrue(tables.contains("test_table"));

        List<String> rows = enhancedClient
                .table("test_table", TableSchema.fromBean(UserEntity.class))
                .scan().items().stream()
                .map(UserEntity::getPartitionKey)
                .toList();

        assertEquals(2, rows.size());
        assertTrue(rows.contains("Pepe Pérez"));
        assertTrue(rows.contains("Pablo López"));
    }

    @Test
    @DisplayName("SHOULD insert the Flamingock change history")
    void flamingockLogsTest() {
        List<AuditEntryEntity> rows = enhancedClient
                .table("flamingockAuditLogs", TableSchema.fromBean(AuditEntryEntity.class))
                .scan().items().stream()
                .toList();

        List<String> taskIds = rows.stream()
                .map(AuditEntryEntity::getTaskId)
                .toList();
        assertTrue(taskIds.contains("table-create"));
        assertTrue(taskIds.contains("insert-user"));
        assertTrue(taskIds.contains("insert-another-user"));

        rows.stream()
                .map(AuditEntryEntity::getState)
                .forEach(state -> assertEquals("EXECUTED", state));

        List<String> classes = rows.stream()
                .map(AuditEntryEntity::getClassName)
                .toList();
        assertTrue(classes.contains("io.flamingock.examples.dynamodb.standalone.changes._0002_createUserTable_changeUnit"));
        assertTrue(classes.contains("io.flamingock.examples.dynamodb.standalone.changes._0003_insertUser_changeUnit"));
        assertTrue(classes.contains("io.flamingock.examples.dynamodb.standalone.changes._0004_insertAnotherUser_changeUnit"));

        assertEquals(3, rows.size());
    }

}
