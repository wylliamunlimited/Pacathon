package com.buaisociety.pacman.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buaisociety.pacman.entity.behavior.Behavior;
import com.buaisociety.pacman.maze.Maze;
import com.buaisociety.pacman.maze.Tile;
import com.buaisociety.pacman.util.Disposable;
import com.buaisociety.pacman.util.NumberUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.joml.Vector2i;

/**
 * Represents an entity in the game.
 */
public abstract class Entity implements Disposable {

    public static final double BASE_SPEED = 1.26262627083;

    protected final @NotNull Maze maze;
    protected final @NotNull EntityType type;
    protected int ticksAlive;
    protected Vector2i spawnTile;
    protected Vector2dc lastPosition;
    protected Vector2dc position;
    protected Direction direction;

    protected Entity(@NotNull Maze maze, @NotNull EntityType type) {
        this.maze = maze;
        this.type = type;
        this.ticksAlive = 0;
        this.spawnTile = new Vector2i();
        this.lastPosition = new Vector2d();
        this.position = new Vector2d();
        this.direction = Direction.UP;
    }

    public @NotNull Maze getMaze() {
        return maze;
    }

    public @NotNull EntityType getType() {
        return type;
    }

    public int getTicksAlive() {
        return ticksAlive;
    }

    public @NotNull Vector2d getPosition() {
        return new Vector2d(position);
    }

    public void setPosition(@NotNull Vector2dc position) {
        ((Vector2d) this.lastPosition).set(this.position);
        ((Vector2d) this.position).set(position);
    }

    public @NotNull Vector2i getTilePosition() {
        return maze.toTileCoords(position);
    }

    public @NotNull Vector2i getSpawnTile() {
        return spawnTile;
    }

    public @NotNull Direction getDirection() {
        return direction;
    }

    public void setDirection(@NotNull Direction direction) {
        this.direction = direction;
    }

    /**
     * Called by {@link Maze#reset()} to reset the entity to its initial state
     * when Pacman dies and respawns.
     */
    public void reset() {
    }

    /**
     * Returns true if the entity should be removed from the game (permanent death).
     *
     * @return true if the entity should be removed.
     */
    public boolean isRemove() {
        return false;
    }

    /**
     * Returns the speed of the entity in pixels per frame.
     *
     * @return the speed of the entity.
     */
    public abstract double getSpeed();

    /**
     * Returns the current behavior (the behavior to handle the next movement)
     * of the entity.
     *
     * @return the behavior of the entity.
     */
    public abstract @NotNull Behavior getBehavior();

    /**
     * Returns true if, if the entity were to face the given direction, it would
     * be able to move forward in that direction. Note that Pacman can "corner,"
     * meaning that he can "move into a wall" partially, so long as it is on a
     * corner.
     *
     * @param direction the direction to check.
     * @return true if the entity can move in the given direction.
     */
    public boolean canMove(@NotNull Direction direction) {
        Tile current = maze.getTile(getTilePosition());
        Tile next = current.getNeighbor(direction);

        // When the next tile is not passable, the only case we can move forward
        // in that direction is when we still have some space to move in the
        // current tile (in pixel coordinates).
        if (!next.getState().isPassable()) {
            Vector2i currentPixel = new Vector2i((int) position.x(), (int) position.y());
            Vector2i toCenter = current.getCenterPixel().sub(currentPixel);
            int dot = toCenter.x * direction.getDx() + toCenter.y * direction.getDy();

            return Math.abs(dot) > 0;
        }

        return true;
    }

    public void move(@NotNull Direction direction, double speed, boolean fixCenter) {
        Vector2d position = getPosition();

        Tile currentTile = maze.getTile(getTilePosition());
        Tile nextTile = currentTile.getNeighbor(direction);

        // To prevent overshooting, we should move towards the center of the current tile
        if (!nextTile.getState().isPassable() && this instanceof PacmanEntity) {
            Vector2i center = currentTile.getCenterPixel();
            position.x = NumberUtil.moveTowards(position.x(), center.x(), speed);
            position.y = NumberUtil.moveTowards(position.y(), center.y(), speed);
        } else {
            position.add(speed * direction.getDx(), speed * direction.getDy());
        }

        if (fixCenter) {
            Vector2i center = currentTile.getCenterPixel();
            if (direction.isHorizontal()) {
                position.y = NumberUtil.moveTowards(position.y(), center.y(), speed);
            } else {
                position.x = NumberUtil.moveTowards(position.x(), center.x(), speed);
            }
        }

        // When going through a tunnel, we need to wrap around the maze.
        setPosition(maze.getWrappedPosition(position));
    }

    /**
     * Updates the entity's state. This method is called once per frame, before
     * rendering the entity.
     */
    public void update() {
        ticksAlive++;
    }

    /**
     * Renders the entity to the screen. This method is called once per frame,
     * after updating the entity's state.
     *
     * @param batch the sprite batch to render the entity with.
     */
    public abstract void render(@NotNull SpriteBatch batch);
}
