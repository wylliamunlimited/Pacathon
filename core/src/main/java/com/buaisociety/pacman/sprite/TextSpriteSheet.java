package com.buaisociety.pacman.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buaisociety.pacman.util.Disposable;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

/**
 * Wraps a sprite sheet that contains text characters. This is good for rendering
 * strings of text.
 */
public class TextSpriteSheet implements Disposable {

    public static final @NotNull String SPECIAL = "1234567890 !?.,";

    private final @NotNull GrayscaleSpriteSheet spriteSheet;

    public TextSpriteSheet(@NotNull GrayscaleSpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public @NotNull GrayscaleSpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public Vector2i getTileFor(char c) {
        if (c >= 'A' && c <= 'Z') {
            return new Vector2i(c - 'A', 0);
        } else if (c >= 'a' && c <= 'z') {
            return new Vector2i(c - 'a', 1);
        }

        int index = SPECIAL.indexOf(c);
        if (index != -1) {
            return new Vector2i(index, 2);
        }

        throw new IllegalArgumentException("Invalid character: " + c);
    }

    public void render(@NotNull SpriteBatch batch, int x, int y, @NotNull String text) {
        int tileWidth = spriteSheet.getTileSize().x();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Vector2i tile = getTileFor(c);
            spriteSheet.setCurrentTile(tile.x, tile.y);
            spriteSheet.render(batch, x + i * tileWidth, y);
        }
    }

    public void renderRightAligned(@NotNull SpriteBatch batch, int x, int y, @NotNull String text) {
        int tileWidth = spriteSheet.getTileSize().x();
        int width = text.length() * tileWidth;

        render(batch, x - width, y, text);
    }

    @Override
    public void dispose() {
        spriteSheet.dispose();
    }
}
