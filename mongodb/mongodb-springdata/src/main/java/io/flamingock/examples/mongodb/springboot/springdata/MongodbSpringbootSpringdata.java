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

package io.flamingock.examples.mongodb.springboot.springdata;

import io.flamingock.api.annotations.EnableFlamingock;
import io.flamingock.api.annotations.Stage;
import io.flamingock.examples.mongodb.springboot.springdata.client.ClientRepository;
import io.flamingock.examples.mongodb.springboot.springdata.config.MongoConfig;
import io.flamingock.examples.mongodb.springboot.springdata.events.PipelineCompletedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.PipelineFailedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.PipelineStartedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.StageCompletedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.StageFailedListener;
import io.flamingock.examples.mongodb.springboot.springdata.events.StageStartedListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


//Set Flamingock On
@EnableFlamingock(
    stages = {
        @Stage(name = "mongodb-initialisation", location = "io.flamingock.examples.mongodb.springboot.springdata.changes")
    }
)
@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = ClientRepository.class)
@Import(MongoConfig.class)
public class MongodbSpringbootSpringdata {

    public final static String CLIENTS_COLLECTION_NAME = "clientCollection";

    public static void main(String[] args) {
//        Run SpringApplication
        SpringApplication.run(MongodbSpringbootSpringdata.class, args);
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