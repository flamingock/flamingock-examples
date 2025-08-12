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

package io.flamingock.examples.mongodb.springboot.builder;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.flamingock.api.SetupType;
import io.flamingock.api.annotations.EnableFlamingock;
import io.flamingock.api.annotations.Stage;
import io.flamingock.api.targets.TargetSystem;
import io.flamingock.community.Flamingock;
import io.flamingock.examples.mongodb.springboot.builder.client.ClientRepository;
import io.flamingock.examples.mongodb.springboot.builder.events.*;
import io.flamingock.springboot.SpringbootUtil;
import io.flamingock.targetystem.mongodb.sync.MongoSyncTargetSystem;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


//Set Flamingock On
@EnableFlamingock(
    setup = SetupType.BUILDER,
    stages = {
        @Stage(name = "mongodb-initialisation", location = "io.flamingock.examples.mongodb.springboot.builder.changes")
    }
)
@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
public class SpringbootWithFlamingockBuilder {

    public final static String DATABASE_NAME = "test";
    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        SpringApplication.run(SpringbootWithFlamingockBuilder.class, args);
    }

    @Bean
    public ApplicationRunner flamingockRunner(
        ApplicationContext applicationContext,
        ApplicationEventPublisher applicationEventPublisher
    ) {
        MongoClient mongoClient = getMongoClient("mongodb://localhost:27017/");
        MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);

        TargetSystem mongoDBTargetSystem = new MongoSyncTargetSystem("mongodb-ts")
            .withMongoClient(mongoClient)
            .withDatabase(mongoDatabase);

        return SpringbootUtil.toApplicationRunner(
            Flamingock.builder()
                .addDependency(applicationContext)
                .addDependency(applicationEventPublisher)
                .addTargetSystem(mongoDBTargetSystem)
                .build()
        );
    }

    private static MongoClient getMongoClient(String connectionString) {

        CodecRegistry codecRegistry = fromRegistries(CodecRegistries.fromCodecs(new ZonedDateTimeCodec()),
            MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        builder.applyConnectionString(new ConnectionString(connectionString));
        builder.codecRegistry(codecRegistry);
        MongoClientSettings build = builder.build();
        return MongoClients.create(build);
    }

    //    Configure Listeners beans
    @Bean
    public PipelineStartedListener startFlamingockListener() {
        return new PipelineStartedListener();
    }

    @Bean
    public PipelineCompletedListener successFlamingockListener() {
        return new PipelineCompletedListener();
    }

    @Bean
    public PipelineFailedListener sailedFlamingockListener() {
        return new PipelineFailedListener();
    }

    @Bean
    public StageStartedListener stageStartedListener() {return new StageStartedListener();}

    @Bean
    public StageCompletedListener stageCompletedListener() {return new StageCompletedListener();}

    @Bean
    public StageFailedListener stageFailedListener() {return new StageFailedListener();}

}
