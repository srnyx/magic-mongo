package xyz.srnyx.magicmongo;

import com.mongodb.client.model.Updates;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class UpdateBuilder {
    @Nullable private Bson update;

    public UpdateBuilder(@NotNull Bson update) {
        this.update = update;
    }

    public UpdateBuilder(@NotNull Bson... updates) {
        update = Updates.combine(updates);
    }

    public UpdateBuilder(@NotNull List<Bson> updates) {
        update = Updates.combine(updates);
    }

    public UpdateBuilder(@NotNull UpdateBuilder updateBuilder) {
        this.update = updateBuilder.update;
    }

    public UpdateBuilder() {}

    @NotNull
    public UpdateBuilder add(@NotNull Bson newUpdate) {
        update = Updates.combine(update, newUpdate);
        return this;
    }

    @NotNull
    public Bson build() {
        if (update == null) throw new IllegalStateException("update cannot be null");
        return update;
    }
}
