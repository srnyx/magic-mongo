package xyz.srnyx.magicmongo;

import org.bson.codecs.configuration.CodecRegistry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * A {@link MagicMongo} instance with multiple {@link MagicDatabase MagicDatabases} that can be loaded and accessed
 */
public class MultiMongo extends MagicMongo {
    /**
     * The {@link MagicDatabase MagicDatabases} that have been loaded
     */
    @NotNull public final Map<String, MagicDatabase> databases = new HashMap<>();

    public MultiMongo(@NotNull String connectionUrl, @Nullable CodecRegistry codecRegistry) {
        super(connectionUrl, codecRegistry);

        // Load database if specified
        final String databaseName = connection.getDatabase();
        if (databaseName != null) loadMagicDatabase(databaseName);
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
     * @return          this {@link MultiMongo} instance
     */
    @NotNull
    public MultiMongo loadMagicDatabases(@NotNull Iterable<String> toLoad) {
        toLoad.forEach(this::loadMagicDatabase);
        return this;
    }

    /**
     * Constructs new {@link MagicDatabase MagicDatabases} with the specified names and loads them
     *
     * @param   toLoad  the names of the databases to create
     *
     * @return          this {@link MultiMongo} instance
     */
    @NotNull
    public MultiMongo loadMagicDatabases(@NotNull String... toLoad) {
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
