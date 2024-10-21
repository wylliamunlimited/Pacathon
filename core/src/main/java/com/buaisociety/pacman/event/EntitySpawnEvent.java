package com.buaisociety.pacman.event;

import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.entity.EntityType;
import com.buaisociety.pacman.util.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an entity is about to be spawned in the maze.
 */
public class EntitySpawnEvent extends PacmanEvent implements Cancellable {

    private @NotNull Entity entity;
    private boolean cancelled;

    public EntitySpawnEvent(@NotNull Entity entity) {
        super(entity.getMaze());
        this.entity = entity;
    }

    /**
     * Returns the entity that will be spawned.
     *
     * @return the entity that will be spawned.
     */
    public @NotNull Entity getEntity() {
        return entity;
    }

    /**
     * Overrides the entity to be spawned.
     *
     * @param entity the entity to be spawned.
     */
    public void setEntity(@NotNull Entity entity) {
        this.entity = entity;
    }

    /**
     * Returns the type of entity that was spawned.
     *
     * @return the type of entity that was spawned.
     */
    public @NotNull EntityType getEntityType() {
        return entity.getType();
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
