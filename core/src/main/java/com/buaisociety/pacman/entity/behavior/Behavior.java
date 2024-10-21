package com.buaisociety.pacman.entity.behavior;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buaisociety.pacman.entity.Direction;
import com.buaisociety.pacman.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * Determines how an entity should move.
 *
 * <p>All entities in Pacman have simple inputs; move up, down, left, or right.
 * Every frame, the entity must decide which direction to move in. This interface
 * provides a way to encapsulate this decision-making process.
 */
public interface Behavior {

    /**
     * Returns the desired direction that the entity should move towards.
     *
     * @param entity the entity to get the direction for
     * @return the desired direction for the entity
     */
    @NotNull Direction getDirection(@NotNull Entity entity);

    /**
     * Renders the behavior, if applicable.
     *
     * <p>This is typically only used to debug the behavior. An entity's
     * behavior is rendered BEFORE the entity is rendered (so the entity
     * may be rendered on top of the behavior).
     *
     * @param batch the sprite batch to render with
     */
    default void render(@NotNull SpriteBatch batch) {
    }
}
