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
 
package com.yourapp;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.runner.standalone.MongockStandalone;

public class YourApplication {

    public static void main(String[] args) {
        MongoClientSettings build = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/")).build();

        try(MongoClient mongoClient = MongoClients.create(build)) {
            MongockStandalone.builder()
                    .setDriver(MongoSync4Driver.withDefaultLock(mongoClient, "test"))
                    .addMigrationScanPackage("com.yourapp.mongock")
                    .buildRunner()
                    .execute();
        }

    }

}
