package com.buaisociety.pacman.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buaisociety.pacman.entity.behavior.Behavior;
import com.buaisociety.pacman.maze.Maze;
import com.buaisociety.pacman.sprite.CutoutSpriteSheet;
import com.buaisociety.pacman.sprite.GrayscaleSpriteSheet;
import com.buaisociety.pacman.sprite.Particle;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;
import org.joml.Vector2i;

import java.util.concurrent.ThreadLocalRandom;

public class FruitEntity extends Entity {

    public static @NotNull CutoutSpriteSheet fruitSpriteShare = new CutoutSpriteSheet(new Texture("sprites/fruit-sprite.png"), 16);
    public static @NotNull GrayscaleSpriteSheet bonusSpriteShare = new GrayscaleSpriteSheet(new Texture("sprites/bonus-points-sprite.png"), new Vector2i(8 * 4, 8 * 2));

    private final @NotNull CutoutSpriteSheet fruitSprite;
    private final @NotNull GrayscaleSpriteSheet bonusSprite;
    private final int fruitTileX;
    private int ticksLeft;
    private boolean isRemove;

    public FruitEntity(@NotNull Maze maze, @NotNull Config config) {
        super(maze, EntityType.FRUIT);
        setPosition(config.spawnPixel);

        fruitTileX = switch (maze.getLevelManager().getLevel()) {
            case 1 -> 0;
            case 2 -> 1;
            case 3, 4 -> 2;
            case 5, 6 -> 3;
            case 7, 8 -> 4;
            case 9, 10 -> 5;
            case 11, 12 -> 6;
            default -> 7;
        };

        this.fruitSprite = config.fruitSprite;
        this.bonusSprite = config.bonusSprite;
        this.ticksLeft = config.ticksLeft;
    }

    /**
     * Returns true if the entity should be removed from the game (permanent death).
     *
     * @return true if the entity should be removed.
     */
    @Override
    public boolean isRemove() {
        return isRemove;
    }

    /**
     * Returns the speed of the entity in pixels per frame.
     *
     * @return the speed of the entity.
     */
    @Override
    public double getSpeed() {
        return 0;
    }

    /**
     * Returns the current behavior (the behavior to handle the next movement) of the entity.
     *
     * @return the behavior of the entity.
     */
    @Override
    public @NotNull Behavior getBehavior() {
        throw new UnsupportedOperationException("Fruit entity does not have a behavior");
    }

    /**
     * Updates the entity's state. This method is called once per frame, before rendering the entity.
     */
    @Override
    public void update() {
        if (isRemove)
            return;

        super.update();

        ticksLeft--;
        if (ticksLeft <= 0) {
            isRemove = true;
        }

        Vector2i pacmanTile = maze.getPacman().getTilePosition();
        if (pacmanTile.equals(getTilePosition())) {
            isRemove = true;

            int score = switch (maze.getLevelManager().getLevel()) {
                case 1 -> 100;
                case 2 -> 300;
                case 3, 4 -> 500;
                case 5, 6 -> 700;
                case 7, 8 -> 1000;
                case 9, 10 -> 2000;
                case 11, 12 -> 3000;
                default -> 5000;
            };

            int spriteX = switch (score) {
                case 100 -> 0;
                case 300 -> 2;
                case 500 -> 4;
                case 700 -> 5;
                case 1000 -> 7;
                case 2000 -> 9;
                case 3000 -> 10;
                default -> 11;
            };

            maze.getLevelManager().incrementScore(score);
            Particle particle = new Particle(
                bonusSprite,
                new Vector2i(spriteX, 0),
                new Color[]{ Color.CLEAR, new Color(0xffb7ffff) }
            );
            particle.setPosition(getPosition());
            particle.setLiveFor(180);
            maze.addParticle(particle);
        }
    }

    /**
     * Renders the entity to the screen. This method is called once per frame, after updating the entity's state.
     *
     * @param batch the sprite batch to render the entity with.
     */
    @Override
    public void render(@NotNull SpriteBatch batch) {
        int pixelX = (int) position.x() - fruitSprite.getTileSize().x() / 2 + 1;
        int pixelY = (int) position.y() - fruitSprite.getTileSize().y() / 2 + 1;
        fruitSprite.setCurrentTile(fruitTileX, 0);
        fruitSprite.render(batch, pixelX, pixelY);
    }

    /**
     * Disposes of the resources. Should be called when the object is deleted.
     */
    @Override
    public void dispose() {
        if (fruitSprite != fruitSpriteShare)
            fruitSprite.dispose();
        if (bonusSprite != bonusSpriteShare)
            bonusSprite.dispose();
    }


    public static class Config {
        public @NotNull Vector2d spawnPixel = new Vector2d();
        public @NotNull CutoutSpriteSheet fruitSprite = fruitSpriteShare;
        public @NotNull GrayscaleSpriteSheet bonusSprite = bonusSpriteShare;
        public int ticksLeft = ThreadLocalRandom.current().nextInt(9 * 60, 10 * 60);
    }
}
