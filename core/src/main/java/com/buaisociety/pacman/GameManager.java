package com.buaisociety.pacman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buaisociety.pacman.entity.Direction;
import com.buaisociety.pacman.entity.EntityType;
import com.buaisociety.pacman.entity.GhostEntity;
import com.buaisociety.pacman.entity.PacmanEntity;
import com.buaisociety.pacman.entity.behavior.Behavior;
import com.buaisociety.pacman.entity.behavior.JoystickInputBehavior;
import com.buaisociety.pacman.event.CreateMazeEvent;
import com.buaisociety.pacman.event.EntityPreSpawnEvent;
import com.buaisociety.pacman.event.EntitySpawnEvent;
import com.buaisociety.pacman.event.GameEndEvent;
import com.buaisociety.pacman.event.LoseLifeEvent;
import com.buaisociety.pacman.event.NextLevelEvent;
import com.buaisociety.pacman.maze.Maze;
import com.buaisociety.pacman.maze.TerminalReason;
import com.buaisociety.pacman.maze.TileState;
import com.buaisociety.pacman.sprite.GrayscaleSpriteSheet;
import com.buaisociety.pacman.sprite.TextSpriteSheet;
import com.buaisociety.pacman.util.Disposable;
import com.buaisociety.pacman.util.EventSystem;
import com.buaisociety.pacman.util.Joystick;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;

/**
 * Handles moving between levels, and setting up the maze for each level.
 */
public class GameManager implements Disposable {

    private final @NotNull EventSystem events;
    private final @NotNull Config config;

    private int level;
    private int extraLives;
    private int score;
    private @Nullable Maze currentMaze;
    private @NotNull TextSpriteSheet textSprite;
    private @NotNull GrayscaleSpriteSheet pacmanSprite;

    public GameManager(@NotNull EventSystem events, @NotNull Config config) {
        this.events = events;
        this.config = config;

        this.level = config.startLevel;
        this.extraLives = config.startLives;
        this.score = config.startScore;
        this.currentMaze = null;

        this.textSprite = config.textSprite;
        textSprite.getSpriteSheet().setColors(Color.CLEAR, Color.WHITE);
        this.pacmanSprite = config.pacmanSprite;
        pacmanSprite.setColors(Color.CLEAR, Color.YELLOW);
    }

    /**
     * Returns the event system, in charge of firing and listening to events.
     *
     * @return the event system
     */
    public @NotNull EventSystem getEvents() {
        return events;
    }

    /**
     * Returns the configuration for the game manager. Each game has its own
     * configuration.
     *
     * @return the configuration for the game manager
     */
    public @NotNull Config getConfig() {
        return config;
    }

    /**
     * Returns the current level. 1 is the first level. 0 implies that no game
     * has been started yet.
     *
     * @return the current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the current score.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    public @NotNull TextSpriteSheet getTextSprite() {
        return textSprite;
    }

    /**
     * Increments the score by the given amount.
     *
     * @param score the amount to increment the score by
     */
    public void incrementScore(int score) {
        int oldScore = this.score;
        this.score += score;

        // Every time we pass a multiple of 10,000 points, add a new life
        if (oldScore / 10000 != this.score / 10000) {
            extraLives++;
        }
    }

    /**
     * Returns the number of extra lives remaining.
     *
     * <p>This method will return 0 if there are no extra lives remaining, e.g.
     * the game will end when pacman dies next.
     *
     * @return the number of extra lives remaining
     */
    public int getExtraLives() {
        return extraLives;
    }

    /**
     * Sets the number of extra lives remaining.
     *
     * @param extraLives the number of extra lives remaining
     */
    public void setExtraLives(int extraLives) {
        this.extraLives = extraLives;
    }

    /**
     * Returns the current maze, or <code>null</code> if no game has been started yet.
     *
     * @return the current maze
     */
    public @Nullable Maze getCurrentMaze() {
        return currentMaze;
    }

    /**
     * Updates the current maze, and checks if the game has been won or lost.
     */
    public void update() {
        if (currentMaze == null)
            return;
        if (extraLives < 0) {
            // Let people press any button to restart the game
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                level = 0;
                extraLives = config.startLives;
                score = config.startScore;
                nextLevel();
            }

            return;
        }

