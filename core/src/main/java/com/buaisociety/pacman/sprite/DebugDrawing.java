package com.buaisociety.pacman.sprite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buaisociety.pacman.entity.Direction;
import com.buaisociety.pacman.maze.Maze;
import com.buaisociety.pacman.maze.Tile;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public final class DebugDrawing {

    private static final @NotNull GrayscaleSpriteSheet TARGET_TILE;
    private static final @NotNull GrayscaleSpriteSheet DIRECTION;

    static {
        TARGET_TILE = new GrayscaleSpriteSheet(new Texture("sprites/power-pellet.png"), 8);
        DIRECTION = new GrayscaleSpriteSheet(new Texture("sprites/directions-sprite.png"), 8);
    }

    private DebugDrawing() {
    }

    public static void drawPixel(@NotNull SpriteBatch batch, int x, int y, @NotNull Color color) {
        drawRect(batch, x, y, 1, 1, color);
    }

    public static void drawRect(@NotNull SpriteBatch batch, int x, int y, int width, int height, @NotNull Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);

        Texture pixel = new Texture(pixmap);
        batch.draw(pixel, x, y);
        pixel.dispose();
        pixmap.dispose();
    }

    public static void outlineTile(@NotNull SpriteBatch batch, @NotNull Tile tile, @NotNull Color color) {
        Vector2i pixel = new Vector2i(tile.getPosition()).mul(Maze.TILE_SIZE);
        TARGET_TILE.setColors(color, Color.CLEAR);
        TARGET_TILE.render(batch, pixel.x, pixel.y);
    }

    /**
     *
     * @param batch The sprite batch to render to.
     * @param x The x-coordinate of the tile, in pixels.
     * @param y The y-coordinate of the tile, in pixels.
     * @param color The color to outline the tile with.
     */
    public static void outlineTile(@NotNull SpriteBatch batch, int x, int y, @NotNull Color color) {
        TARGET_TILE.setColors(color, Color.CLEAR);
        TARGET_TILE.render(batch, x, y);
    }

    public static void drawDirection(@NotNull SpriteBatch batch, int x, int y, @NotNull Direction direction, @NotNull Color color) {
        DIRECTION.setColors(Color.CLEAR, color);
        DIRECTION.setCurrentTile(direction.ordinal(), 0);
        DIRECTION.render(batch, x, y);
    }
}
