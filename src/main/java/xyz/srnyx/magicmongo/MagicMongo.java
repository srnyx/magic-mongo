package xyz.srnyx.magicmongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A class for managing Mongo using {@link MagicDatabase MagicDatabases} and {@link MagicCollection MagicCollections}
 *
 * @see SingleMongo
 * @see MultiMongo
 */
public class MagicMongo {
    @NotNull protected final ConnectionString connection;
    @NotNull public final MongoClient client;
    @Nullable private final CodecRegistry codecRegistry;

    /**
     * Creates a new {@link MagicMongo} instance
     *
     * @param   connectionUrl   the connection URL for the MongoDB database
     * @param   codecRegistry   the {@link CodecRegistry} to use for the MongoDB database
     */
    public MagicMongo(@NotNull String connectionUrl, @Nullable CodecRegistry codecRegistry) {
        this.connection = new ConnectionString(connectionUrl);
        client = MongoClients.create(connection);
        this.codecRegistry = codecRegistry;
    }

    /**
     * Creates a new {@link MagicMongo} instance with the default {@link CodecRegistry}
     *
     * @param   connectionUrl   the connection URL for the MongoDB database
     */
    public MagicMongo(@NotNull String connectionUrl) {
        this(connectionUrl, CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())));
    }

    /**
     * Constructs a new {@link MagicDatabase} with the specified name
     *
     * @param   name    the name of the database to create
     *
     * @return          the new {@link MagicDatabase} instance
     */
    @NotNull
    public MagicDatabase newMagicDatabase(@NotNull String name) {
        final MongoDatabase database = client.getDatabase(name);
        if (codecRegistry != null) database.withCodecRegistry(codecRegistry);
        return new MagicDatabase(database);
    }
}
