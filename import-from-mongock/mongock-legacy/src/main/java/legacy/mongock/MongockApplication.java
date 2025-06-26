package legacy.mongock;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.runner.standalone.MongockStandalone;

public class MongockApplication {

    public static void main(String[] args) {
        MongoClientSettings build = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/")).build();

        try(MongoClient mongoClient = MongoClients.create(build)) {
            MongockStandalone.builder()
                    .setDriver(MongoSync4Driver.withDefaultLock(mongoClient, "test"))
                    .addMigrationScanPackage("legacy.mongock.changes")
                    .buildRunner()
                    .execute();
        }

    }

}
