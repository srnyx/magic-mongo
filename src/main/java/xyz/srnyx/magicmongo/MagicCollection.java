package xyz.srnyx.magicmongo;

import com.mongodb.MongoNamespace;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * A wrapper for {@link MongoCollection} with some useful shortcuts/methods
 *
 * @param   <T> the type of the collection
 */
public class MagicCollection<T> implements MongoCollection<T> {
    /**
     * The {@link MongoCollection} instance
     */
    @NotNull public final MongoCollection<T> collection;

    /**
     * Constructs a new {@link MagicCollection} instance
     *
     * @param   database    the {@link MongoDatabase} instance
     * @param   name        the name of the collection
     * @param   clazz       the class of the collection
     */
    public MagicCollection(@NotNull MongoDatabase database, @NotNull String name, @NotNull Class<T> clazz) {
        collection = database.getCollection(name, clazz);
    }

    /**
     * Finds one document in the collection
     *
     * @param   filter  the filter to apply
     *
     * @return          the document found, or null if none was found
     */
    @NotNull
    public Optional<T> findOne(@NotNull Bson filter) {
        return Optional.ofNullable(find(filter).first());
    }

    /**
     * Finds one document in the collection
     *
     * @param   field   the field to filter by
     * @param   value   the value of the field to filter by
     *
     * @return          the document found, or null if none was found
     */
    @NotNull
    public Optional<T> findOne(@NotNull String field, @Nullable Object value) {
        return findOne(Filters.eq(field, value));
    }

    /**
     * Finds multiple documents in the collection
     *
     * @param   filter  the filter to apply
     *
     * @return          the documents found
     */
    @NotNull
    public List<T> findMany(@NotNull Bson filter) {
        return find(filter).into(new ArrayList<>());
    }

    /**
     * Inserts a document in the collection and returns the inserted document's ID
     *
     * @param   t   the document to insert
     *
     * @return      the ID of the inserted document
     */
    @NotNull
    public ObjectId insertOneReturnId(@NotNull T t) {
        return Objects.requireNonNull(insertOne(t).getInsertedId()).asObjectId().getValue();
    }

    /**
     * Upserts a document in the collection
     *
     * @param   filter  the filter to apply
     * @param   update  the update to apply
     *
     * @return          the {@link UpdateResult} of the operation
     */
    @NotNull
    public UpdateResult upsertOne(@NotNull Bson filter, @NotNull Bson update) {
        return updateOne(filter, update, new UpdateOptions().upsert(true));
    }

