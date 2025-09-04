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

package io.flamingock.examples.mongodb.springboot.springdata.config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.flamingock.community.mongodb.springdata.driver.SpringDataMongoAuditStore;
import io.flamingock.targetsystem.mongodb.springdata.MongoSpringDataTargetSystem;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.List;

@Configuration
@TestConfiguration
public class MongoInitializer  implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:6"));

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        mongoDBContainer.start();
        String replicaSetUrl = mongoDBContainer.getReplicaSetUrl();
        List<String> addedProperties = Collections.singletonList(
                "spring.data.mongodb.uri=" + replicaSetUrl
        );
        TestPropertyValues.of(addedProperties).applyTo(context.getEnvironment());
        String[] urlChunks = replicaSetUrl.split("/");
        String database = urlChunks[urlChunks.length - 1 ];
        String mongoHost = urlChunks[urlChunks.length - 2 ];
    }

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
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        return mongoClient.getDatabase("test");
    }

    @Bean
    @Primary
    public MongoSpringDataTargetSystem mongoSpringDataTargetSystem(MongoTemplate mongoTemplate,
                                                                   WriteConcern writeConcern,
                                                                   ReadConcern readConcern,
                                                                   ReadPreference readPreference) {
        return new MongoSpringDataTargetSystem("mongo-springdata-target-system")
                .withMongoTemplate(mongoTemplate)
                .withWriteConcern(writeConcern)
                .withReadConcern(readConcern)
                .withReadPreference(readPreference);
    }

    @Bean
    @Primary
    public SpringDataMongoAuditStore auditStore() {
        return new SpringDataMongoAuditStore();
    }
}

