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

import java.util.HashMap;
import java.util.Map;


/**
 * A class for managing Mongo using {@link MagicDatabase MagicDatabases} and {@link MagicCollection MagicCollections}
 *
 * @see SingleMongo
 */
public class MagicMongo {
    /**
     * The {@link ConnectionString} for the MongoDB connection
     */
    @NotNull public final ConnectionString connection;
    /**
     * The {@link MongoClient} instance for the MongoDB connection
     */
    @NotNull public final MongoClient client;
    /**
     * The {@link CodecRegistry} to use when creating {@link MagicDatabase MagicDatabases}
     */
    @Nullable private final CodecRegistry codecRegistry;
    /**
     * The {@link MagicDatabase MagicDatabases} that have been loaded
     */
    @NotNull public final Map<String, MagicDatabase> databases = new HashMap<>();

    public MagicMongo(@NotNull String connectionUrl, @Nullable CodecRegistry codecRegistry) {
        connection = new ConnectionString(connectionUrl);
        client = MongoClients.create(connection);
        this.codecRegistry = codecRegistry;

        // Load database if specified
        final String databaseName = connection.getDatabase();
        if (databaseName != null) loadMagicDatabase(databaseName);
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

    /**
     * Constructs a new {@link MagicDatabase} with the specified name and loads it
     *
     * @param   name    the name of the database to create
     *
     * @return          the new {@link MagicDatabase} instance
     */
    @NotNull
    public MagicDatabase loadMagicDatabase(@NotNull String name) {
        final MagicDatabase database = newMagicDatabase(name);
        databases.put(name, database);
        return database;
    }

    /**
     * Constructs new {@link MagicDatabase MagicDatabases} with the specified names and loads them
     *
     * @param   toLoad  the names of the databases to create
     *
     * @return          this {@link MagicMongo} instance
     */
    @NotNull
    public MagicMongo loadMagicDatabases(@NotNull Iterable<String> toLoad) {
        toLoad.forEach(this::loadMagicDatabase);
        return this;
    }

    /**
     * Constructs new {@link MagicDatabase MagicDatabases} with the specified names and loads them
     *
     * @param   toLoad  the names of the databases to create
     *
     * @return          this {@link MagicMongo} instance
     */
    @NotNull
    public MagicMongo loadMagicDatabases(@NotNull String... toLoad) {
        for (final String name : toLoad) loadMagicDatabase(name);
        return this;
    }

    /**
     * Gets a {@link MagicDatabase} by name
     *
     * @param   name                        the name of the database to get
     *
     * @return                              the {@link MagicDatabase} instance
     *
     * @throws  IllegalArgumentException    if no {@link MagicDatabase} is found with the specified name
     */
    @NotNull
    public MagicDatabase getMagicDatabase(@NotNull String name) {
        final MagicDatabase collection = databases.get(name);
        if (collection == null) throw new IllegalArgumentException("No MagicDatabase found with name " + name);
        return collection;
    }
}
