package com.yourapp;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.flamingock.api.annotations.EnableFlamingock;
import io.flamingock.api.annotations.Stage;
import io.flamingock.community.Flamingock;

import static io.flamingock.api.StageType.LEGACY;


@EnableFlamingock(
        systemStage = "com.yourapp.flamingock.system",
        stages = {
                @Stage(name = "legacy-stage", type = LEGACY, location = "com.yourapp.mongock"),
                @Stage(name = "New MongoDB changes", location = "com.yourapp.flamingock.mongodb")
        }
)
public class YourApplication {

    public static void main(String[] args) {
        MongoClientSettings build = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/")).build();

        try (MongoClient mongoClient = MongoClients.create(build)) {
            Flamingock.builder()
                    .addDependency(mongoClient)
                    .addDependency(mongoClient.getDatabase("test"))
                    .build()
                    .run();
        }

    }
}

