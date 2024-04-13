package xyz.srnyx.magicmongo.builders;

import com.mongodb.client.model.Projections;

import org.bson.conversions.Bson;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * A simple builder for {@link Projections MongoDB projections}
 */
public class ProjectionBuilder extends MongoBsonBuilder<ProjectionBuilder> {
    /**
     * Creates a new {@link ProjectionBuilder} instance with no starting projection
     */
    public ProjectionBuilder() {}

    /**
     * Creates a new {@link ProjectionBuilder} instance with the given projection
     *
     * @param   projection  the {@link Bson projection} to start with
     */
    public ProjectionBuilder(@NotNull Bson projection) {
        super(projection);
    }

    /**
     * Creates a new {@link ProjectionBuilder} instance with the given projections
     *
     * @param   projections the {@link Bson projections} to start with
     */
    public ProjectionBuilder(@NotNull Bson... projections) {
        super(Projections.fields(projections));
    }

    /**
     * Creates a new {@link ProjectionBuilder} instance with the given {@link List} of projections
     *
     * @param   projections the {@link List} of {@link Bson projections} to start with
     */
    public ProjectionBuilder(@NotNull List<Bson> projections) {
        super(Projections.fields(projections));
    }

    /**
     * Creates a new {@link ProjectionBuilder} instance with the given {@link ProjectionBuilder}
     *
     * @param   projectionBuilder   the {@link ProjectionBuilder} to start with
     */
    public ProjectionBuilder(@NotNull ProjectionBuilder projectionBuilder) {
        super(projectionBuilder.bson);
    }

    @NotNull @Override
    public Bson ifNull() {
        return Projections.include("_id");
    }

    /**
     * Combines the given {@link Bson projection} with the {@link #bson current projection} using {@link Projections#fields(Bson...)}
     *
     * @param   newProjection   the {@link Bson projection} to add
     *
     * @return                  the current {@link ProjectionBuilder} instance
     */
    @NotNull
    public ProjectionBuilder add(@NotNull Bson newProjection) {
        return set(bson == null ? newProjection : Projections.fields(bson, newProjection));
    }
}
