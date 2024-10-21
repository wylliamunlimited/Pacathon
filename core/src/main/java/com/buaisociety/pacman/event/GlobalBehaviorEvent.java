package com.buaisociety.pacman.event;

import com.buaisociety.pacman.entity.GhostState;
import com.buaisociety.pacman.maze.Maze;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the {@link Maze} switches between {@link GhostState#SCATTER} and
 * {@link GhostState#CHASE}. This causes the ghosts to switch directions, and
 * switch modes.
 */
public class GlobalBehaviorEvent extends PacmanEvent {

    private boolean isChaseBehavior;
    private int timeLeft;

    public GlobalBehaviorEvent(@NotNull Maze maze, boolean isChaseBehavior, int timeLeft) {
        super(maze);
        this.isChaseBehavior = isChaseBehavior;
        this.timeLeft = timeLeft;
    }

    /**
     * Returns <code>true</code> if the next behavior state is {@link GhostState#CHASE},
     * otherwise returns <code>false</code> if the next behavior state is
     * {@link GhostState#SCATTER}.
     *
     * @return whether the next behavior state is chase or scatter
     */
    public boolean isChaseBehavior() {
        return isChaseBehavior;
    }

    /**
     * Sets whether the next behavior state is {@link GhostState#CHASE} or
     * {@link GhostState#SCATTER}.
     *
     * @param isChaseBehavior whether the next behavior state is chase or scatter
     */
    public void setChaseBehavior(boolean isChaseBehavior) {
        this.isChaseBehavior = isChaseBehavior;
    }

    /**
     * Returns the time left, in frames, of the next behavior state.
     *
     * @return the time left in the current behavior state
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Sets the time left, in frames, of the next behavior state.
     *
     * <p>Settings this value to a negative number, like -1, will cause the
     * ghosts to remain in this next state for the rest of the maze (until
     * the next round).
     *
     * @param timeLeft the time left in the current behavior state
     */
    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }
}
