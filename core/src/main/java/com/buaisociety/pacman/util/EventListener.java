package com.buaisociety.pacman.util;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an event listener that listens to events of type {@link T}.
 *
 * <p>Note that an event listener has no control over the order in which it is
 * called.
 *
 * @param <T> the type of event this listener listens to.
 */
@FunctionalInterface
public interface EventListener<T extends Event> {

    /**
     * Called when the event is fired.
     *
     * @param event the event that was fired.
     */
    void onEvent(@NotNull T event);
}
