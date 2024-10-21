package com.buaisociety.pacman.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Holds all types of entities that can be created in pacman.
 */
public enum EntityType {

    PACMAN(PacmanEntity.class, PacmanEntity.Config.class),
    GHOST(GhostEntity.class, GhostEntity.Config.class),
    FRUIT(FruitEntity.class, FruitEntity.Config.class);


    private final @NotNull Class<? extends Entity> entityClass;
    private final @NotNull Class<?> configClass;

    EntityType(
        @NotNull Class<? extends Entity> entityClass,
        @NotNull Class<?> configClass
    ) {
        this.entityClass = entityClass;
        this.configClass = configClass;
    }

    public @NotNull Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public @NotNull Class<?> getConfigClass() {
        return configClass;
    }
}
