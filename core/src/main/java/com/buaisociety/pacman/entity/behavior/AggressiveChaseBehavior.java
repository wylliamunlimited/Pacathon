package com.buaisociety.pacman.entity.behavior;

import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.entity.PacmanEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

/**
 * A behavior to chase the target directly.
 *
 * <p>This behavior is used by blinky (the red ghost) to chase Pacman directly.
 */
public class AggressiveChaseBehavior implements TargetableBehavior {

    @Override
    public @NotNull Vector2i getTarget(@NotNull Entity entity) {
        PacmanEntity pacman = entity.getMaze().getPacman();
        return pacman.getTilePosition();
    }
}
