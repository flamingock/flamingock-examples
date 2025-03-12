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
import io.flamingock.examples.dynamodb.standalone.mongock.MongockLegacyDataProvisioner;
import io.flamingock.oss.driver.dynamodb.driver.DynamoDBDriver;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.net.URISyntaxException;

public class CommunityStandaloneDynamoDBApp {

    public static void main(String[] args) throws URISyntaxException {
        new CommunityStandaloneDynamoDBApp().run(DynamoDBUtil.getClient());
    }

    public void run(DynamoDbClient client) {
        //This line adds data to simulate previous legacy Mongock executions
        MongockLegacyDataProvisioner.provisionLegacyMongockData();

        //Running flamingock
        FlamingockStandalone.local()
                .setDriver(new DynamoDBDriver(client))
                .setMongockImporterConfiguration(CoreConfiguration.MongockImporterConfiguration.withSource("mongockChangeLog"))
                .addStage(new Stage("stage-name")
                        //adding mongock legacy changeUnits
                        .addCodePackage("io.flamingock.examples.dynamodb.standalone.mongock")
                        //Adding a package for new changeUnits (note that legacy and new changeUnits can coexist in the same package)
                        .addCodePackage("io.flamingock.examples.dynamodb.standalone.changes"))
                .addDependency(client)
                .setTransactionEnabled(true)
                .build()
                .run();
    }
}