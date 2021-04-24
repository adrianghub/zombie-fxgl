package com.adrianghub.zombie.handlers;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.adrianghub.zombie.factories.WeaponsFactory.WeaponType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class SurvivorAmmoHandler extends CollisionHandler {

    public SurvivorAmmoHandler() {
        super(SURVIVOR, AMMO);
    }

    @Override
    protected void onCollisionBegin(Entity survivor, Entity ammo) {

        ammo.getComponent(CollidableComponent.class).setValue(false);

        set("weaponType", PISTOL);

        if (geti("score") >= 10000) {
            inc("ammo", + 50);
            set("weaponType", SHOTGUN);
        } else {
            inc("ammo", +25);
        }

        animationBuilder()
                .duration(Duration.seconds(0.75))
                .onFinished(ammo::removeFromWorld)
                .interpolator(Interpolators.BACK.EASE_IN())
                .scale(ammo)
                .from(new Point2D(ammo.getScaleX(), ammo.getScaleY()))
                .to(new Point2D(0, 0))
                .buildAndPlay();
    }
}
