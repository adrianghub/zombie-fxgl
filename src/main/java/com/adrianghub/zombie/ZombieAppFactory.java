package com.adrianghub.zombie;

import com.adrianghub.zombie.components.SurvivorComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.dsl.components.RandomMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class ZombieAppFactory implements EntityFactory {

    @Spawns("survivor")
    public Entity newSurvivor(SpawnData data) {
        return entityBuilder(data)
                .type(SURVIVOR)
                .viewWithBBox("survivor.png")
                .with(new SurvivorComponent())
                .collidable()
                .build();
    }

    @Spawns("wanderer")
    public Entity newWanderer(SpawnData data) {

        return entityBuilder(data)
                .type(ZOMBIE)
                .viewWithBBox("zombie-wan.png")
                .collidable()
                .with(new RandomMoveComponent(
                        new Rectangle2D(0, 0,
                                getAppWidth()+100, getAppHeight()+100), 150))
                .build();
    }

    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        Point2D direction = data.get("dir");

        return entityBuilder(data)
                .type(BULLET)
                .viewWithBBox("bullet.png")
                .collidable()
                .with(new ProjectileComponent(direction, 1000))
                .with(new OffscreenCleanComponent())
                .build();
    }
}
