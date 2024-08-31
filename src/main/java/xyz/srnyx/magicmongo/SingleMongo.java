package xyz.srnyx.magicmongo;

import org.bson.codecs.configuration.CodecRegistry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * A {@link MagicMongo} instance with a single {@link MagicDatabase} that's specified in the connection URL
 */
public class SingleMongo extends MagicMongo {
    /**
     * The {@link MagicDatabase} that's specified in the connection URL
     */
    @NotNull public final MagicDatabase database;

    public SingleMongo(@NotNull String connectionUrl, @Nullable CodecRegistry codecRegistry) {
        super(connectionUrl, codecRegistry);
        database = Objects.requireNonNull(databases.get(connection.getDatabase()), "Database name not specified in connection URL: " + connectionUrl);
    }

    public SingleMongo(@NotNull String connectionUrl) {
        this(connectionUrl, getDefaultCodecRegistry());
    }
}
