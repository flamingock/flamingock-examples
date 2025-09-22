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

package io.flamingock.examples.community.couchbase;

import com.couchbase.client.core.io.CollectionIdentifier;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.manager.query.QueryIndex;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.query.QueryScanConsistency;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class SuccessExecutionTest {

    private static final String BUCKET_NAME = "bucket";
    private static final String AUDIT_STORE_SCOPE_NAME = CollectionIdentifier.DEFAULT_SCOPE;
    private static final String AUDIT_STORE_COLLECTION_NAME = "flamingockAuditLogs";

    @Container
    public static final CouchbaseContainer couchbaseContainer = new CouchbaseContainer("couchbase/server:7.2.4")
            .withBucket(new BucketDefinition(BUCKET_NAME));

    private static Cluster cluster;

    @BeforeAll
    static void beforeAll() {
        couchbaseContainer.start();
        cluster = Cluster.connect(
                couchbaseContainer.getConnectionString(),
                couchbaseContainer.getUsername(),
                couchbaseContainer.getPassword());
        cluster.bucket(BUCKET_NAME).waitUntilReady(Duration.ofSeconds(10));
            new CommunityStandaloneCouchbaseApp().run(cluster, BUCKET_NAME);
    }

    @Test
    @DisplayName("SHOULD create idx_standalone_index index")
    void functionalTest() {
        List<QueryIndex> indexes = cluster
                .queryIndexes()
                .getAllIndexes(BUCKET_NAME)
                .stream()
                .filter(i -> i.name().equals("idx_standalone_index"))
                .toList();

        assertEquals(1, indexes.size());
    }

    @Test
    @DisplayName("SHOULD insert the Flamingock change history")
    void flamingockLogsTest() {
        QueryResult result = cluster.query(
                String.format("SELECT `%s`.* FROM `%s`.`%s`.`%s`", AUDIT_STORE_COLLECTION_NAME, BUCKET_NAME, AUDIT_STORE_SCOPE_NAME, AUDIT_STORE_COLLECTION_NAME),
                QueryOptions.queryOptions()
                        .scanConsistency(QueryScanConsistency.REQUEST_PLUS));

        List<JsonObject> flamingockDocuments = result
                .rowsAsObject()
                .stream()
                .sorted(Comparator.comparing(o -> o.getLong("timestamp")))
                .toList();

        assertEquals(1, flamingockDocuments.size());

        JsonObject executionEntry = flamingockDocuments.get(0);
        assertEquals("index-initializer", executionEntry.get("changeId"));
        assertEquals("APPLIED", executionEntry.get("state"));
        assertEquals("io.flamingock.examples.community.couchbase.changes._0001_IndexInitializerChange", executionEntry.get("changeUnitClass"));
    }
}
