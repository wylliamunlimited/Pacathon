package com.buaisociety.pacman.event;

import com.buaisociety.pacman.maze.Maze;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a new maze is created (usually after a {@link NextLevelEvent}).
 */
public class CreateMazeEvent extends PacmanEvent {
    public CreateMazeEvent(@NotNull Maze maze) {
        super(maze);
    }
}
