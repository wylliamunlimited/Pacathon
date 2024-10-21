package com.buaisociety.pacman.maze;

/**
 * Represents the state of a tile on the board.
 *
 * <p>A board is just a 2d grid of tiles. Tiles can be either solid walls or
 * passable. Some entities can pass through certain tiles, while others cannot.
 */
public enum TileState {

    /**
     * Represents an empty tile. When a {@link #PELLET} or {@link #POWER_PELLET}
     * is eaten, that tile will become a SPACE tile.
     */
    SPACE(true),

    /**
     * Represents a score pellet that can be eaten by pacman.
     */
    PELLET(true),

    /**
     * Represents a power pellet that can be eaten by pacman. When eaten, all
     * ghosts will become vulnerable to pacman.
     */
    POWER_PELLET(true),

    /**
     * Represents a solid wall that cannot be passed by any entity.
     */
    WALL(false),

    /**
     * Represents a "tunnel" that connects opposite sides of the board.
     */
    TUNNEL(true),

    /**
     * The ghost pen is a special tile that protects the ghost's spawn.
     * Pacman cannot pass through this tile, but the ghosts can.
     */
    GHOST_PEN(false);


    private final boolean isPassable;

    TileState(boolean isPassable) {
        this.isPassable = isPassable;
    }

    /**
     * Returns whether the tile is passable to all entities. Some tiles can still
     * be passed by certain entity types.
     *
     * @return true if the tile is passable to all entities, false otherwise.
     */
    public boolean isPassable() {
        return isPassable;
    }
}
