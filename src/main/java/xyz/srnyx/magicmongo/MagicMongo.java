package xyz.srnyx.magicmongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
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
 * A class for managing MongoDB collections using {@link MagicCollection MagicCollections}
 */
public class MagicMongo {
    /**
     * A map of {@link MagicCollection mongo collections} for the bot
     */
    @NotNull public final Map<Class<?>, MagicCollection<?>> mongoCollections = new HashMap<>();

    /**
     * Creates a new {@link MagicMongo} instance for managing MongoDB collections
     *
     * @param   connectionUrl               the connection URL for the MongoDB database
     * @param   collections                 a map of strings (names) and classes (types) of collections in the MongoDB database
     * @param   codecRegistry               the {@link CodecRegistry} to use for the MongoDB database
     *
     * @throws  IllegalArgumentException    if no database name is found in the connection URL
     */
    public MagicMongo(@NotNull String connectionUrl, @NotNull Map<String, Class<?>> collections, @Nullable CodecRegistry codecRegistry) {
        // Get database name
        final ConnectionString connection = new ConnectionString(connectionUrl);
        final String databaseName = connection.getDatabase();
        if (databaseName == null) throw new IllegalArgumentException("No database name found in connection URL " + connectionUrl);

        // Connect to database
        MongoDatabase database = MongoClients.create(connection).getDatabase(databaseName);
        if (codecRegistry != null) database = database.withCodecRegistry(codecRegistry);

        // Get/set collections
        for (final Map.Entry<String, Class<?>> entry : collections.entrySet()) {
            final Class<?> clazz = entry.getValue();
            mongoCollections.put(clazz, new MagicCollection<>(database, entry.getKey(), clazz));
        }
    }

    /**
     * Creates a new {@link MagicMongo} instance for managing MongoDB collections with the default {@link CodecRegistry}
     *
     * @param   connectionUrl               the connection URL for the MongoDB database
     * @param   collections                 a map of strings (names) and classes (types) of collections in the MongoDB database
     *
     * @throws  IllegalArgumentException    if no database name is found in the connection URL
     */
    public MagicMongo(@NotNull String connectionUrl, @NotNull Map<String, Class<?>> collections) {
        this(connectionUrl, collections, CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())));
    }

    /**
     * Gets the {@link MagicCollection} for the given class
     *
     * @param   clazz   the class to get the {@link MagicCollection} for
     *
     * @return          the {@link MagicCollection} for the given class
     *
     * @param   <T>     the type of the class
     */
    @NotNull
    public <T> MagicCollection<T> getMongoCollection(@NotNull Class<T> clazz) {
        final MagicCollection<?> collection = mongoCollections.get(clazz);
        if (collection == null) throw new IllegalArgumentException("No MagicCollection found for class " + clazz);
        return (MagicCollection<T>) collection;
    }

    /**
     * Gets the {@link MagicCollection} with the given name.
     * <br><b>Highly recommended to use {@link #getMongoCollection(Class)} instead!</b>
     *
     * @param   name    the name of the {@link MagicCollection} to get
     *
     * @return          the {@link MagicCollection} with the given name
     *
     * @see             #getMongoCollection(Class)
     */
    @NotNull
    public MagicCollection<?> getMongoCollection(@NotNull String name) {
        final MagicCollection<?> collection = mongoCollections.values().stream()
                .filter(collectionFilter -> collectionFilter.collection.getNamespace().getCollectionName().equals(name))
                .findFirst()
                .orElse(null);
        if (collection == null) throw new IllegalArgumentException("No MagicCollection found with name " + name);
        return collection;
    }
}