        currentMaze.update();
    }

    public void postUpdate() {
        TerminalReason complete = currentMaze.getTerminalReason();

        if (complete == TerminalReason.WIN) {
            nextLevel();
        } else if (complete == TerminalReason.LOSE) {
            currentMaze.reset();

            // Fire an event to allow modification of the number of lives remaining
            LoseLifeEvent event = new LoseLifeEvent(currentMaze.getPacman(), extraLives - 1);
            events.fireEvent(event);
            if (event.isCancelled())
                return;

            extraLives = event.getNumLives();
        }

        if (extraLives < 0) {
            GameEndEvent event = new GameEndEvent(this);
            events.fireEvent(event);
        }
    }

    /**
     * Renders the current maze, and the high score.
     *
     * @param batch the sprite batch to render to
     */
    public void render(@NotNull SpriteBatch batch) {
        if (currentMaze == null)
            return;

        currentMaze.render(batch);
        textSprite.render(batch, Maze.TILE_SIZE * 9, Maze.TILE_SIZE * 35, "HIGH SCORE");

        // Arcade Pacman only shows scores once Pacman has collected a dot
        if (score > 0) {
            textSprite.renderRightAligned(batch, Maze.TILE_SIZE * 17, Maze.TILE_SIZE * 34, String.valueOf(score));
        }

        // Show the number of lives remaining
        Vector2i pacmanSpriteTile = new Vector2i(0, 2);
        for (int i = 0; i < extraLives; i++) {
            pacmanSprite.setCurrentTile(pacmanSpriteTile.x, pacmanSpriteTile.y);
            pacmanSprite.render(batch, Maze.TILE_SIZE * 2 + i * Maze.TILE_SIZE * 2, -1);
        }

        if (extraLives < 0) {
            textSprite.getSpriteSheet().setColors(Color.CLEAR, Color.RED);
            textSprite.render(batch, 76, 120, "GAME OVER");
            textSprite.getSpriteSheet().setColors(Color.CLEAR, Color.WHITE);
        }
    }

    /**
     * Moves to the next level, and sets up the maze for that level.
     */
    public void nextLevel() {
        level++;

        // Parse levels.json, and determine the name of the next level
        ObjectMapper mapper = new ObjectMapper();
        JsonNode levelsJson;
        try {
            levelsJson = mapper.readTree(Gdx.files.internal(config.levelsPreset).readString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + config.levelsPreset, e);
        }

        ArrayNode allLevels = (ArrayNode) levelsJson.get("levels");
        String nextLevelName = allLevels.get(Math.min(level, allLevels.size()) - 1).asText();

        // Fire an event to allow modification of the next level name
        NextLevelEvent event = new NextLevelEvent(nextLevelName);
        events.fireEvent(event);
        nextLevelName = event.getNextLevel();

        // Now that we have the next level name, we should get the maze for it
        JsonNode mazeConfigJson;
        try {
            mazeConfigJson = mapper.readTree(Gdx.files.internal("mazes/" + nextLevelName + "/config.json").readString());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load maze config for " + nextLevelName, e);
        }

        Pixmap pixmap = new Pixmap(Gdx.files.internal("mazes/" + nextLevelName + "/maze.png"));
        TileState[][] tiles = new TileState[pixmap.getHeight()][pixmap.getWidth()];
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pixel = pixmap.getPixel(x, y);
                Color color = new Color(pixel);
                int red = (int) (color.r * 255);  // grayscale, so r=g=b

                // This is a 4-bit grayscale image, so we can only have 16 colors
                int normalized = red / (256 / (TileState.values().length - 1));
                tiles[pixmap.getHeight() - 1 - y][x] = switch (normalized) {
                    case 0 -> TileState.SPACE;
                    case 1 -> TileState.TUNNEL;
                    case 2 -> TileState.PELLET;
                    case 3 -> TileState.POWER_PELLET;
                    case 4 -> TileState.GHOST_PEN;
                    case 5 -> TileState.WALL;
                    default -> throw new IllegalStateException("Unexpected value: " + normalized);
                };
            }
        }

        Sprite levelSprite = new Sprite(new Texture("mazes/" + nextLevelName + "/render.png"));

        Vector2i fruitSpawnPixel = parseVector(mazeConfigJson.get("fruit_spawn_position"));
        Maze maze = new Maze(this, levelSprite, tiles, fruitSpawnPixel);
        Vector2i pacmanSpawnPixel = parseVector(mazeConfigJson.get("pacman_spawn_position"));
        Vector2i ghostRevivePixel = parseVector(mazeConfigJson.get("ghost_revive_position"));

        for (JsonNode ghostNode : mazeConfigJson.get("ghosts")) {
            String ghostName = ghostNode.get("name").asText();  // expect a value like 'ghosts/inky.json'
            Vector2i ghostSpawnPixel = parseVector(ghostNode.get("spawn_position"));
            Direction ghostSpawnDirection = Direction.valueOf(ghostNode.get("spawn_direction").asText().toUpperCase(Locale.ROOT));
            boolean isReleased = ghostNode.get("is_released").asBoolean();
            Vector2i ghostScatterTile = parseVector(ghostNode.get("scatter_tile"));

            JsonNode ghostJson;
            try {
                ghostJson = mapper.readTree(Gdx.files.internal(ghostName).readString());
            } catch (Exception e) {
                throw new RuntimeException("Failed to load ghost config for " + ghostName, e);
            }

            String behaviorClassPath = ghostJson.get("chase_behavior").asText();
            boolean isElroy = ghostJson.get("is_elroy").asBoolean();
            Behavior behavior;
            try {
                behavior = (Behavior) Class.forName(behaviorClassPath).getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load behavior for " + ghostName, e);
            }

            JsonNode colorJson = ghostJson.get("colors");
            List<Color> aliveColors = StreamSupport.stream(colorJson.get("alive").spliterator(), false)
                .map(GameManager::parseColor)
                .toList();
            List<Color> frightenedColors = StreamSupport.stream(colorJson.get("frightened").spliterator(), false)
                .map(GameManager::parseColor)
                .toList();
            List<Color> flashColors = StreamSupport.stream(colorJson.get("flash").spliterator(), false)
                .map(GameManager::parseColor)
                .toList();
            List<Color> eatenColors = StreamSupport.stream(colorJson.get("eaten").spliterator(), false)
                .map(GameManager::parseColor)
                .toList();

            GhostEntity.Config ghostConfig = new GhostEntity.Config();
            ghostConfig.isElroy = isElroy;
            ghostConfig.spriteSheet = new GrayscaleSpriteSheet(new Texture("sprites/ghost-sprite.png"), 20);
            ghostConfig.chase = behavior;
            ghostConfig.scatterTile = ghostScatterTile;
            ghostConfig.spawnPixel = ghostSpawnPixel;
            ghostConfig.spawnDirection = ghostSpawnDirection;
            ghostConfig.spawnReleased = isReleased;
            ghostConfig.reviveTile = ghostRevivePixel;
            ghostConfig.colorsAlive = aliveColors.toArray(new Color[0]);
            ghostConfig.colorsFrightened = frightenedColors.toArray(new Color[0]);
            ghostConfig.colorsFlash = flashColors.toArray(new Color[0]);
            ghostConfig.colorsEaten = eatenColors.toArray(new Color[0]);

            // Fire an event to allow modification of the ghost config
            EntityPreSpawnEvent preSpawnEvent = new EntityPreSpawnEvent(maze, EntityType.GHOST, ghostConfig);
            events.fireEvent(preSpawnEvent);
            ghostConfig = (GhostEntity.Config) preSpawnEvent.getConfig();
            if (preSpawnEvent.isCancelled())
                continue;

            GhostEntity ghost = new GhostEntity(maze, ghostConfig);

            // Fire an event to alert that the ghost has been spawned
            EntitySpawnEvent spawnEvent = new EntitySpawnEvent(ghost);
            events.fireEvent(spawnEvent);
            if (spawnEvent.isCancelled())
                continue;

            maze.getEntities().add(spawnEvent.getEntity());
        }

        Joystick joystick = new Joystick();
        Gdx.input.setInputProcessor(joystick);
        PacmanEntity.Config pacmanConfig = new PacmanEntity.Config();
        pacmanConfig.behavior = new JoystickInputBehavior(joystick);
        pacmanConfig.spawnPixel = pacmanSpawnPixel;
        pacmanConfig.spriteSheet = new GrayscaleSpriteSheet(new Texture("sprites/pacman-sprite.png"), 20);

        // Fire an event to allow modification of the pacman config
        EntityPreSpawnEvent preSpawnEvent = new EntityPreSpawnEvent(maze, EntityType.PACMAN, pacmanConfig);
        events.fireEvent(preSpawnEvent);
        pacmanConfig = (PacmanEntity.Config) preSpawnEvent.getConfig();
        if (preSpawnEvent.isCancelled())
            return;

        PacmanEntity pacman = new PacmanEntity(maze, pacmanConfig);

        // Fire an event to alert that the pacman has been spawned
        EntitySpawnEvent spawnEvent = new EntitySpawnEvent(pacman);
        events.fireEvent(spawnEvent);
        if (spawnEvent.isCancelled())
            return;

        maze.getEntities().add(spawnEvent.getEntity());

        CreateMazeEvent createMazeEvent = new CreateMazeEvent(maze);
        events.fireEvent(createMazeEvent);
        this.currentMaze = maze;
    }

    /**
     * Disposes of the resources. Should be called when the object is deleted.
     */
    @Override
    public void dispose() {
        if (currentMaze != null)
            currentMaze.dispose();
        textSprite.dispose();
        pacmanSprite.dispose();
    }

    private static @NotNull Color parseColor(@NotNull JsonNode node) {
        return new Color(
            (float) node.get("r").asDouble(),
            (float) node.get("g").asDouble(),
            (float) node.get("b").asDouble(),
            (float) node.get("a").asDouble()
        );
    }

    private static @NotNull Vector2i parseVector(@NotNull JsonNode node) {
        return new Vector2i(node.get("x").asInt(), node.get("y").asInt());
    }


    public static class Config {
        public int id = 0;
        public @NotNull TextSpriteSheet textSprite = new TextSpriteSheet(new GrayscaleSpriteSheet(new Texture("sprites/text-sprite.png"), 8));
        public @NotNull GrayscaleSpriteSheet pacmanSprite = new GrayscaleSpriteSheet(new Texture("sprites/pacman-sprite.png"), 20);
        public String levelsPreset = "levels.json";
        public int startLevel = 0;  // when nextLevel() is called for the first time, this gets incremented to 1
        public int startLives = 2;
        public int startScore = 0;
        public int handicap = 0;  // Delays the level speed changes by this many levels
    }
}
