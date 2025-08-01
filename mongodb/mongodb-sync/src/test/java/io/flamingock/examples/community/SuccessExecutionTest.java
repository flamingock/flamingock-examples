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

package io.flamingock.examples.community;

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

import static io.flamingock.examples.community.CommunityStandaloneMongodbSyncApp.DATABASE_NAME;
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
                .getCollection("flamingockAuditLogs")
                .find()
                .into(new ArrayList<>());

        Document aCreateCollection = flamingockDocuments.get(0);
        assertEquals("create-collection", aCreateCollection.get("changeId"));
        assertEquals("EXECUTED", aCreateCollection.get("state"));
        assertEquals("io.flamingock.examples.community.changes.ACreateCollection", aCreateCollection.get("changeUnitClass"));

        Document bInsertDocument = flamingockDocuments.get(1);
        assertEquals("insert-document", bInsertDocument.get("changeId"));
        assertEquals("EXECUTED", bInsertDocument.get("state"));
        assertEquals("io.flamingock.examples.community.changes.BInsertDocument", bInsertDocument.get("changeUnitClass"));

        Document cInsertAnotherDocument = flamingockDocuments.get(2);
        assertEquals("insert-another-document", cInsertAnotherDocument.get("changeId"));
        assertEquals("EXECUTED", cInsertAnotherDocument.get("state"));
        assertEquals("io.flamingock.examples.community.changes.CInsertAnotherDocument", cInsertAnotherDocument.get("changeUnitClass"));

        assertEquals(3, flamingockDocuments.size());
    }

}
