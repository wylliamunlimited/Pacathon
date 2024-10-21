package com.buaisociety.pacman.sprite;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2i;

/**
 * Represents a particle that can be rendered on the screen.
 */
public class Particle {

    // Physics properties
    private Vector2d position;
    private Vector2d velocity;
    private int velocityFor = -1;  // ticks before velocity is reset
    private int liveFor = -1;  // ticks before death

    // Visual properties
    private final GrayscaleSpriteSheet spriteSheet;
    private final Vector2i spriteTile;
    private final Color[] colors;

    public Particle(@NotNull GrayscaleSpriteSheet spriteSheet, @NotNull Vector2i spriteTile, @NotNull Color[] colors) {
        this.spriteSheet = spriteSheet;
        this.spriteTile = spriteTile;
        this.colors = colors;

        this.position = new Vector2d();
        this.velocity = new Vector2d();
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2d velocity) {
        this.velocity = velocity;
    }

    public void setVelocityFor(int ticks) {
        this.velocityFor = ticks;
    }

    public boolean isAlive() {
        return liveFor > 0;
    }

    public void setLiveFor(int ticks) {
        this.liveFor = ticks;
    }

    public void render(@NotNull SpriteBatch batch) {
        if (liveFor == -1L)
            throw new IllegalStateException("Particle needs a lifespan");
        if (!isAlive())
            return;

        // Move the particle until the velocity expires
        if (velocityFor > 0) {
            velocityFor--;
            position.add(velocity);
        }

        liveFor--;
        spriteSheet.setCurrentTile(spriteTile.x, spriteTile.y);
        spriteSheet.setColors(colors);

        int spriteX = (int) position.x - spriteSheet.getTileSize().x() / 2 + 1;
        int spriteY = (int) position.y - spriteSheet.getTileSize().y() / 2 + 1;
        spriteSheet.render(batch, spriteX, spriteY);
    }
}
