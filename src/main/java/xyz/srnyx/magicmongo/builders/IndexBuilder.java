package xyz.srnyx.magicmongo.builders;

import com.mongodb.client.model.Indexes;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A simple builder for {@link Indexes MongoDB indexes}
 */
public class IndexBuilder extends MongoBsonBuilder<IndexBuilder> {
    /**
     * Creates a new {@link IndexBuilder} instance with no starting index
     */
    public IndexBuilder() {}
    
    /**
     * Creates a new {@link IndexBuilder} instance with the given index
     *
     * @param   index   the {@link Bson index} to start with
     */
    public IndexBuilder(@NotNull Bson index) {
        super(index);
    }

    /**
     * Creates a new {@link IndexBuilder} instance with the given indexes
     *
     * @param   indexes the {@link Bson indexes} to start with
     */
    public IndexBuilder(@NotNull Bson... indexes) {
        super(Indexes.compoundIndex(indexes));
    }

    /**
     * Creates a new {@link IndexBuilder} instance with the given {@link List} of indexes
     *
     * @param   indexes the {@link List} of {@link Bson indexes} to start with
     */
    public IndexBuilder(@NotNull List<Bson> indexes) {
        super(Indexes.compoundIndex(indexes));
    }

    /**
     * Creates a new {@link IndexBuilder} instance with the given {@link IndexBuilder}
     *
     * @param   indexBuilder    the {@link IndexBuilder} to start with
     */
    public IndexBuilder(@NotNull IndexBuilder indexBuilder) {
        super(indexBuilder.bson);
    }

    /**
     * Combines the given {@link Bson index} with the {@link #bson current index} using {@link Indexes#compoundIndex(Bson...)}
     *
     * @param   newIndex    the {@link Bson index} to add
     *
     * @return              the current {@link IndexBuilder} instance
     */
    @NotNull
    public IndexBuilder add(@NotNull Bson newIndex) {
        return set(bson == null ? newIndex : Indexes.compoundIndex(bson, newIndex));
    }
}
