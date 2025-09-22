/*
 * Copyright 2025 Flamingock (https://www.flamingock.io)
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

package io.flamingock.examples.mongodb.springboot.springdata.config;

import com.mongodb.client.MongoClient;
import io.flamingock.community.mongodb.sync.driver.MongoDBSyncAuditStore;
import io.flamingock.internal.core.store.CommunityAuditStore;
import io.flamingock.targetsystem.mongodb.springdata.MongoDBSpringDataTargetSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;

@Configuration
public class MongoConfig {

    public final static String DATABASE_NAME = "test";

    @Bean
    @Primary
    public WriteConcern writeConcern() {
        return WriteConcern.MAJORITY.withJournal(true);
    }

    @Bean
    @Primary
    public ReadConcern readConcern() {
        return ReadConcern.MAJORITY;
    }

    @Bean
    @Primary
    public ReadPreference readPreference() {
        return ReadPreference.primary();
    }

    @Bean
    @Primary
    public MongoDBSpringDataTargetSystem mongoDBSpringDataTargetSystem(MongoTemplate mongoTemplate,
                                                                     WriteConcern writeConcern,
                                                                     ReadConcern readConcern,
                                                                     ReadPreference readPreference) {
        return new MongoDBSpringDataTargetSystem("mongodb-springdata-target-system", mongoTemplate)
                .withWriteConcern(writeConcern)
                .withReadConcern(readConcern)
                .withReadPreference(readPreference);
    }

    @Bean
    @Primary
    public CommunityAuditStore auditStore(MongoClient mongoClient,
                                          WriteConcern writeConcern,
                                          ReadConcern readConcern,
                                          ReadPreference readPreference) {
        return new MongoDBSyncAuditStore(mongoClient, DATABASE_NAME)
                .withWriteConcern(writeConcern)
                .withReadConcern(readConcern)
                .withReadPreference(readPreference);
    }
}
