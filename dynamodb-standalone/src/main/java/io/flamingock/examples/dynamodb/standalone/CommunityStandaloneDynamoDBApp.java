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

import io.flamingock.core.configurator.core.CoreConfiguration;
import io.flamingock.core.configurator.standalone.FlamingockStandalone;
import io.flamingock.core.pipeline.Stage;
import io.flamingock.examples.dynamodb.standalone.events.FailureEventListener;
import io.flamingock.examples.dynamodb.standalone.events.StartedEventListener;
import io.flamingock.examples.dynamodb.standalone.events.SuccessEventListener;
import io.flamingock.examples.dynamodb.standalone.mongock.MongockLegacyDataProvisioner;
import io.flamingock.oss.driver.dynamodb.driver.DynamoDBDriver;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URISyntaxException;

public class CommunityStandaloneDynamoDBApp {

    public static void main(String[] args) throws URISyntaxException {
        new CommunityStandaloneDynamoDBApp().run(DynamoDBUtil.getClient());
    }

    public void run(DynamoDbClient client) throws URISyntaxException {
        MongockLegacyDataProvisioner.provisionLegacyMongockData();
        FlamingockStandalone.local()
                .setDriver(new DynamoDBDriver(client))
                .setMongockImporterConfiguration(CoreConfiguration.MongockImporterConfiguration.withSource("mongockChangeLog"))
                .setLockAcquiredForMillis(60 * 1000L)// this is just to show how is set. Default value is still 60 * 1000L
                .setLockQuitTryingAfterMillis(3 * 60 * 1000L)// this is just to show how is set. Default value is still 3 * 60 * 1000L
                .setLockTryFrequencyMillis(1000L)// this is just to show how is set. Default value is still 1000L
                .addStage(new Stage("stage-name").addCodePackage("io.flamingock.examples.dynamodb.standalone.changes"))
                .addDependency(client)
                .setTrackIgnored(true)
                .setTransactionEnabled(true)
                .setPipelineStartedListener(new StartedEventListener())
                .setPipelineCompletedListener(new SuccessEventListener())
                .setPipelineFailedListener(new FailureEventListener())
                .build()
                .run();
    }
}