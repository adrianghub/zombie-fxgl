package com.adrianghub.zombie.handlers;

import com.adrianghub.zombie.ZombieApp;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.*;
import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.adrianghub.zombie.ZombieApp.EntityType.HEART;
import static com.adrianghub.zombie.ZombieApp.EntityType.SURVIVOR;
import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.geti;
import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;

public class SurvivorLifeHandler extends CollisionHandler {

    public SurvivorLifeHandler() {
        super(SURVIVOR, LIFE);
    }

    @Override
    protected void onCollisionBegin(Entity survivor, Entity life) {


        inc("lives", +1);
        spawn("textScore", new SpawnData(life.getPosition()).put("text", "+1 life"));

        animationBuilder()
                .duration(Duration.seconds(0.75))
                .onFinished(life::removeFromWorld)
                .interpolator(Interpolators.BACK.EASE_IN())
                .scale(life)
                .from(new Point2D(life.getScaleX(), life.getScaleY()))
                .to(new Point2D(0, 0))
                .buildAndPlay();
    }
}
