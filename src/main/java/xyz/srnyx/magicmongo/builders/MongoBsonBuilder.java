package xyz.srnyx.magicmongo.builders;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * A simple builder for {@link Bson MongoDB Bson} objects
 *
 * @param   <T> the type of the builder
 *
 * @see     FilterBuilder
 * @see     UpdateBuilder
 * @see     SortBuilder
 */
public abstract class MongoBsonBuilder<T extends MongoBsonBuilder<T>> {
    /**
     * The {@link Bson} to build
     */
    @Nullable protected Bson bson;

    /**
     * Creates a new {@link MongoBsonBuilder} instance with no starting {@link #bson}
     */
    public MongoBsonBuilder() {}

    /**
     * Creates a new {@link MongoBsonBuilder} instance with the given {@link Bson}
     *
     * @param   bson    the {@link Bson} to start with
     */
    public MongoBsonBuilder(@Nullable Bson bson) {
        this.bson = bson;
    }

    /**
     * Builds the {@link Bson} object
     *
     * @return                          the built {@link Bson} object
     *
     * @throws  IllegalStateException   if the {@link #bson} is null
     */
    @NotNull
    public Bson build() {
        if (bson == null) throw new IllegalStateException("bson cannot be null!");
        return bson;
    }

    /**
     * Sets the {@link Bson} to the given {@link Bson}
     *
     * @param   bson    the {@link Bson} to set
     *
     * @return          the current {@link MongoBsonBuilder} instance casted to the type of the builder
     */
    @NotNull
    public T set(@Nullable Bson bson) {
        this.bson = bson;
        return (T) this;
    }
}