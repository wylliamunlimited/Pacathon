package com.buaisociety.pacman.event;

import com.buaisociety.pacman.util.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Called when pacman eats all the dots in the maze, and the next level starts loading.
 *
 * <p>This event does not inherit from {@link PacmanEvent}, since it is not associated
 * with any maze (We are between mazes at this state).
 */
public class NextLevelEvent implements Event {

    private @NotNull String nextLevel;

    public NextLevelEvent(@NotNull String nextLevel) {
        this.nextLevel = nextLevel;
    }

    /**
     * Returns the name of the next level that will be loaded.
     *
     * <p>This name will match up with one of the folders in
     * the <code>./assets/mazes</code> directory.
     *
     * @return the name of the next level that will be loaded
     */
    public @NotNull String getNextLevel() {
        return nextLevel;
    }

    /**
     * Changes the next level that will be loaded.
     *
     * <p>Ensure that the level name matches up with one of the folders in
     * the <code>./assets/mazes</code> directory. If the level name is invalid,
     * the game will crash.
     *
     * @param nextLevel the name of the next level that will be loaded
     */
    public void setNextLevel(@NotNull String nextLevel) {
        this.nextLevel = nextLevel;
    }
}
