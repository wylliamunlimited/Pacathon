package com.buaisociety.pacman.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.buaisociety.pacman.entity.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * A joystick that can be used to get input from the user.
 */
public class Joystick implements InputProcessor {

    private Direction direction;

    public Joystick() {
    }

    public @Nullable Direction getDirection() {
        return direction;
    }

    public @Nullable Direction popDirection() {
        Direction direction = this.direction;
        this.direction = null;
        return direction;
    }

    @Override
    public boolean keyDown(int keycode) {
        Direction direction = null;
        switch (keycode) {
            case Input.Keys.UP, Input.Keys.W, Input.Keys.T, Input.Keys.I -> direction = Direction.UP;
            case Input.Keys.DOWN, Input.Keys.S, Input.Keys.G, Input.Keys.K -> direction = Direction.DOWN;
            case Input.Keys.LEFT, Input.Keys.A, Input.Keys.F, Input.Keys.J -> direction = Direction.LEFT;
            case Input.Keys.RIGHT, Input.Keys.D, Input.Keys.H, Input.Keys.L -> direction = Direction.RIGHT;
        }

        // No keyboard input that we care about...
        if (direction == null)
            return false;

        this.direction = direction;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
