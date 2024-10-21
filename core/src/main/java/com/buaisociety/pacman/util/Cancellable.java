package com.buaisociety.pacman.util;

/**
 * Represents an event that can be cancelled.
 */
public interface Cancellable {

    /**
     * Cancels the event.
     */
    default void cancel() {
        setCancelled(true);
    }

    /**
     * Sets whether the event is cancelled.
     *
     * @param cancelled {@code true} if the event is cancelled, {@code false} otherwise.
     */
    void setCancelled(boolean cancelled);


    /**
     * Returns whether the event is cancelled.
     *
     * @return {@code true} if the event is cancelled, {@code false} otherwise.
     */
    boolean isCancelled();
}
