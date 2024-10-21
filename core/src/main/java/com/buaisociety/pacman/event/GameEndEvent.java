package com.buaisociety.pacman.event;

import com.buaisociety.pacman.GameManager;
import com.buaisociety.pacman.util.Event;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the game ends (Pacman has no more lives).
 */
public class GameEndEvent implements Event {

    private final @NotNull GameManager gameManager;

    public GameEndEvent(@NotNull GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Returns the game manager that is associated with the game.
     *
     * @return the game manager that is associated with the game.
     */
    public @NotNull GameManager getGameManager() {
        return gameManager;
    }
}
