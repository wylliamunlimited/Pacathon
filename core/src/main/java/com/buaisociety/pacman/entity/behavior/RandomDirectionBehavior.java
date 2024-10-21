package com.buaisociety.pacman.entity.behavior;

import com.buaisociety.pacman.entity.Direction;
import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.maze.Tile;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A behavior that randomly chooses a direction to move in. This is used by the
 * ghosts when they are in the frightened state.
 */
public class RandomDirectionBehavior implements Behavior {

    private final Direction[] directions = Direction.values();

    @NotNull
    @Override
    public Direction getDirection(@NotNull Entity entity) {
        int index = ThreadLocalRandom.current().nextInt(directions.length);
        Direction direction = directions[index];

        // Make sure that tile is passable
        while (!isValidDirection(entity, direction)) {
            direction = direction.right();  // clockwise rotation
        }

        return direction;
    }

    public boolean isValidDirection(@NotNull Entity entity, @NotNull Direction direction) {
        Tile current = entity.getMaze().getTile(entity.getTilePosition());
        boolean isPassable = current.getNeighbor(direction).getState().isPassable();
        boolean isNotBehind = entity.getDirection().behind() != direction;
        return isPassable && isNotBehind;
    }
}
