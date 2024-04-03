package xyz.srnyx.magicmongo.builders;

import com.mongodb.client.model.Sorts;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A simple builder for {@link Sorts MongoDB sorts}
 */
public class SortBuilder extends MongoBsonBuilder<SortBuilder> {
    /**
     * Creates a new {@link SortBuilder} instance with no starting sort
     */
    public SortBuilder() {}

    /**
     * Creates a new {@link SortBuilder} instance with the given sort
     *
     * @param   sort    the {@link Bson sort} to start with
     */
    public SortBuilder(@NotNull Bson sort) {
        super(sort);
    }

    /**
     * Creates a new {@link SortBuilder} instance with the given sorts
     *
     * @param   sorts the {@link Bson sorts} to start with
     */
    public SortBuilder(@NotNull Bson... sorts) {
        super(Sorts.orderBy(sorts));
    }

    /**
     * Creates a new {@link SortBuilder} instance with the given {@link List} of sorts
     *
     * @param   sorts the {@link List} of {@link Bson sorts} to start with
     */
    public SortBuilder(@NotNull List<Bson> sorts) {
        super(Sorts.orderBy(sorts));
    }

    /**
     * Creates a new {@link SortBuilder} instance with the given {@link SortBuilder}
     *
     * @param   sortBuilder the {@link SortBuilder} to start with
     */
    public SortBuilder(@NotNull SortBuilder sortBuilder) {
        super(sortBuilder.bson);
    }

    /**
     * Combines the given {@link Bson sort} with the {@link #bson current sort} using {@link Sorts#orderBy(Bson...)}
     *
     * @param   newSort the {@link Bson sort} to add
     *
     * @return          the current {@link SortBuilder} instance
     */
    @NotNull
    public SortBuilder add(@NotNull Bson newSort) {
        return set(bson == null ? newSort : Sorts.orderBy(bson, newSort));
    }
}
