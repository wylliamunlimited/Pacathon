package com.buaisociety.pacman.entity.behavior;

import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.entity.GhostEntity;
import com.buaisociety.pacman.entity.PacmanEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class FeignChaseBehavior implements TargetableBehavior {
    @NotNull
    @Override
    public Vector2i getTarget(@NotNull Entity entity) {
        PacmanEntity pacman = entity.getMaze().getPacman();
        Vector2i target = pacman.getTilePosition();

        if (target.distanceSquared(entity.getTilePosition()) > 64) {
            return target;
        } else {
            return ((GhostEntity) entity).getScatterTile();
        }
    }
}
