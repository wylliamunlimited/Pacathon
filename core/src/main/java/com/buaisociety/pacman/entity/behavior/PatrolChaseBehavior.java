package com.buaisociety.pacman.entity.behavior;

import com.buaisociety.pacman.entity.Direction;
import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.entity.GhostEntity;
import com.buaisociety.pacman.entity.GhostState;
import com.buaisociety.pacman.entity.PacmanEntity;
import com.buaisociety.pacman.maze.Maze;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

/**
 * This behavior coordinates with the
 */
public class PatrolChaseBehavior implements TargetableBehavior {

    private @Nullable GhostEntity blinky;

    public @NotNull GhostEntity findBlinky(@NotNull Maze maze) {
        for (Entity entity : maze.getEntities()) {
            if (entity instanceof GhostEntity ghost && ghost.getBehavior(GhostState.CHASE) instanceof AggressiveChaseBehavior) {
                return ghost;
            }
        }

        throw new IllegalStateException("No ghost found in maze");
    }

    @NotNull
    @Override
    public Vector2i getTarget(@NotNull Entity entity) {
        Maze maze = entity.getMaze();
        if (blinky == null) {
            blinky = findBlinky(maze);
        }

        PacmanEntity pacman = maze.getPacman();
        Direction dir = pacman.getDirection();
        Vector2i target = pacman.getTilePosition().add(dir.getDx() * 2, dir.getDy() * 2);

        Vector2i between = target.sub(blinky.getTilePosition());
        between.mul(2).add(blinky.getTilePosition());
        return between;
    }
}
