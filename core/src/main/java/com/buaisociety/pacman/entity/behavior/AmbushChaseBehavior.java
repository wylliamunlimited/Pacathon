package com.buaisociety.pacman.entity.behavior;

import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.entity.PacmanEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

/**
 * A behavior to chase 4 tiles ahead of the target.
 *
 * <p>This behavior is used by pinky (the pink ghost) to ambush Pacman.
 */
public class AmbushChaseBehavior implements TargetableBehavior {

    @NotNull
    @Override
    public Vector2i getTarget(@NotNull Entity entity) {
        PacmanEntity pacman = entity.getMaze().getPacman();
        Vector2i origin = pacman.getTilePosition();
        origin.add(pacman.getDirection().asVector().mul(4));
        return origin;
    }
}
