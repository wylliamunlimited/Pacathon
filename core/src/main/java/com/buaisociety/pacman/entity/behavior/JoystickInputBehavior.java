package com.buaisociety.pacman.entity.behavior;

import com.buaisociety.pacman.entity.Direction;
import com.buaisociety.pacman.entity.Entity;
import com.buaisociety.pacman.util.Joystick;
import org.jetbrains.annotations.NotNull;

/**
 * This behavior is used to get the direction from a joystick/keyboard input.
 */
public class JoystickInputBehavior implements Behavior {

    private final Joystick joystick;

    public JoystickInputBehavior(Joystick joystick) {
        this.joystick = joystick;
    }

    @NotNull
    @Override
    public Direction getDirection(@NotNull Entity entity) {
        Direction direction = joystick.getDirection();

        // Only pop direction if we can move in that direction
        if (direction != null && entity.canMove(direction)) {
            joystick.popDirection();
            return direction;
        }

        return entity.getDirection();
    }
}
