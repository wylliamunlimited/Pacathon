package com.buaisociety.pacman.entity.behavior;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buaisociety.pacman.maze.Maze;
import com.buaisociety.pacman.sprite.DebugDrawing;
import com.cjcrafter.neat.Client;
import com.buaisociety.pacman.entity.Direction;
import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.entity.PacmanEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;

import com.buaisociety.pacman.maze.Tile;

public class NeatPacmanBehavior implements Behavior {

    private final @NotNull Client client;
    private @Nullable PacmanEntity pacman;

    // Score modifiers help us maintain "multiple pools" of points.
    // This is great for training, because we can take away points from
    // specific pools of points instead of subtracting from all.
    private int scoreModifier = 0;
    private int numberUpdatesSinceLastScore = 0;
    private int lastScore = 0;
    private Queue<Vector2d> lastPositions = new LinkedList<>();
    private int countOverlapPosition = 0;
    private int pelletCount = 0;
    private int samePelletCountTimes = 0;
    private double lastPelletDistance = 0.00;

    public NeatPacmanBehavior(@NotNull Client client) {
        this.client = client;
    }

    /**
     * Returns the desired direction that the entity should move towards.
     *
     * @param entity the entity to get the direction for
     * @return the desired direction for the entity
     */
    @NotNull
    @Override
    public Direction getDirection(@NotNull Entity entity) {
        if (pacman == null) {
            pacman = (PacmanEntity) entity;
        }

        int totalScore = pacman.getMaze().getLevelManager().getScore();

        // SPECIAL TRAINING CONDITIONS
        // TODO: Make changes here to help with your training...

        // initial penalities setup
        // Vector2d pacmanposition = pacman.getPosition();
        // if (pacmanposition == null) {
        //     System.out.println("Error: Pacman's position is null.");
        //     pacman.kill();
        //     return Direction.UP;
        // }
        // Maze maze = pacman.getMaze();
        // if (maze == null) {
        //     System.out.println("Error: maze is null.");
        //     pacman.kill();
        //     return Direction.UP;
        // }
        // Tile[][] tiles = maze.getTiles();
        // if (tiles == null) {
        //     System.out.println("Error: tile is null.");
        //     pacman.kill();
        //     return Direction.UP;
        // }
        // double closestPelletDistance = Double.MAX_VALUE;
        // // Get closest pellet distance
        // for (Tile[] row : tiles) {
        //     if (row == null) {
        //         System.out.println("Error: row is null.");
        //         pacman.kill();
        //         return Direction.UP;
        //     }
        //     for (Tile tile : row) {
        //         if (tile == null) {
        //             System.out.println("Error: tile is null.");
        //             pacman.kill();
        //             return Direction.UP;
        //         }
        //         double dx = tile.getPosition().x() - pacmanposition.x;
        //         double dy = tile.getPosition().y() - pacmanposition.y;
        //         if (closestPelletDistance > Math.sqrt(dx * dx + dy * dy)) {
        //             closestPelletDistance = Math.sqrt(dx * dx + dy * dy);
        //         }
        //     }
        // }
        // scoreModifier *= (1 - closestPelletDistance * 0.001);
        int newScore = pacman.getMaze().getLevelManager().getScore();
        if (newScore != lastScore) {
            lastScore = newScore;
            numberUpdatesSinceLastScore = 0;
        } 
        if (numberUpdatesSinceLastScore++ > 60 * 50) {
            pacman.kill();
            return Direction.UP;
        } else {
            totalScore *= (1 - numberUpdatesSinceLastScore * 0.01);
        }
        //

        // secondary penalities setup
        Vector2d pacmanposition = pacman.getPosition();
        if (lastPositions.contains(pacmanposition)) {
            countOverlapPosition++;
            totalScore *= 0.001;
        } else {
            countOverlapPosition = 0;
            totalScore *= 1.5;
        }
        if (lastPositions.size() > 250) {
            lastPositions.poll();
        }
        lastPositions.add(pacmanposition);
        if (countOverlapPosition > 10) {
            pacman.kill();
            return Direction.UP;
        }

        // 3rd penalties setup
        Maze maze = pacman.getMaze();
        if (pelletCount != maze.getPelletsRemaining()) {
            pelletCount = maze.getPelletsRemaining();
            samePelletCountTimes = 0;
        }

        totalScore *= Math.pow(0.75, samePelletCountTimes);
        // Vector2d pacmanposition = pacman.getPosition();
        if (pacmanposition == null) {
            System.out.println("Error: Pacman's position is null.");
            pacman.kill();
            return Direction.UP;
        }
        Tile[][] tiles = maze.getTiles();
        double closestPelletDistance = Double.MAX_VALUE;
        // Get closest pellet distance
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                double dx = tile.getPosition().x() - pacmanposition.x;
                double dy = tile.getPosition().y() - pacmanposition.y;
                if (closestPelletDistance > Math.sqrt(dx * dx + dy * dy)) {
                    closestPelletDistance = Math.sqrt(dx * dx + dy * dy);
                }
            }
        }
        // if (lastPelletDistance >= closestPelletDistance) {
        //     pacman.kill();
        //     return Direction.UP;
        // } else {
        //     lastPelletDistance = closestPelletDistance;
        //     scoreModifier *= Math.pow(0.75, closestPelletDistance);
        // }


        // // END OF SPECIAL TRAINING CONDITIONS

        // We are going to use these directions a lot for different inputs. Get them all once for clarity and brevity
        Direction forward = pacman.getDirection();
        Direction left = pacman.getDirection().left();
        Direction right = pacman.getDirection().right();
        Direction behind = pacman.getDirection().behind();

        // Input nodes 1, 2, 3, and 4 show if the pacman can move in the forward, left, right, and behind directions
        boolean canMoveForward = pacman.canMove(forward);
        boolean canMoveLeft = pacman.canMove(left);
        boolean canMoveRight = pacman.canMove(right);
        boolean canMoveBehind = pacman.canMove(behind);

        float[] outputs = client.getCalculator().calculate(new float[]{
            canMoveForward ? 1f : 0f,
            canMoveLeft ? 1f : 0f,
            canMoveRight ? 1f : 0f,
            canMoveBehind ? 1f : 0f,
            (float) closestPelletDistance
        }).join();

        int index = 0;
        float max = outputs[0];
        for (int i = 1; i < outputs.length; i++) {
            if (outputs[i] > max) {
                max = outputs[i];
                index = i;
            }
        }

        Direction newDirection = switch (index) {
            case 0 -> pacman.getDirection();
            case 1 -> pacman.getDirection().left();
            case 2 -> pacman.getDirection().right();
            case 3 -> pacman.getDirection().behind();
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };

        // client.setScore(pacman.getMaze().getLevelManager().getScore() + scoreModifier);
        client.setScore(totalScore);
        return newDirection;
    }

    @Override
    public void render(@NotNull SpriteBatch batch) {
        // TODO: You can render debug information here
        /*
        if (pacman != null) {
            DebugDrawing.outlineTile(batch, pacman.getMaze().getTile(pacman.getTilePosition()), Color.RED);
            DebugDrawing.drawDirection(batch, pacman.getTilePosition().x() * Maze.TILE_SIZE, pacman.getTilePosition().y() * Maze.TILE_SIZE, pacman.getDirection(), Color.RED);
        }
         */
    }
}
