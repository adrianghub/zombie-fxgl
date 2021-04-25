package com.adrianghub.zombie.handlers;

import com.adrianghub.zombie.ZombieApp;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.EntityType.COIN;
import static com.adrianghub.zombie.ZombieApp.EntityType.SURVIVOR;
import static com.almasb.fxgl.dsl.FXGL.*;

public class SurvivorCoinHandler extends CollisionHandler {

    public SurvivorCoinHandler() {
        super(SURVIVOR, COIN);
    }

    @Override
    protected void onCollisionBegin(Entity survivor, Entity coin) {

        coin.getComponent(CollidableComponent.class).setValue(false);

        inc("score", +100);

        animationBuilder()
                .duration(Duration.seconds(0.75))
                .onFinished(coin::removeFromWorld)
                .interpolator(Interpolators.BACK.EASE_IN())
                .scale(coin)
                .from(new Point2D(coin.getScaleX(), coin.getScaleY()))
                .to(new Point2D(0, 0))
                .buildAndPlay();
    }
}
