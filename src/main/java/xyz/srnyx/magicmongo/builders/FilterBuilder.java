package xyz.srnyx.magicmongo.builders;

import com.mongodb.client.model.Filters;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;

import java.util.function.BinaryOperator;


/**
 * A simple builder for {@link Filters MongoDB filters}
 */
public class FilterBuilder extends MongoBsonBuilder<FilterBuilder> {
    /**
     * Creates a new {@link FilterBuilder} instance with no starting filter
     */
    public FilterBuilder() {}

    /**
     * Creates a new {@link FilterBuilder} instance with the given filter
     *
     * @param   filter  the {@link Bson filter} to start with
     */
    public FilterBuilder(@NotNull Bson filter) {
        super(filter);
    }

    @NotNull @Override
    public Bson ifNull() {
        return Filters.empty();
    }

    /**
     * Adds a new {@link Bson filter} to the current filter
     *
     * @param   filterFunction  the {@link BinaryOperator} to apply to the current filter with the new {@link Bson filter}
     * @param   newFilter       the new {@link Bson filter} to add
     *
     * @return                  the current {@link FilterBuilder}
     */
    @NotNull
    public FilterBuilder add(@NotNull BinaryOperator<Bson> filterFunction, @NotNull Bson newFilter) {
        return set(bson == null ? newFilter : filterFunction.apply(bson, newFilter));
    }

    /**
     * Adds a new {@link Bson filter} to the current filter using {@link Filters#and(Bson...)}
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
     * Adds a new {@link Bson filter} to the current filter using {@link Filters#or(Bson...)}
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
     * Adds a new {@link Bson filter} to the current filter using {@link Filters#nor(Bson...)}
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
