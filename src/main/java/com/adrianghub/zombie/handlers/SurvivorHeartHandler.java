package com.adrianghub.zombie.handlers;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.EntityType.HEART;
import static com.adrianghub.zombie.ZombieApp.EntityType.SURVIVOR;
import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.play;

public class SurvivorHeartHandler extends CollisionHandler {

    public SurvivorHeartHandler() {
        super(SURVIVOR, HEART);
    }

    @Override
    protected void onCollisionBegin(Entity survivor, Entity heart) {

        var hp = survivor.getComponent(HealthIntComponent.class);

        if (hp.getValue() < 3) {
            hp.setValue(hp.getValue() + 1);
            play("breath.wav");
        }

        animationBuilder()
                .duration(Duration.seconds(0.75))
                .onFinished(heart::removeFromWorld)
                .interpolator(Interpolators.BACK.EASE_IN())
                .scale(heart)
                .from(new Point2D(heart.getScaleX(), heart.getScaleY()))
                .to(new Point2D(0, 0))
                .buildAndPlay();
    }
}
