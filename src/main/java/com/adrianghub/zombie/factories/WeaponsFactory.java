package com.adrianghub.zombie.factories;

import com.adrianghub.zombie.components.BulletComponent;
import com.adrianghub.zombie.components.ShotgunComponent;
import com.adrianghub.zombie.components.TripleShotgunComponent;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.EntityType.BULLET;
import static com.adrianghub.zombie.factories.WeaponsFactory.WeaponType.SHOTGUN;
import static com.adrianghub.zombie.factories.WeaponsFactory.WeaponType.TRIPLE_SHOTGUN;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

public class WeaponsFactory implements EntityFactory {

    public enum WeaponType {
        NO_AMMO,
        PISTOL,
        SHOTGUN,
        TRIPLE_SHOTGUN
    }


    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        var expireClean = new ExpireCleanComponent(Duration.seconds(0.5)).animateOpacity();
        expireClean.pause();

        Point2D direction = data.get("dir");

        return entityBuilder(data)
                .type(BULLET)
                .viewWithBBox("bullet.png")
                .collidable()
                .with(new ProjectileComponent(direction, 500))
                .with(new BulletComponent())
                .with(expireClean)
                .build();
    }

    @Spawns("shotgun")
    public Entity newShotgun(SpawnData data) {

        return entityBuilder(data)
                .type(SHOTGUN)
                .scale(1.5, 1.5)
                .viewWithBBox("shotgun.png")
                .zIndex(100)
                .collidable()
                .with(new ShotgunComponent())
                .with(new ExpireCleanComponent(Duration.seconds(10)))
                .build();
    }

    @Spawns("triple-shotgun")
    public Entity newTShotgun(SpawnData data) {

        return entityBuilder(data)
                .type(TRIPLE_SHOTGUN)
                .scale(1.5, 1.5)
                .viewWithBBox("triple-shotgun.png")
                .zIndex(100)
                .collidable()
                .with(new TripleShotgunComponent())
                .with(new ExpireCleanComponent(Duration.seconds(10)))
                .build();
    }
}