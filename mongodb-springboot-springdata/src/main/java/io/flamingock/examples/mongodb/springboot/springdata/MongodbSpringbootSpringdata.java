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

package io.flamingock.examples.mongodb.springboot.springdata;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.flamingock.examples.mongodb.springboot.springdata.client.ClientRepository;
import io.flamingock.examples.mongodb.springboot.springdata.events.PipelineCompletedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.PipelineFailedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.PipelineStartedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.StageCompletedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.StageFailedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.StageStartedListener;
import io.flamingock.examples.mongodb.springboot.springdata.mongock.MongockExecutor;
import io.flamingock.examples.mongodb.springboot.springdata.mongock.MongockLegacyChangeUnit;
import io.flamingock.springboot.v2.context.EnableFlamingock;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.runner.standalone.MongockStandalone;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


@EnableFlamingock
@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
public class MongodbSpringbootSpringdata {

    public final static String DATABASE_NAME = "test";
    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
        MongockExecutor.addMongockLegacyData("mongodb://localhost:27017/", DATABASE_NAME);
        SpringApplication.run(MongodbSpringbootSpringdata.class, args);
    }

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