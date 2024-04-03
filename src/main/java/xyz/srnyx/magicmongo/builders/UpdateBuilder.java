package xyz.srnyx.magicmongo.builders;

import com.mongodb.client.model.Updates;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A simple builder for {@link Updates MongoDB updates}
 */
public class UpdateBuilder extends MongoBsonBuilder<UpdateBuilder> {
    /**
     * Creates a new {@link UpdateBuilder} instance with no starting update
     */
    public UpdateBuilder() {}

    /**
     * Creates a new {@link UpdateBuilder} instance with the given update
     *
     * @param   update  the {@link Bson update} to start with
     */
    public UpdateBuilder(@NotNull Bson update) {
        super(update);
    }

    /**
     * Creates a new {@link UpdateBuilder} instance with the given updates
     *
     * @param   updates the {@link Bson updates} to start with
     */
    public UpdateBuilder(@NotNull Bson... updates) {
        super(Updates.combine(updates));
    }

    /**
     * Creates a new {@link UpdateBuilder} instance with the given {@link List} of updates
     *
     * @param   updates the {@link List} of {@link Bson updates} to start with
     */
    public UpdateBuilder(@NotNull List<Bson> updates) {
        super(Updates.combine(updates));
    }

    /**
     * Creates a new {@link UpdateBuilder} instance with the given {@link UpdateBuilder}
     *
     * @param   updateBuilder    the {@link UpdateBuilder} to start with
     */
    public UpdateBuilder(@NotNull UpdateBuilder updateBuilder) {
        super(updateBuilder.bson);
    }

    /**
     * Combines the given {@link Bson update} with the current update
     *
     * @param   newUpdate   the {@link Bson update} to add
     *
     * @return              the current {@link UpdateBuilder} instance
     */
    @NotNull
    public UpdateBuilder add(@NotNull Bson newUpdate) {
        return set(bson == null ? newUpdate : Updates.combine(bson, newUpdate));
    }
}
