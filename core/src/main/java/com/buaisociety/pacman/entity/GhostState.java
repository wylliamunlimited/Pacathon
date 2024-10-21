package com.buaisociety.pacman.entity;

import org.joml.Vector2i;
import org.joml.Vector2ic;

/**
 * Holds the possible states of a ghost.
 */
public enum GhostState {

    /**
     * When the ghost is using its chase behavior
     */
    CHASE(0, 0, true, 2),

    /**
     * When the ghost is moving to its corner of the maze (same as chase)
     */
    SCATTER(0, 0, true, 2),

    /**
     * When Pacman eats a power pellet (sprites have no direction).
     */
    FRIGHTENED(0, 1, false, 2),

    /**
     * When the ghost is eaten by the player (has no animation).
     */
    EATEN(2, 1, true, 1);


    private final Vector2ic spriteOffset;
    private final boolean directional;
    private final int animationFrames;

    GhostState(int spriteX, int spriteY, boolean directional, int animationFrames) {
        this.spriteOffset = new Vector2i(spriteX, spriteY);
        this.directional = directional;
        this.animationFrames = animationFrames;
    }

    public Vector2ic getSpriteOffset() {
        return spriteOffset;
    }

    public boolean isDirectional() {
        return directional;
    }

    public int getAnimationFrames() {
        return animationFrames;
    }
}
