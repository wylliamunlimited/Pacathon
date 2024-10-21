package com.buaisociety.pacman.entity.behavior;

import com.buaisociety.pacman.entity.Direction;
import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.maze.Maze;
import com.buaisociety.pacman.maze.Tile;
import org.jetbrains.annotations.NotNull;
import org.joml.RoundingMode;
import org.joml.Vector2i;

/**
 * A behavior that targets a tile in the maze instead of a direction.
 *
 * <p>The direction will be chosen based on which direction immediately makes
 * the entity closer to the target tile.
 */
public interface TargetableBehavior extends Behavior {

    /**
     * For internal use only -- DO NOT MODIFY
     *
     * <p>the <code>.values()</code> is extremely inefficient. Cache.
     */
    @NotNull Direction[] DIRECTIONS = Direction.values();

    @Override
    default @NotNull Direction getDirection(@NotNull Entity entity) {
        Vector2i target = getTarget(entity);

        Direction temp = null;
        int smallest = Integer.MAX_VALUE;

        Tile current = entity.getMaze().getTile(entity.getTilePosition());
        for (Direction direction : DIRECTIONS) {
            // Ghosts may not reverse direction
            if (entity.getDirection().behind() == direction)
                continue;

            Tile next = current.getNeighbor(direction);
            if (!next.getState().isPassable())
                continue;

            Vector2i location = new Vector2i(entity.getPosition().div(Maze.TILE_SIZE), RoundingMode.TRUNCATE).add(direction.asVector());
            int distance = (int) location.distanceSquared(target);

            if (distance <= smallest) {
                smallest = distance;
                temp = direction;
            }
        }

        if (temp == null)
            return entity.getDirection();

        return temp;
    }

    @NotNull Vector2i getTarget(@NotNull Entity entity);
}
