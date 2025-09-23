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

package io.flamingock.examples.mongodb.standalone;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static io.flamingock.examples.mongodb.standalone.CommunityStandaloneMongodbSyncApp.DATABASE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class SuccessExecutionTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6"));

    private static MongoClient mongoClient;

    @BeforeAll
    static void beforeAll() {
        mongoClient = MongoClients.create(MongoClientSettings
                .builder()
                .applyConnectionString(new ConnectionString(mongoDBContainer.getConnectionString()))
                .build());
        new CommunityStandaloneMongodbSyncApp().run(mongoClient, "test");
    }


    @Test
    @DisplayName("SHOULD create clientCollection and insert two clients")
    void functionalTest() {
        Set<String> clients = mongoClient.getDatabase(DATABASE_NAME)
                .getCollection("clientCollection")
                .find()
                .map(document -> document.getString("name"))
                .into(new HashSet<>());

        assertTrue(clients.contains("Jorge"));
        assertTrue(clients.contains("Federico"));
        assertEquals(2, clients.size());
    }

    @Test
    @DisplayName("SHOULD insert the Flamingock change history")
    void flamingockLogsTest() {
        ArrayList<Document> flamingockDocuments = mongoClient.getDatabase(DATABASE_NAME)
                .getCollection("flamingockAuditLog")
                .find()
                .into(new ArrayList<>());

        assertEquals(6, flamingockDocuments.size());

        verifyChangeExecution(flamingockDocuments, 0, 1,
                "create-collection", "io.flamingock.examples.mongodb.standalone.changes._0001_CreateCollectionChange");

        verifyChangeExecution(flamingockDocuments, 2, 3,
                "insert-document", "io.flamingock.examples.mongodb.standalone.changes._0002_InsertDocumentChange");

        verifyChangeExecution(flamingockDocuments, 4, 5,
                "insert-another-document", "io.flamingock.examples.mongodb.standalone.changes._0003_InsertAnotherDocumentChange");
    }

    private void verifyChangeExecution(ArrayList<Document> documents, int startedIndex, int executedIndex,
                                       String expectedChangeId, String expectedChangeUnitClass) {
        Document startedDocument = documents.get(startedIndex);
        assertEquals(expectedChangeId, startedDocument.get("changeId"));
        assertEquals("STARTED", startedDocument.get("state"));
        assertEquals(expectedChangeUnitClass, startedDocument.get("changeClass"));

        Document executedDocument = documents.get(executedIndex);
        assertEquals(expectedChangeId, executedDocument.get("changeId"));
        assertEquals("APPLIED", executedDocument.get("state"));
        assertEquals(expectedChangeUnitClass, executedDocument.get("changeClass"));
    }

}
