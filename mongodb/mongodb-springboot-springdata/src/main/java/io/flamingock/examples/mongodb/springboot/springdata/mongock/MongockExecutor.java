package io.flamingock.examples.mongodb.springboot.springdata.mongock;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.flamingock.examples.mongodb.springboot.springdata.ZonedDateTimeCodec;
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.runner.standalone.MongockStandalone;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public final class MongockExecutor {

    private MongockExecutor(){}

    public static void addMongockLegacyData(String mongoUrl, String database) {
        MongoClient mongoClient = getMongoClient(mongoUrl);
        MongoSync4Driver mongockSync4Driver = io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver
                .withDefaultLock(mongoClient, database);

        MongockStandalone.builder()
                .setDriver(mongockSync4Driver)
                .addMigrationClass(MongockLegacyChangeUnit.class)
                .setTrackIgnored(true)
                .setTransactional(true)
                .buildRunner()
                .execute();
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
}
