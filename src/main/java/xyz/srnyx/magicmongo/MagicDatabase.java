package xyz.srnyx.magicmongo;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.CreateViewOptions;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * A wrapper class for {@link MongoDatabase}
 */
public class MagicDatabase implements MongoDatabase {
    /**
     * The {@link MongoDatabase} instance for this {@link MagicDatabase}
     */
    @NotNull public final MongoDatabase database;
    /**
     * A map of names to classes of collections for this database
     */
    @NotNull public final Map<String, Class<?>> nameToClass = new HashMap<>();
    /**
     * A map of {@link MagicCollection mongo collections} for this database
     */
    @NotNull public final Map<Class<?>, MagicCollection<?>> collections = new HashMap<>();

    /**
     * Creates a new {@link MagicDatabase} instance
     *
     * @param   database    the {@link MongoDatabase} instance to use
     */
    public MagicDatabase(@NotNull MongoDatabase database) {
        this.database = database;
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
    public <T> MagicCollection<T> newMagicCollection(@NotNull String name, @NotNull Class<T> clazz) {
        return new MagicCollection<>(this, name, clazz);
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
    public <T> MagicCollection<T> loadMagicCollection(@NotNull String name, @NotNull Class<T> clazz) {
        final MagicCollection<T> collection = newMagicCollection(name, clazz);
        nameToClass.put(name, clazz);
        collections.put(clazz, collection);
        return collection;
    }

    /**
     * Loads many new {@link MagicCollection MagicCollections} with the given names and classes
     *
     * @param   toLoad  a map of strings (names) and classes (types) of collections to load
     *
     * @return          this {@link MagicDatabase} instance for chaining
     */
    @NotNull
    public MagicDatabase loadMagicCollections(@NotNull Map<String, Class<?>> toLoad) {
        toLoad.forEach(this::loadMagicCollection);
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
    public <T> MagicCollection<T> getMagicCollection(@NotNull Class<T> clazz) {
        final MagicCollection<?> collection = collections.get(clazz);
        if (collection == null) throw new IllegalArgumentException("No MagicCollection found for class " + clazz);
        return (MagicCollection<T>) collection;
    }

    /**
     * Gets the {@link MagicCollection} with the given name
     * <br><b>Recommended to use {@link #getMagicCollection(Class)} instead</b>
     *
     * @param   name    the name of the {@link MagicCollection} to get
     *
     * @return          the {@link MagicCollection} with the given name
     *
     * @see             #getMagicCollection(Class)
     */
    @NotNull
    public MagicCollection<?> getMagicCollection(@NotNull String name) {
        final Class<?> clazz = nameToClass.get(name);
        if (clazz == null) throw new IllegalArgumentException("No MagicCollection found with name " + name);
        return getMagicCollection(clazz);
    }

    // OVERRIDE METHODS FROM MongoDatabase
    @Override @NotNull
    public String getName() {
        return database.getName();
    }
    @Override @NotNull
    public CodecRegistry getCodecRegistry() {
        return database.getCodecRegistry();
    }
    @Override @NotNull
    public ReadPreference getReadPreference() {
        return database.getReadPreference();
    }
    @Override @NotNull
    public WriteConcern getWriteConcern() {
        return database.getWriteConcern();
    }
    @Override @NotNull
    public ReadConcern getReadConcern() {
        return database.getReadConcern();
    }
    @Override @Nullable
    public Long getTimeout(@NotNull TimeUnit timeUnit) {
        return database.getTimeout(timeUnit);
    }
    @Override @NotNull
    public MongoDatabase withCodecRegistry(@NotNull CodecRegistry codecRegistry) {
        return database.withCodecRegistry(codecRegistry);
    }
    @Override @NotNull
    public MongoDatabase withReadPreference(@NotNull ReadPreference readPreference) {
        return database.withReadPreference(readPreference);
    }
    @Override @NotNull
    public MongoDatabase withWriteConcern(@NotNull WriteConcern writeConcern) {
        return database.withWriteConcern(writeConcern);
    }
    @Override @NotNull
    public MongoDatabase withReadConcern(@NotNull ReadConcern readConcern) {
        return database.withReadConcern(readConcern);
    }
    @Override @NotNull
    public MongoDatabase withTimeout(long timeout, @NotNull TimeUnit timeUnit) {
        return database.withTimeout(timeout, timeUnit);
    }
    @Override @NotNull
    public MongoCollection<Document> getCollection(@NotNull String collectionName) {
        return database.getCollection(collectionName);
    }
    @Override @NotNull
    public <T> MongoCollection<T> getCollection(@NotNull String collectionName, @NotNull Class<T> tDocumentClass) {
        return database.getCollection(collectionName, tDocumentClass);
    }
    @Override @NotNull
    public Document runCommand(@NotNull Bson command) {
        return database.runCommand(command);
    }
    @Override @NotNull
    public Document runCommand(@NotNull Bson command, @NotNull ReadPreference readPreference) {
        return database.runCommand(command, readPreference);
    }
    @Override @NotNull
    public <R> R runCommand(@NotNull Bson command, @NotNull Class<R> tResultClass) {
        return database.runCommand(command, tResultClass);
    }
    @Override @NotNull
    public <R> R runCommand(@NotNull Bson command, @NotNull ReadPreference readPreference, @NotNull Class<R> tResultClass) {
        return database.runCommand(command, readPreference, tResultClass);
    }
    @Override @NotNull
    public Document runCommand(@NotNull ClientSession clientSession, @NotNull Bson command) {
        return database.runCommand(clientSession, command);
    }
    @Override @NotNull
    public Document runCommand(@NotNull ClientSession clientSession, @NotNull Bson command, @NotNull ReadPreference readPreference) {
        return database.runCommand(clientSession, command, readPreference);
    }
    @Override @NotNull
    public <R> R runCommand(@NotNull ClientSession clientSession, @NotNull Bson command, @NotNull Class<R> tResultClass) {
        return database.runCommand(clientSession, command, tResultClass);
    }
    @Override @NotNull
    public <R> R runCommand(@NotNull ClientSession clientSession, @NotNull Bson command, @NotNull ReadPreference readPreference, @NotNull Class<R> tResultClass) {
        return database.runCommand(clientSession, command, readPreference, tResultClass);
    }
    @Override
    public void drop() {
        database.drop();
    }
    @Override
    public void drop(@NotNull ClientSession clientSession) {
        database.drop(clientSession);
    }
    @Override @NotNull
    public ListCollectionNamesIterable listCollectionNames() {
        return database.listCollectionNames();
    }
    @Override @NotNull
    public ListCollectionsIterable<Document> listCollections() {
        return database.listCollections();
    }
    @Override @NotNull
    public <R> ListCollectionsIterable<R> listCollections(@NotNull Class<R> tResultClass) {
        return database.listCollections(tResultClass);
    }
    @Override @NotNull
    public ListCollectionNamesIterable listCollectionNames(@NotNull ClientSession clientSession) {
        return database.listCollectionNames(clientSession);
    }
    @Override @NotNull
    public ListCollectionsIterable<Document> listCollections(@NotNull ClientSession clientSession) {
        return database.listCollections(clientSession);
    }
    @Override @NotNull
    public <R> ListCollectionsIterable<R> listCollections(@NotNull ClientSession clientSession, @NotNull Class<R> tResultClass) {
        return database.listCollections(clientSession, tResultClass);
    }
    @Override
    public void createCollection(@NotNull String collectionName) {
        database.createCollection(collectionName);
    }
    @Override
    public void createCollection(@NotNull String collectionName, @NotNull CreateCollectionOptions createCollectionOptions) {
        database.createCollection(collectionName, createCollectionOptions);
    }
    @Override
    public void createCollection(@NotNull ClientSession clientSession, @NotNull String collectionName) {
        database.createCollection(clientSession, collectionName);
    }
    @Override
    public void createCollection(@NotNull ClientSession clientSession, @NotNull String collectionName, @NotNull CreateCollectionOptions createCollectionOptions) {
        database.createCollection(clientSession, collectionName, createCollectionOptions);
    }
    @Override
    public void createView(@NotNull String viewName, @NotNull String viewOn, @NotNull List<? extends Bson> pipeline) {
        database.createView(viewName, viewOn, pipeline);
    }
    @Override
    public void createView(@NotNull String viewName, @NotNull String viewOn, @NotNull List<? extends Bson> pipeline, @NotNull CreateViewOptions createViewOptions) {
        database.createView(viewName, viewOn, pipeline, createViewOptions);
    }
    @Override
    public void createView(@NotNull ClientSession clientSession, @NotNull String viewName, @NotNull String viewOn, @NotNull List<? extends Bson> pipeline) {
        database.createView(clientSession, viewName, viewOn, pipeline);
    }
    @Override
    public void createView(@NotNull ClientSession clientSession, @NotNull String viewName, @NotNull String viewOn, @NotNull List<? extends Bson> pipeline, @NotNull CreateViewOptions createViewOptions) {
        database.createView(clientSession, viewName, viewOn, pipeline, createViewOptions);
    }
    @Override @NotNull
    public ChangeStreamIterable<Document> watch() {
        return database.watch();
    }
    @Override @NotNull
    public <R> ChangeStreamIterable<R> watch(@NotNull Class<R> tResultClass) {
        return database.watch(tResultClass);
    }
    @Override @NotNull
    public ChangeStreamIterable<Document> watch(@NotNull List<? extends Bson> pipeline) {
        return database.watch(pipeline);
    }
    @Override @NotNull
    public <R> ChangeStreamIterable<R> watch(@NotNull List<? extends Bson> pipeline, @NotNull Class<R> tResultClass) {
        return database.watch(pipeline, tResultClass);
    }
    @Override @NotNull
    public ChangeStreamIterable<Document> watch(@NotNull ClientSession clientSession) {
        return database.watch(clientSession);
    }
    @Override @NotNull
    public <R> ChangeStreamIterable<R> watch(@NotNull ClientSession clientSession, @NotNull Class<R> tResultClass) {
        return database.watch(clientSession, tResultClass);
    }
    @Override @NotNull
    public ChangeStreamIterable<Document> watch(@NotNull ClientSession clientSession, @NotNull List<? extends Bson> pipeline) {
        return database.watch(clientSession, pipeline);
    }
    @Override @NotNull
    public <R> ChangeStreamIterable<R> watch(@NotNull ClientSession clientSession, @NotNull List<? extends Bson> pipeline, @NotNull Class<R> tResultClass) {
        return database.watch(clientSession, pipeline, tResultClass);
    }
    @Override @NotNull
    public AggregateIterable<Document> aggregate(@NotNull List<? extends Bson> pipeline) {
        return database.aggregate(pipeline);
    }
    @Override @NotNull
    public <R> AggregateIterable<R> aggregate(@NotNull List<? extends Bson> pipeline, @NotNull Class<R> tResultClass) {
        return database.aggregate(pipeline, tResultClass);
    }
    @Override @NotNull
    public AggregateIterable<Document> aggregate(@NotNull ClientSession clientSession, @NotNull List<? extends Bson> pipeline) {
        return database.aggregate(clientSession, pipeline);
    }
    @Override @NotNull
    public <R> AggregateIterable<R> aggregate(@NotNull ClientSession clientSession, @NotNull List<? extends Bson> pipeline, @NotNull Class<R> tResultClass) {
        return database.aggregate(clientSession, pipeline, tResultClass);
    }
}
