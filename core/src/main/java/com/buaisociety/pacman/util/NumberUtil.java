package com.buaisociety.pacman.util;

/**
 * A utility class for math operations.
 */
public final class NumberUtil {

    private NumberUtil() {
    }

    /**
     * Moves a value towards a target value by a maximum delta. The value will
     * never exceed the target value.
     *
     * @param current the current value
     * @param target the target value
     * @param maxDelta the maximum delta to move
     * @return the new value
     */
    public static double moveTowards(double current, double target, double maxDelta) {
        if (current < target) {
            return Math.min(current + maxDelta, target);
        } else {
            return Math.max(current - maxDelta, target);
        }
    }
}
