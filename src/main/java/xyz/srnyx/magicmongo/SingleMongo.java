package xyz.srnyx.magicmongo;

import org.bson.codecs.configuration.CodecRegistry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A {@link MagicMongo} instance with a single {@link MagicDatabase} that's specified in the connection URL
 */
public class SingleMongo extends MagicMongo {
    /**
     * The {@link MagicDatabase} instance for the single database
     */
    @NotNull public final MagicDatabase database;

    public SingleMongo(@NotNull String connectionUrl, @Nullable CodecRegistry codecRegistry) {
        super(connectionUrl, codecRegistry);

        // Load database
        final String databaseName = connection.getDatabase();
        if (databaseName == null) throw new IllegalArgumentException("No database name found in connection URL: " + connectionUrl);
        database = newMagicDatabase(databaseName);
    }
}
