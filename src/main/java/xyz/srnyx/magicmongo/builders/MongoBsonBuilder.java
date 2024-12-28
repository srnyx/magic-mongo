package xyz.srnyx.magicmongo.builders;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


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
     * Duplicates the given {@link MongoBsonBuilder} instance
     *
     * @param   builder the {@link MongoBsonBuilder} to duplicate
     */
    public MongoBsonBuilder(@NotNull T builder) {
        this.bson = builder.bson;
    }

    /**
     * The {@link Bson} to return if the {@link #bson} is null when {@link #build() building}
     *
     * @return  the {@link Bson} to return if the {@link #bson} is null
     */
    @NotNull
    public abstract Bson ifNull();

    @Override @NotNull
    public abstract MongoBsonBuilder<T> clone();

    /**
     * Checks if the {@link #bson} is null
     *
     * @return  whether the {@link #bson} is null
     */
    public boolean isNull() {
        return bson == null;
    }

    /**
     * Gets the {@link Bson} object
     * <br><b>You should use {@link #build()} instead!</b>
     *
     * @return  the {@link Bson} object
     *
     * @see     #build()
     */
    @NotNull
    public Optional<Bson> get() {
        return Optional.ofNullable(bson);
    }

    /**
     * Builds the {@link Bson} object
     *
     * @return  the built {@link Bson} object or the {@link #ifNull() default} if the {@link #bson} is null
     */
    @NotNull
    public Bson build() {
        return bson != null ? bson : ifNull();
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
