package com.adrianghub.zombie.handlers;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.EntityType.SURVIVOR;
import static com.adrianghub.zombie.factories.WeaponsFactory.WeaponType.SHOTGUN;
import static com.almasb.fxgl.dsl.FXGL.*;

public class SurvivorShotgunHandler extends CollisionHandler {

    public SurvivorShotgunHandler() {
        super(SURVIVOR, SHOTGUN);
    }

    @Override
    protected void onCollisionBegin(Entity survivor, Entity shotgun) {

        shotgun.getComponent(CollidableComponent.class).setValue(false);

        set("weaponType", SHOTGUN);
        inc("ammo", +10);

        animationBuilder()
                .duration(Duration.seconds(0.75))
                .onFinished(shotgun::removeFromWorld)
                .interpolator(Interpolators.BACK.EASE_IN())
                .scale(shotgun)
                .from(new Point2D(shotgun.getScaleX(), shotgun.getScaleY()))
                .to(new Point2D(0, 0))
                .buildAndPlay();
    }
}
