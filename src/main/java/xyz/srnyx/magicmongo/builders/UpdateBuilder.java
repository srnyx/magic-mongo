package xyz.srnyx.magicmongo.builders;

import com.mongodb.client.model.Updates;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * A simple builder for {@link Updates MongoDB updates}
 */
public class UpdateBuilder {
    /**
     * The {@link Bson update} to build
     */
    @Nullable private Bson update;

    /**
     * Creates a new {@link UpdateBuilder} instance with no starting {@link #update}
     */
    public UpdateBuilder() {}

    /**
     * Creates a new {@link UpdateBuilder} instance with the given {@link #update}
     *
     * @param   update  the {@link Bson update} to start with
     */
    public UpdateBuilder(@Nullable Bson update) {
        this.update = update;
    }

    /**
     * Creates a new {@link UpdateBuilder} instance with the given {@link #update updates}
     *
     * @param   updates the {@link Bson updates} to start with
     */
    public UpdateBuilder(@NotNull Bson... updates) {
        update = Updates.combine(updates);
    }

    /**
     * Creates a new {@link UpdateBuilder} instance with the given {@link List} of {@link #update}
     *
     * @param   updates the {@link List} of {@link Bson updates} to start with
     */
    public UpdateBuilder(@NotNull List<Bson> updates) {
        update = Updates.combine(updates);
    }

    /**
     * Creates a new {@link UpdateBuilder} instance with the given {@link UpdateBuilder}
     *
     * @param   updateBuilder    the {@link UpdateBuilder} to start with
     */
    public UpdateBuilder(@NotNull UpdateBuilder updateBuilder) {
        this.update = updateBuilder.update;
    }

    /**
     * Returns the final/built {@link Bson update}
     *
     * @return                          the final/built {@link Bson update}
     *
     * @throws  IllegalStateException   if the {@link #update} is null (only ever happens if {@link UpdateBuilder the builder} is created with no starting {@link #update})
     */
    @NotNull
    public Bson build() {
        if (update == null) throw new IllegalStateException("update cannot be null");
        return update;
    }

    /**
     * Combines the given {@link Bson update} with the current {@link #update}
     *
     * @param   newUpdate   the {@link Bson update} to add
     *
     * @return              the current {@link UpdateBuilder} instance
     */
    @NotNull
    public UpdateBuilder add(@NotNull Bson newUpdate) {
        update = Updates.combine(update, newUpdate);
        return this;
    }
}
