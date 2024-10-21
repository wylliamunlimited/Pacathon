package com.buaisociety.pacman.event;

import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.entity.EntityType;
import com.buaisociety.pacman.maze.Maze;
import com.buaisociety.pacman.util.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Called before an entity is created to be spawned in the maze.
 *
 * <p>Since no {@link Entity} instance exists yet, this event is only used to
 * modify the configuration of the entity before it is spawned. To modify the
 * entity instance directly, use {@link EntitySpawnEvent} instead.
 *
 * <p>The configuration object is specific to the {@link EntityType}. Check the
 * {@link EntityType#getConfigClass()} for the expected configuration object.
 */
public class EntityPreSpawnEvent extends PacmanEvent implements Cancellable {

    private final @NotNull EntityType type;
    private @NotNull Object config;

    private boolean cancelled;

    public EntityPreSpawnEvent(@NotNull Maze maze, @NotNull EntityType type, @NotNull Object config) {
        super(maze);
        this.type = type;
        this.config = config;
    }

    /**
     * Returns the type of entity that will be spawned.
     *
     * @return the type of entity that will be spawned
     */
    public @NotNull EntityType getEntityType() {
        return type;
    }

    /**
     * Returns the configuration of the entity that will be spawned.
     *
     * <p>The type of configuration object depends on the {@link EntityType}. Check
     * the {@link EntityType#getConfigClass()}.
     *
     * @return the configuration of the entity that will be spawned
     */
    public @NotNull Object getConfig() {
        return config;
    }

    /**
     * Sets the configuration of the entity that will be spawned.
     *
     * <p>The type of configuration object depends on the {@link EntityType}. Check
     * the {@link EntityType#getConfigClass()}.
     *
     * @param config the configuration of the entity that will be spawned
     */
    public void setConfig(@NotNull Object config) {
        if (config.getClass() != type.getConfigClass()) {
            throw new IllegalArgumentException("Invalid configuration object for entity type, expected "
                + type.getConfigClass().getSimpleName() + " but got " + config.getClass().getSimpleName());
        }

        this.config = config;
    }

    /**
     * Sets whether the event is cancelled.
     *
     * @param cancelled {@code true} if the event is cancelled, {@code false} otherwise.
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Returns whether the event is cancelled.
     *
     * @return {@code true} if the event is cancelled, {@code false} otherwise.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
