package xyz.srnyx.magicmongo.builders;

import com.mongodb.client.model.Filters;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BinaryOperator;


public class FilterBuilder {
    /**
     * The {@link Bson filter} to build
     */
    @Nullable private Bson filter;

    /**
     * Creates a new {@link FilterBuilder} instance with no starting {@link #filter}
     */
    public FilterBuilder() {}

    /**
     * Creates a new {@link FilterBuilder} instance with the given {@link #filter}
     *
     * @param   filter  the {@link Bson filter} to start with
     */
    public FilterBuilder(@Nullable Bson filter) {
        this.filter = filter;
    }

    /**
     * Returns the final/built {@link Bson filter}
     *
     * @return                          the final/built {@link Bson filter}
     *
     * @throws  IllegalStateException   if the {@link #filter} is null (only ever happens if {@link FilterBuilder the builder} is created with no starting {@link #filter})
     */
    @NotNull
    public Bson build() {
        if (filter == null) throw new IllegalStateException("filter cannot be null");
        return filter;
    }

    /**
     * Adds a new {@link Bson filter} to the current {@link #filter}
     *
     * @param   filterFunction  the {@link BinaryOperator} to apply to the current {@link #filter} with the new {@link Bson filter}
     * @param   newFilter       the new {@link Bson filter} to add
     *
     * @return                  the current {@link FilterBuilder}
     */
    @NotNull
    public FilterBuilder add(@NotNull BinaryOperator<Bson> filterFunction, @NotNull Bson newFilter) {
        filter = filterFunction.apply(filter, newFilter);
        return this;
    }

    /**
     * Adds a new {@link Bson filter} to the current {@link #filter} using {@link Filters#and(Bson...)}
     *
     * @param   newFilter   the new {@link Bson filter} to add
     *
     * @return              the current {@link FilterBuilder} instance
     */
    @NotNull
    public FilterBuilder and(@NotNull Bson newFilter) {
        return add(Filters::and, newFilter);
    }

    /**
     * Adds a new {@link Bson filter} to the current {@link #filter} using {@link Filters#or(Bson...)}
     *
     * @param   newFilter   the new {@link Bson filter} to add
     *
     * @return              the current {@link FilterBuilder} instance
     */
    @NotNull
    public FilterBuilder or(@NotNull Bson newFilter) {
        return add(Filters::or, newFilter);
    }

    /**
     * Adds a new {@link Bson filter} to the current {@link #filter} using {@link Filters#nor(Bson...)}
     *
     * @param   newFilter   the new {@link Bson filter} to add
     *
     * @return              the current {@link FilterBuilder} instance
     */
    @NotNull
    public FilterBuilder nor(@NotNull Bson newFilter) {
        return add(Filters::nor, newFilter);
    }
}
