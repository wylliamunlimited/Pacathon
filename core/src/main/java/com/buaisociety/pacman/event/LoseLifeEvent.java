package com.buaisociety.pacman.event;

import com.buaisociety.pacman.entity.PacmanEntity;
import com.buaisociety.pacman.util.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Called when pacman gets hit by a ghost.
 */
public class LoseLifeEvent extends PacmanEvent implements Cancellable {

    private final @NotNull PacmanEntity pacman;
    private int numLives;

    private boolean cancelled;

    public LoseLifeEvent(@NotNull PacmanEntity pacman, int numLives) {
        super(pacman.getMaze());
        this.pacman = pacman;
        this.numLives = numLives;
    }

    /**
     * The pacman entity that will lose a life.
     *
     * @return the pacman entity that will lose a life
     */
    public @NotNull PacmanEntity getPacman() {
        return pacman;
    }

    /**
     * Returns the new number of lives that the pacman entity will have (unless this event is cancelled).
     *
     * @return the new number of lives that the pacman entity will have
     */
    public int getNumLives() {
        return numLives;
    }

    /**
     * Overrides the new number of lives that the pacman entity will have.
     *
     * @param numLives the new number of lives that the pacman entity will have
     */
    public void setNumLives(int numLives) {
        this.numLives = numLives;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
