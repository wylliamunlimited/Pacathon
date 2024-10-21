package com.buaisociety.pacman.event;

import com.buaisociety.pacman.GameManager;
import com.buaisociety.pacman.maze.Maze;
import com.buaisociety.pacman.util.Event;
import org.jetbrains.annotations.NotNull;

/**
 * The base class used for all Pacman game events.
 */
public class PacmanEvent implements Event {

    private final @NotNull Maze maze;

    public PacmanEvent(@NotNull Maze maze) {
        this.maze = maze;
    }

    /**
     * Returns the maze that this event is associated with.
     *
     * @return the maze that this event is associated with.
     */
    public @NotNull Maze getMaze() {
        return maze;
    }

    /**
     * The level manager that is associated with the maze.
     *
     * @return the level manager that is associated with the maze.
     */
    public @NotNull GameManager getGameManager() {
        return maze.getLevelManager();
    }

    /**
     * The current level that the maze is on.
     *
     * @return the current level that the maze is on.
     */
    public int getLevel() {
        return maze.getLevelManager().getLevel();
    }
}
