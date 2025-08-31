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

import io.flamingock.community.dynamodb.driver.DynamoDBAuditStore;
import io.flamingock.targetsystem.dynamodb.DynamoDBTargetSystem;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import io.flamingock.api.annotations.EnableFlamingock;
import io.flamingock.api.annotations.Stage;
import io.flamingock.community.Flamingock;

import java.net.URISyntaxException;

@EnableFlamingock(
    stages = {
        @Stage(name = "dynamodb-initialisation", location = "io.flamingock.examples.dynamodb.standalone.changes")
    }
)
public class CommunityStandaloneDynamoDBApp {

    public static void main(String[] args) throws URISyntaxException {
        new CommunityStandaloneDynamoDBApp().run(DynamoDBUtil.getClient());
    }

    public void run(DynamoDbClient client) throws URISyntaxException {

        DynamoDBTargetSystem dynamoDbTargetSystem = new DynamoDBTargetSystem("dynamodb-target-system").withDynamoDBClient(DynamoDBUtil.getClient());

        //Running flamingock
        Flamingock.builder()
                .addDependency(client)
                .addTargetSystem(dynamoDbTargetSystem)
                .setAuditStore(new DynamoDBAuditStore())
                .build()
                .run();
    }
}
