package io.flamingock;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.flamingock.api.annotations.EnableFlamingock;
import io.flamingock.api.annotations.Stage;
import io.flamingock.community.Flamingock;
import io.flamingock.internal.core.runner.Runner;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@EnableFlamingock(
    stages = {
        @Stage(name = "mongodb-initialisation", location = "io.flamingock.changes")
    }
)
public class App {

    public static void main(String[] args) {
        getRunner().execute();
    }

    public final static String MONGODB_CONNECTION_STRING = "mongodb://localhost:27017/";
    public final static String MONGODB_DB_NAME = "test";


    private static Runner getRunner() {
         MongoClient mongoClient = getMongoClient();
         return Flamingock.builder()
                 .addDependency(mongoClient)
                 .addDependency(mongoClient.getDatabase(MONGODB_DB_NAME))
                 .setLockAcquiredForMillis(60 * 1000L)//this is just to show how is set. Default value is still 60 * 1000L
                 .setLockQuitTryingAfterMillis(3 * 60 * 1000L)//this is just to show how is set. Default value is still 3 * 60 * 1000L
                 .setLockTryFrequencyMillis(1000L)//this is just to show how is set. Default value is still 1000L
                 .addDependency(mongoClient.getDatabase(MONGODB_DB_NAME))
                 .build();
    }

    private static MongoClient getMongoClient() {
        return buildMongoClientWithCodecs(MONGODB_CONNECTION_STRING);
    }

    private static MongoClient buildMongoClientWithCodecs(String connectionString) {

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