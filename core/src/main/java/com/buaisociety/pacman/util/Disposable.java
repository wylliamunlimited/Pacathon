package com.buaisociety.pacman.util;

/**
 * Represents an object that has resources to be disposed.
 */
public interface Disposable {

    /**
     * Disposes of the resources. Should be called when the object is deleted.
     */
    void dispose();
}
