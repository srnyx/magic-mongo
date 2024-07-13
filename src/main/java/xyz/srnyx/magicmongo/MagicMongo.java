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
 * A class for managing MongoDB collections using {@link MagicCollection MagicCollections}
 */
public class MagicMongo {
    /**
     * The {@link MongoClient}
     */
    @NotNull public MongoClient client;
    /**
     * The {@link MongoDatabase}
     */
    @NotNull public MongoDatabase database;
    /**
     * A map of {@link MagicCollection mongo collections}
     */
    @NotNull public final Map<Class<?>, MagicCollection<?>> mongoCollections = new HashMap<>();

    /**
     * Creates a new {@link MagicMongo} instance for managing MongoDB collections
     *
     * @param   connectionUrl               the connection URL for the MongoDB database
     * @param   codecRegistry               the {@link CodecRegistry} to use for the MongoDB database
     *
     * @throws  IllegalArgumentException    if no database name is found in the connection URL
     */
    public MagicMongo(@NotNull String connectionUrl, @Nullable CodecRegistry codecRegistry) {
        // Get database name
        final ConnectionString connection = new ConnectionString(connectionUrl);
        final String databaseName = connection.getDatabase();
        if (databaseName == null) throw new IllegalArgumentException("No database name found in connection URL " + connectionUrl);

        // Connect to database
        client = MongoClients.create(connection);
        database = client.getDatabase(databaseName);
        if (codecRegistry != null) database = database.withCodecRegistry(codecRegistry);
    }

    /**
     * Creates a new {@link MagicMongo} instance for managing MongoDB collections with the default {@link CodecRegistry}
     *
     * @param   connectionUrl               the connection URL for the MongoDB database
     *
     * @throws  IllegalArgumentException    if no database name is found in the connection URL
     */
    public MagicMongo(@NotNull String connectionUrl) {
        this(connectionUrl, CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())));
    }

    /**
     * Constructs, but doesn't load, a new {@link MagicCollection} with the given name and class
     * <br>Useful if you want to store collections in your own way (e.g. if a class is used for multiple collections)
     *
     * @param   name    the name of the collection
     * @param   clazz   the class of the collection
     *
     * @return          the new {@link MagicCollection}
     *
     * @param   <T>     the type of the class
     */
    @NotNull
    public <T> MagicCollection<T> newCollection(@NotNull String name, @NotNull Class<T> clazz) {
        return new MagicCollection<>(database, name, clazz);
    }

    /**
     * Loads a new {@link MagicCollection} with the given name and class
     *
     * @param   name    the name of the collection
     * @param   clazz   the class of the collection
     *
     * @return          the new {@link MagicCollection}
     *
     * @param   <T>     the type of the class
     */
    @NotNull
    public <T> MagicCollection<T> loadCollection(@NotNull String name, @NotNull Class<T> clazz) {
        final MagicCollection<T> collection = newCollection(name, clazz);
        mongoCollections.put(clazz, collection);
        return collection;
    }

    /**
     * Loads many new {@link MagicCollection MagicCollections} with the given names and classes
     *
     * @param   collections a map of strings (names) and classes (types) of collections to load
     *
     * @return              this {@link MagicMongo} instance for chaining
     */
    @NotNull
    public MagicMongo loadCollections(@NotNull Map<String, Class<?>> collections) {
        collections.forEach(this::loadCollection);
        return this;
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
    public <T> MagicCollection<T> getCollection(@NotNull Class<T> clazz) {
        final MagicCollection<?> collection = mongoCollections.get(clazz);
        if (collection == null) throw new IllegalArgumentException("No MagicCollection found for class " + clazz);
        return (MagicCollection<T>) collection;
    }

    /**
     * Gets the {@link MagicCollection} with the given name.
     * <br><b>Highly recommended to use {@link #getCollection(Class)} instead!</b>
     *
     * @param   name    the name of the {@link MagicCollection} to get
     *
     * @return          the {@link MagicCollection} with the given name
     *
     * @see             #getCollection(Class)
     */
    @NotNull
    public MagicCollection<?> getCollection(@NotNull String name) {
        final MagicCollection<?> collection = mongoCollections.values().stream()
                .filter(collectionFilter -> collectionFilter.collection.getNamespace().getCollectionName().equals(name))
                .findFirst()
                .orElse(null);
        if (collection == null) throw new IllegalArgumentException("No MagicCollection found with name " + name);
        return collection;
    }
}
