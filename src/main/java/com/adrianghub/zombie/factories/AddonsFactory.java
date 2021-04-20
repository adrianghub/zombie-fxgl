package com.adrianghub.zombie.factories;

import com.adrianghub.zombie.components.AmmoComponent;
import com.adrianghub.zombie.components.HeartComponent;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.EntityType.AMMO;
import static com.adrianghub.zombie.ZombieApp.EntityType.HEART;
import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class AddonsFactory implements EntityFactory {

    @Spawns("ammo")
    public Entity newAmmo(SpawnData data) {

        return entityBuilder(data)
                .type(AMMO)
                .scale(1.25, 1.25)
                .viewWithBBox("bullets.png")
                .zIndex(100)
                .collidable()
                .with(new AmmoComponent())
                .with(new ExpireCleanComponent(Duration.seconds(15)))
                .build();
    }

    @Spawns("heart")
    public Entity newHeart(SpawnData data) {

        var e = entityBuilder(data)
                .type(HEART)
                .scale(1.25, 1.25)
                .viewWithBBox("heart.png")
                .zIndex(100)
                .collidable()
                .with(new HeartComponent())
                .with(new ExpireCleanComponent(Duration.seconds(15)))
                .build();

        animationBuilder()
                .repeatInfinitely()
                .autoReverse(true)
                .duration(Duration.seconds(1))
                .scale(e)
                .from(new Point2D(1, 1))
                .to(new Point2D(1.3, 1.3))
                .buildAndPlay();

        return e;
    }
}
