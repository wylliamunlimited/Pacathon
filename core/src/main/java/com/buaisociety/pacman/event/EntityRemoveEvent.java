package com.buaisociety.pacman.event;

import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.entity.FruitEntity;
import com.buaisociety.pacman.util.Cancellable;

/**
 * Called when an entity is removed from the maze. This event is only called if
 * an entity is removed from the list of entities in the maze. This means that
 * only {@link FruitEntity fruits} will trigger this event.
 */
public class EntityRemoveEvent extends PacmanEvent implements Cancellable {

    private final Entity entity;

    private boolean cancelled;

    public EntityRemoveEvent(Entity entity) {
        super(entity.getMaze());
        this.entity = entity;
    }

    /**
     * Returns the entity that was removed from the maze.
     *
     * @return the entity that was removed from the maze
     */
    public Entity getEntity() {
        return entity;
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
