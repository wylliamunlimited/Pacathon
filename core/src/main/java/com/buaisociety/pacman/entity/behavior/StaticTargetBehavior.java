package com.buaisociety.pacman.entity.behavior;

import com.buaisociety.pacman.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

/**
 * Targets 1 specific tile, always. This is used for the scatter behavior of the ghosts.
 */
public class StaticTargetBehavior implements TargetableBehavior {

    private final Vector2i target;

    public StaticTargetBehavior(Vector2i target) {
        this.target = target;
    }

    @Override
    public @NotNull Vector2i getTarget(@NotNull Entity entity) {
        return target;
    }
}
