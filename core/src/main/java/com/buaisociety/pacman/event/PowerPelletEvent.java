package com.buaisociety.pacman.event;

import com.buaisociety.pacman.entity.PacmanEntity;
import com.buaisociety.pacman.maze.Tile;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a power pellet is eaten by pacman.
 */
public class PowerPelletEvent extends PacmanEvent {

    private final @NotNull PacmanEntity pacman;
    private final @NotNull Tile powerPellet;
    private int addScore;
    private int frightDuration;

    public PowerPelletEvent(@NotNull PacmanEntity pacman, @NotNull Tile powerPellet, int addScore, int frightDuration) {
        super(pacman.getMaze());
        this.pacman = pacman;
        this.powerPellet = powerPellet;
        this.addScore = addScore;
        this.frightDuration = frightDuration;
    }

    /**
     * Returns the pacman entity that ate the power pellet.
     *
     * @return the pacman entity that ate the power pellet.
     */
    public @NotNull PacmanEntity getPacman() {
        return pacman;
    }

    /**
     * Returns the power pellet that was eaten.
     *
     * @return the power pellet that was eaten.
     */
    public @NotNull Tile getPowerPellet() {
        return powerPellet;
    }

    /**
     * The amount of score added when the power pellet is eaten. Typically, this
     * is 50 points.
     *
     * @return the amount of score added when the power pellet is eaten.
     */
    public int getAddScore() {
        return addScore;
    }

    /**
     * Overrides the amount of score added when the power pellet is eaten.
     *
     * @param addScore the amount of score added when the power pellet is eaten.
     */
    public void setAddScore(int addScore) {
        this.addScore = addScore;
    }

    /**
     * The duration that the ghosts are frightened for when the power pellet is
     *
     * @return the duration that the ghosts are frightened for when the power
     */
    public int getFrightDuration() {
        return frightDuration;
    }

    public void setFrightDuration(int frightDuration) {
        this.frightDuration = frightDuration;
    }
}
