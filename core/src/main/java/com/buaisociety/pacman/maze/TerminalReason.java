package com.buaisociety.pacman.maze;

/**
 * A {@link Maze} can be in a terminal state for two reasons:
 * <ul>
 *     <li>{@link #WIN} - The player has eaten all the pellets in the maze.</li>
 *     <li>{@link #LOSE} - The player has been caught by a ghost.</li>
 * </ul>
 *
 * @see Maze#getTerminalReason()
 */
public enum TerminalReason {
    /**
     * The player has eaten all the pellets in the maze.
     */
    WIN,

    /**
     * The player has been caught by a ghost.
     */
    LOSE
}