    /**
     * Updates a document in the collection and returns the updated document
     *
     * @param   filter  the filter to apply
     * @param   update  the update to apply
     *
     * @return          the updated document
     */
    @NotNull
    public Optional<T> findOneAndUpdateReturn(@NotNull Bson filter, @NotNull Bson update) {
        return Optional.ofNullable(findOneAndUpdate(filter, update, new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)));
    }

    /**
     * Updates a document in the collection (or inserts it if it doesn't exist) and returns the updated document
     *
     * @param   filter  the filter to apply
     * @param   update  the update to apply
     *
     * @return          the updated or inserted document
     */
    @NotNull
    public T findOneAndUpsert(@NotNull Bson filter, @NotNull Bson update) {
        return Objects.requireNonNull(findOneAndUpdate(filter, update, new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .upsert(true)));
    }

    /**
     * Deletes a document in the collection
     *
     * @param   field   the field to filter by
     * @param   value   the value of the field to filter by
     *
     * @return          the {@link DeleteResult} of the operation
     */
    @NotNull
    public DeleteResult deleteOne(@NotNull String field, @Nullable Object value) {
        return deleteOne(Filters.eq(field, value));
    }

    // OVERRIDE METHODS FROM MongoCollection
    @Override @NotNull
    public UpdateResult updateOne(@NotNull Bson filter, @NotNull Bson update) {
        return collection.updateOne(filter, update);
    }
    @Override @NotNull
    public UpdateResult updateOne(@NotNull Bson filter, @NotNull Bson update, @NotNull UpdateOptions updateOptions) {
        return collection.updateOne(filter, update, updateOptions);
    }
    @Override @NotNull
    public UpdateResult updateOne(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull Bson update) {
        return collection.updateOne(clientSession, filter, update);
    }
    @Override @NotNull
    public UpdateResult updateOne(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull Bson update, @NotNull UpdateOptions updateOptions) {
        return collection.updateOne(clientSession, filter, update, updateOptions);
    }
    @Override @NotNull
    public UpdateResult updateOne(@NotNull Bson filter, @NotNull List<? extends Bson> update) {
        return collection.updateOne(filter, update);
    }
    @Override @NotNull
    public UpdateResult updateOne(@NotNull Bson filter, @NotNull List<? extends Bson> update, @NotNull UpdateOptions updateOptions) {
        return collection.updateOne(filter, update, updateOptions);
    }
    @Override @NotNull
    public UpdateResult updateOne(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull List<? extends Bson> update) {
        return collection.updateOne(clientSession, filter, update);
    }
    @Override @NotNull
    public UpdateResult updateOne(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull List<? extends Bson> update, @NotNull UpdateOptions updateOptions) {
        return collection.updateOne(clientSession, filter, update, updateOptions);
    }
    @Override @NotNull
    public UpdateResult updateMany(@NotNull Bson filter, @NotNull Bson update) {
        return collection.updateMany(filter, update);
    }
    @Override @NotNull
    public UpdateResult updateMany(@NotNull Bson filter, @NotNull Bson update, @NotNull UpdateOptions updateOptions) {
        return collection.updateMany(filter, update, updateOptions);
    }
    @Override @NotNull
    public UpdateResult updateMany(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull Bson update) {
        return collection.updateMany(clientSession, filter, update);
    }
    @Override @NotNull
    public UpdateResult updateMany(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull Bson update, @NotNull UpdateOptions updateOptions) {
        return collection.updateMany(clientSession, filter, update, updateOptions);
    }
    @Override @NotNull
    public UpdateResult updateMany(@NotNull Bson filter, @NotNull List<? extends Bson> update) {
        return collection.updateMany(filter, update);
    }
    @Override @NotNull
    public UpdateResult updateMany(@NotNull Bson filter, @NotNull List<? extends Bson> update, @NotNull UpdateOptions updateOptions) {
        return collection.updateMany(filter, update, updateOptions);
    }
    @Override @NotNull
    public UpdateResult updateMany(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull List<? extends Bson> update) {
        return collection.updateMany(clientSession, filter, update);
    }
    @Override @NotNull
    public UpdateResult updateMany(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull List<? extends Bson> update, @NotNull UpdateOptions updateOptions) {
        return collection.updateMany(clientSession, filter, update, updateOptions);
    }
    @Override @Nullable
    public T findOneAndDelete(@NotNull Bson filter) {
        return collection.findOneAndDelete(filter);
    }
    @Override @Nullable
    public T findOneAndDelete(@NotNull Bson filter, @NotNull FindOneAndDeleteOptions options) {
        return collection.findOneAndDelete(filter, options);
    }
    @Override @Nullable
    public T findOneAndDelete(@NotNull ClientSession clientSession, @NotNull Bson filter) {
        return collection.findOneAndDelete(clientSession, filter);
    }
    @Override @Nullable
    public T findOneAndDelete(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull FindOneAndDeleteOptions options) {
        return collection.findOneAndDelete(clientSession, filter, options);
    }
    @Override @Nullable
    public T findOneAndReplace(@NotNull Bson filter, @NotNull T replacement) {
        return collection.findOneAndReplace(filter, replacement);
    }
    @Override @Nullable
    public T findOneAndReplace(@NotNull Bson filter, @NotNull T replacement, @NotNull FindOneAndReplaceOptions options) {
        return collection.findOneAndReplace(filter, replacement, options);
    }
    @Override @Nullable
    public T findOneAndReplace(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull T replacement) {
        return collection.findOneAndReplace(clientSession, filter, replacement);
    }
    @Override @Nullable
    public T findOneAndReplace(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull T replacement, @NotNull FindOneAndReplaceOptions options) {
        return collection.findOneAndReplace(clientSession, filter, replacement, options);
    }
    @Override @Nullable
    public T findOneAndUpdate(@NotNull Bson filter, @NotNull Bson update) {
        return collection.findOneAndUpdate(filter, update);
    }
    @Override @Nullable
    public T findOneAndUpdate(@NotNull Bson filter, @NotNull Bson update, @NotNull FindOneAndUpdateOptions options) {
        return collection.findOneAndUpdate(filter, update, options);
    }
    @Override @Nullable
    public T findOneAndUpdate(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull Bson update) {
        return collection.findOneAndUpdate(clientSession, filter, update);
    }
    @Override @Nullable
    public T findOneAndUpdate(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull Bson update, @NotNull FindOneAndUpdateOptions options) {
        return collection.findOneAndUpdate(clientSession, filter, update, options);
    }
    @Override @Nullable
    public T findOneAndUpdate(@NotNull Bson filter, @NotNull List<? extends Bson> update) {
        return collection.findOneAndUpdate(filter, update);
    }
    @Override @Nullable
    public T findOneAndUpdate(@NotNull Bson filter, @NotNull List<? extends Bson> update, @NotNull FindOneAndUpdateOptions options) {
        return collection.findOneAndUpdate(filter, update, options);
    }
    @Override @Nullable
    public T findOneAndUpdate(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull List<? extends Bson> update) {
        return collection.findOneAndUpdate(clientSession, filter, update);
    }
    @Override @Nullable
    public T findOneAndUpdate(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull List<? extends Bson> update, @NotNull FindOneAndUpdateOptions options) {
        return collection.findOneAndUpdate(clientSession, filter, update, options);
    }
    @Override
    public void drop() {
        collection.drop();
    }
    @Override
    public void drop(@NotNull ClientSession clientSession) {
        collection.drop(clientSession);
    }
    @Override
    public void drop(@NotNull DropCollectionOptions dropCollectionOptions) {
        collection.drop(dropCollectionOptions);
    }
    @Override
    public void drop(@NotNull ClientSession clientSession, @NotNull DropCollectionOptions dropCollectionOptions) {
        collection.drop(clientSession, dropCollectionOptions);
    }
    @Override @NotNull
    public String createSearchIndex(@NotNull String indexName, @NotNull Bson definition) {
        return collection.createSearchIndex(indexName, definition);
    }
    @Override @NotNull
    public String createSearchIndex(@NotNull Bson definition) {
        return collection.createSearchIndex(definition);
    }
    @Override @NotNull
    public List<String> createSearchIndexes(@NotNull List<SearchIndexModel> searchIndexModels) {
        return collection.createSearchIndexes(searchIndexModels);
    }
    @Override
    public void updateSearchIndex(@NotNull String indexName, @NotNull Bson definition) {
        collection.updateSearchIndex(indexName, definition);
    }
    @Override
    public void dropSearchIndex(@NotNull String indexName) {
        collection.dropSearchIndex(indexName);
    }
    @Override @NotNull
    public ListSearchIndexesIterable<Document> listSearchIndexes() {
        return collection.listSearchIndexes();
    }
    @Override @NotNull
    public <R> ListSearchIndexesIterable<R> listSearchIndexes(@NotNull Class<R> tResultClass) {
        return collection.listSearchIndexes(tResultClass);
    }
    @Override @NotNull
    public String createIndex(@NotNull Bson keys) {
        return collection.createIndex(keys);
    }
    @Override @NotNull
    public String createIndex(@NotNull Bson keys, @NotNull IndexOptions indexOptions) {
        return collection.createIndex(keys, indexOptions);
    }
    @Override @NotNull
    public String createIndex(@NotNull ClientSession clientSession, @NotNull Bson keys) {
        return collection.createIndex(clientSession, keys);
    }
    @Override @NotNull
    public String createIndex(@NotNull ClientSession clientSession, @NotNull Bson keys, @NotNull IndexOptions indexOptions) {
        return collection.createIndex(clientSession, keys, indexOptions);
    }
    @Override @NotNull
    public List<String> createIndexes(@NotNull List<IndexModel> indexes) {
        return collection.createIndexes(indexes);
    }
    @Override @NotNull
    public List<String> createIndexes(@NotNull List<IndexModel> indexes, @NotNull CreateIndexOptions createIndexOptions) {
        return collection.createIndexes(indexes, createIndexOptions);
    }
    @Override @NotNull
    public List<String> createIndexes(@NotNull ClientSession clientSession, @NotNull List<IndexModel> indexes) {
        return collection.createIndexes(clientSession, indexes);
    }
    @Override @NotNull
    public List<String> createIndexes(@NotNull ClientSession clientSession, @NotNull List<IndexModel> indexes, @NotNull CreateIndexOptions createIndexOptions) {
        return collection.createIndexes(clientSession, indexes, createIndexOptions);
    }
    @Override @NotNull
    public ListIndexesIterable<Document> listIndexes() {
        return collection.listIndexes();
    }
    @Override @NotNull
    public <R> ListIndexesIterable<R> listIndexes(@NotNull Class<R> tResultClass) {
        return collection.listIndexes(tResultClass);
    }
    @Override @NotNull
    public ListIndexesIterable<Document> listIndexes(@NotNull ClientSession clientSession) {
        return collection.listIndexes(clientSession);
    }
    @Override @NotNull
    public <R> ListIndexesIterable<R> listIndexes(@NotNull ClientSession clientSession, @NotNull Class<R> tResultClass) {
        return collection.listIndexes(clientSession, tResultClass);
    }
    @Override
    public void dropIndex(@NotNull String indexName) {
        collection.dropIndex(indexName);
    }
    @Override
    public void dropIndex(@NotNull String indexName, @NotNull DropIndexOptions dropIndexOptions) {
        collection.dropIndex(indexName, dropIndexOptions);
    }
    @Override
    public void dropIndex(@NotNull Bson keys) {
        collection.dropIndex(keys);
    }
    @Override
    public void dropIndex(@NotNull Bson keys, @NotNull DropIndexOptions dropIndexOptions) {
        collection.dropIndex(keys, dropIndexOptions);
    }
    @Override
    public void dropIndex(@NotNull ClientSession clientSession, @NotNull String indexName) {
        collection.dropIndex(clientSession, indexName);
    }
    @Override
    public void dropIndex(@NotNull ClientSession clientSession, @NotNull Bson keys) {
        collection.dropIndex(clientSession, keys);
    }
    @Override
    public void dropIndex(@NotNull ClientSession clientSession, @NotNull String indexName, @NotNull DropIndexOptions dropIndexOptions) {
        collection.dropIndex(clientSession, indexName, dropIndexOptions);
    }
    @Override
    public void dropIndex(@NotNull ClientSession clientSession, @NotNull Bson keys, @NotNull DropIndexOptions dropIndexOptions) {
        collection.dropIndex(clientSession, keys, dropIndexOptions);
    }
    @Override
    public void dropIndexes() {
        collection.dropIndexes();
    }
    @Override
    public void dropIndexes(@NotNull ClientSession clientSession) {
        collection.dropIndexes(clientSession);
    }
    @Override
    public void dropIndexes(@NotNull DropIndexOptions dropIndexOptions) {
        collection.dropIndexes(dropIndexOptions);
    }
    @Override
    public void dropIndexes(@NotNull ClientSession clientSession, @NotNull DropIndexOptions dropIndexOptions) {
        collection.dropIndexes(clientSession, dropIndexOptions);
    }
    @Override
    public void renameCollection(@NotNull MongoNamespace newCollectionNamespace) {
        collection.renameCollection(newCollectionNamespace);
    }
    @Override
    public void renameCollection(@NotNull MongoNamespace newCollectionNamespace, @NotNull RenameCollectionOptions renameCollectionOptions) {
        collection.renameCollection(newCollectionNamespace, renameCollectionOptions);
    }
    @Override
    public void renameCollection(@NotNull ClientSession clientSession, @NotNull MongoNamespace newCollectionNamespace) {
        collection.renameCollection(clientSession, newCollectionNamespace);
    }
    @Override
    public void renameCollection(@NotNull ClientSession clientSession, @NotNull MongoNamespace newCollectionNamespace, @NotNull RenameCollectionOptions renameCollectionOptions) {
        collection.renameCollection(clientSession, newCollectionNamespace, renameCollectionOptions);
    }
    @Override @NotNull
    public MongoNamespace getNamespace() {
        return collection.getNamespace();
    }
    @Override @NotNull
    public Class<T> getDocumentClass() {
        return collection.getDocumentClass();
    }
    @Override @NotNull
    public CodecRegistry getCodecRegistry() {
        return collection.getCodecRegistry();
    }
    @Override @NotNull
    public ReadPreference getReadPreference() {
        return collection.getReadPreference();
    }
    @Override @NotNull
    public WriteConcern getWriteConcern() {
        return collection.getWriteConcern();
    }
    @Override @NotNull
    public ReadConcern getReadConcern() {
        return collection.getReadConcern();
    }
    @Override @Nullable
    public Long getTimeout(@NotNull TimeUnit timeUnit) {
        return collection.getTimeout(timeUnit);
    }
    @Override @NotNull
    public <N> MongoCollection<N> withDocumentClass(@NotNull Class<N> clazz) {
        return collection.withDocumentClass(clazz);
    }
    @Override @NotNull
    public MongoCollection<T> withCodecRegistry(@NotNull CodecRegistry codecRegistry) {
        return collection.withCodecRegistry(codecRegistry);
    }
    @Override @NotNull
    public MongoCollection<T> withReadPreference(@NotNull ReadPreference readPreference) {
        return collection.withReadPreference(readPreference);
    }
    @Override @NotNull
    public MongoCollection<T> withWriteConcern(@NotNull WriteConcern writeConcern) {
        return collection.withWriteConcern(writeConcern);
    }
    @Override @NotNull
    public MongoCollection<T> withReadConcern(@NotNull ReadConcern readConcern) {
        return collection.withReadConcern(readConcern);
    }
    @Override @NotNull
    public MongoCollection<T> withTimeout(long timeout, @NotNull TimeUnit timeUnit) {
        return collection.withTimeout(timeout, timeUnit);
    }
    @Override
    public long countDocuments() {
        return collection.countDocuments();
    }
    @Override
    public long countDocuments(@NotNull Bson filter) {
        return collection.countDocuments(filter);
    }
    @Override
    public long countDocuments(@NotNull Bson filter, @NotNull CountOptions options) {
        return collection.countDocuments(filter, options);
    }
    @Override
    public long countDocuments(@NotNull ClientSession clientSession) {
        return collection.countDocuments(clientSession);
    }
    @Override
    public long countDocuments(@NotNull ClientSession clientSession, @NotNull Bson filter) {
        return collection.countDocuments(clientSession, filter);
    }
    @Override
    public long countDocuments(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull CountOptions options) {
        return collection.countDocuments(clientSession, filter, options);
    }
    @Override
    public long estimatedDocumentCount() {
        return collection.estimatedDocumentCount();
    }
    @Override
    public long estimatedDocumentCount(@NotNull EstimatedDocumentCountOptions options) {
        return collection.estimatedDocumentCount(options);
    }
    @Override @NotNull
    public <R> DistinctIterable<R> distinct(@NotNull String fieldName, @NotNull Class<R> tResultClass) {
        return collection.distinct(fieldName, tResultClass);
    }
    @Override @NotNull
    public <R> DistinctIterable<R> distinct(@NotNull String fieldName, @NotNull Bson filter, @NotNull Class<R> tResultClass) {
        return collection.distinct(fieldName, filter, tResultClass);
    }
    @Override @NotNull
    public <R> DistinctIterable<R> distinct(@NotNull ClientSession clientSession, @NotNull String fieldName, @NotNull Class<R> tResultClass) {
        return collection.distinct(clientSession, fieldName, tResultClass);
    }
    @Override @NotNull
    public <R> DistinctIterable<R> distinct(@NotNull ClientSession clientSession, @NotNull String fieldName, @NotNull Bson filter, @NotNull Class<R> tResultClass) {
        return collection.distinct(clientSession, fieldName, filter, tResultClass);
    }
    @Override @NotNull
    public FindIterable<T> find() {
        return collection.find();
    }
    @Override @NotNull
    public <R> FindIterable<R> find(@NotNull Class<R> tResultClass) {
        return collection.find(tResultClass);
    }
    @Override @NotNull
    public FindIterable<T> find(@NotNull Bson filter) {
        return collection.find(filter);
    }
    @Override @NotNull
    public <R> FindIterable<R> find(@NotNull Bson filter, @NotNull Class<R> tResultClass) {
        return collection.find(filter, tResultClass);
    }
    @Override @NotNull
    public FindIterable<T> find(@NotNull ClientSession clientSession) {
        return collection.find(clientSession);
    }
    @Override @NotNull
    public <R> FindIterable<R> find(@NotNull ClientSession clientSession, @NotNull Class<R> tResultClass) {
        return collection.find(clientSession, tResultClass);
    }
    @Override @NotNull
    public FindIterable<T> find(@NotNull ClientSession clientSession, @NotNull Bson filter) {
        return collection.find(clientSession, filter);
    }
    @Override @NotNull
    public <R> FindIterable<R> find(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull Class<R> tResultClass) {
        return collection.find(clientSession, filter, tResultClass);
    }
    @Override @NotNull
    public AggregateIterable<T> aggregate(@NotNull List<? extends Bson> pipeline) {
        return collection.aggregate(pipeline);
    }
    @Override @NotNull
    public <R> AggregateIterable<R> aggregate(@NotNull List<? extends Bson> pipeline, @NotNull Class<R> tResultClass) {
        return collection.aggregate(pipeline, tResultClass);
    }
    @Override @NotNull
    public AggregateIterable<T> aggregate(@NotNull ClientSession clientSession, @NotNull List<? extends Bson> pipeline) {
        return collection.aggregate(clientSession, pipeline);
    }
    @Override @NotNull
    public <R> AggregateIterable<R> aggregate(@NotNull ClientSession clientSession, @NotNull List<? extends Bson> pipeline, @NotNull Class<R> tResultClass) {
        return collection.aggregate(clientSession, pipeline, tResultClass);
    }
    @Override @NotNull
    public ChangeStreamIterable<T> watch() {
        return collection.watch();
    }
    @Override @NotNull
    public <R> ChangeStreamIterable<R> watch(@NotNull Class<R> tResultClass) {
        return collection.watch(tResultClass);
    }
    @Override @NotNull
    public ChangeStreamIterable<T> watch(@NotNull List<? extends Bson> pipeline) {
        return collection.watch(pipeline);
    }
    @Override @NotNull
    public <R> ChangeStreamIterable<R> watch(@NotNull List<? extends Bson> pipeline, @NotNull Class<R> tResultClass) {
        return collection.watch(pipeline, tResultClass);
    }
    @Override @NotNull
    public ChangeStreamIterable<T> watch(@NotNull ClientSession clientSession) {
        return collection.watch(clientSession);
    }
    @Override @NotNull
    public <R> ChangeStreamIterable<R> watch(@NotNull ClientSession clientSession, @NotNull Class<R> tResultClass) {
        return collection.watch(clientSession, tResultClass);
    }
    @Override @NotNull
    public ChangeStreamIterable<T> watch(@NotNull ClientSession clientSession, @NotNull List<? extends Bson> pipeline) {
        return collection.watch(clientSession, pipeline);
    }
    @Override @NotNull
    public <R> ChangeStreamIterable<R> watch(@NotNull ClientSession clientSession, @NotNull List<? extends Bson> pipeline, @NotNull Class<R> tResultClass) {
        return collection.watch(clientSession, pipeline, tResultClass);
    }
    @Override @NotNull
    public MapReduceIterable<T> mapReduce(@NotNull String mapFunction, @NotNull String reduceFunction) {
        return collection.mapReduce(mapFunction, reduceFunction);
    }
    @Override @NotNull
    public <R> MapReduceIterable<R> mapReduce(@NotNull String mapFunction, @NotNull String reduceFunction, @NotNull Class<R> tResultClass) {
        return collection.mapReduce(mapFunction, reduceFunction, tResultClass);
    }
    @Override @NotNull
    public MapReduceIterable<T> mapReduce(@NotNull ClientSession clientSession, @NotNull String mapFunction, @NotNull String reduceFunction) {
        return collection.mapReduce(clientSession, mapFunction, reduceFunction);
    }
    @Override @NotNull
    public <R> MapReduceIterable<R> mapReduce(@NotNull ClientSession clientSession, @NotNull String mapFunction, @NotNull String reduceFunction, @NotNull Class<R> tResultClass) {
        return collection.mapReduce(clientSession, mapFunction, reduceFunction, tResultClass);
    }
    @Override @NotNull
    public BulkWriteResult bulkWrite(@NotNull List<? extends WriteModel<? extends T>> requests) {
        return collection.bulkWrite(requests);
    }
    @Override @NotNull
    public BulkWriteResult bulkWrite(@NotNull List<? extends WriteModel<? extends T>> requests, @NotNull BulkWriteOptions options) {
        return collection.bulkWrite(requests, options);
    }
    @Override @NotNull
    public BulkWriteResult bulkWrite(@NotNull ClientSession clientSession, @NotNull List<? extends WriteModel<? extends T>> requests) {
        return collection.bulkWrite(clientSession, requests);
    }
    @Override @NotNull
    public BulkWriteResult bulkWrite(@NotNull ClientSession clientSession, @NotNull List<? extends WriteModel<? extends T>> requests, @NotNull BulkWriteOptions options) {
        return collection.bulkWrite(clientSession, requests, options);
    }
    @Override @NotNull
    public InsertOneResult insertOne(@NotNull T t) {
        return collection.insertOne(t);
    }
    @Override @NotNull
    public InsertOneResult insertOne(@NotNull T t, @NotNull InsertOneOptions options) {
        return collection.insertOne(t, options);
    }
    @Override @NotNull
    public InsertOneResult insertOne(@NotNull ClientSession clientSession, @NotNull T t) {
        return collection.insertOne(clientSession, t);
    }
    @Override @NotNull
    public InsertOneResult insertOne(@NotNull ClientSession clientSession, @NotNull T t, @NotNull InsertOneOptions options) {
        return collection.insertOne(clientSession, t, options);
    }
    @Override @NotNull
    public InsertManyResult insertMany(@NotNull List<? extends T> ts) {
        return collection.insertMany(ts);
    }
    @Override @NotNull
    public InsertManyResult insertMany(@NotNull List<? extends T> ts, @NotNull InsertManyOptions options) {
        return collection.insertMany(ts, options);
    }
    @Override @NotNull
    public InsertManyResult insertMany(@NotNull ClientSession clientSession, @NotNull List<? extends T> ts) {
        return collection.insertMany(clientSession, ts);
    }
    @Override @NotNull
    public InsertManyResult insertMany(@NotNull ClientSession clientSession, @NotNull List<? extends T> ts, @NotNull InsertManyOptions options) {
        return collection.insertMany(clientSession, ts, options);
    }
    @Override @NotNull
    public DeleteResult deleteMany(@NotNull Bson filter) {
        return collection.deleteMany(filter);
    }
    @Override @NotNull
    public DeleteResult deleteMany(@NotNull Bson filter, @NotNull DeleteOptions options) {
        return collection.deleteMany(filter, options);
    }
    @Override @NotNull
    public DeleteResult deleteMany(@NotNull ClientSession clientSession, @NotNull Bson filter) {
        return collection.deleteMany(clientSession, filter);
    }
    @Override @NotNull
    public DeleteResult deleteMany(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull DeleteOptions options) {
        return collection.deleteMany(clientSession, filter, options);
    }
    @Override @NotNull
    public DeleteResult deleteOne(@NotNull Bson filter) {
        return collection.deleteOne(filter);
    }
    @Override @NotNull
    public DeleteResult deleteOne(@NotNull Bson filter, @NotNull DeleteOptions options) {
        return collection.deleteOne(filter, options);
    }
    @Override @NotNull
    public DeleteResult deleteOne(@NotNull ClientSession clientSession, @NotNull Bson filter) {
        return collection.deleteOne(clientSession, filter);
    }
    @Override @NotNull
    public DeleteResult deleteOne(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull DeleteOptions options) {
        return collection.deleteOne(clientSession, filter, options);
    }
    @Override @NotNull
    public UpdateResult replaceOne(@NotNull Bson filter, @NotNull T replacement) {
        return collection.replaceOne(filter, replacement);
    }
    @Override @NotNull
    public UpdateResult replaceOne(@NotNull Bson filter, @NotNull T replacement, @NotNull ReplaceOptions replaceOptions) {
        return collection.replaceOne(filter, replacement, replaceOptions);
    }
    @Override @NotNull
    public UpdateResult replaceOne(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull T replacement) {
        return collection.replaceOne(clientSession, filter, replacement);
    }
    @Override @NotNull
    public UpdateResult replaceOne(@NotNull ClientSession clientSession, @NotNull Bson filter, @NotNull T replacement, @NotNull ReplaceOptions replaceOptions) {
        return collection.replaceOne(clientSession, filter, replacement, replaceOptions);
    }
}
