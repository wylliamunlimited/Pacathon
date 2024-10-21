package com.buaisociety.pacman.entity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

/**
 * Represents the 4 cardinal directions, UP, DOWN, LEFT, RIGHT. Each direction
 * has a dx and dy value that represents the change in x and y coordinates when
 * moving in that direction.
 *
 * <p>The directions are ordered in such that {@link Enum#ordinal()} will return
 * the y-offset of this direction on a SpriteSheet. This is useful for rendering
 * sprites that are oriented in different directions (Like pacman, and the ghosts).
 */
public enum Direction {

    UP(0, 1),
    DOWN(0, -1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    /**
     * Returns the vector representation of this direction.
     *
     * @return the vector representation of this direction
     */
    @Contract(pure = true)
    public @NotNull Vector2i asVector() {
        return new Vector2i(dx, dy);
    }

    /**
     * Returns whether this direction is horizontal ({@link #LEFT} or {@link #RIGHT}).
     *
     * @return true if this direction is horizontal, false otherwise
     */
    @Contract(pure = true)
    public boolean isHorizontal() {
        return dy == 0;
    }

    /**
     * Returns whether this direction is vertical ({@link #UP} or {@link #DOWN}).
     *
     * @return true if this direction is vertical, false otherwise
     */
    @Contract(pure = true)
    public boolean isVertical() {
        return dx == 0;
    }

    /**
     * Returns the direction that is 90 degrees to the left of this direction.
     *
     * @return the direction to the left of this direction
     */
    @Contract(pure = true)
    public @NotNull Direction left() {
        return switch (this) {
            case UP -> LEFT;
            case DOWN -> RIGHT;
            case LEFT -> DOWN;
            case RIGHT -> UP;
        };
    }

    /**
     * Returns the direction that is 90 degrees to the right of this direction.
     *
     * @return the direction to the right of this direction
     */
    @Contract(pure = true)
    public @NotNull Direction right() {
        return switch (this) {
            case UP -> RIGHT;
            case DOWN -> LEFT;
            case LEFT -> UP;
            case RIGHT -> DOWN;
        };
    }

    /**
     * Returns the direction that is 180 degrees behind this direction.
     *
     * @return the direction behind this direction
     */
    @Contract(pure = true)
    public @NotNull Direction behind() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
