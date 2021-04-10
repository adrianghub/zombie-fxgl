package com.adrianghub.zombie;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.animation.Interpolator;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ZombieApp extends GameApplication {

    private Entity survivor;

    public enum EntityType {
        SURVIVOR, ZOMBIE, BULLET, WALL
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Zombie Invasion");
        settings.setFontUI("zombie.ttf");
        settings.setWidth(1024);
        settings.setHeight(720);
    }

    @Override
    protected void initGame() {

        getGameWorld().addEntityFactory(new ZombieAppFactory());

        getGameScene().setBackgroundColor(Color.color(0, 0, 0.05, 0.5));

        this.survivor = spawn("survivor", getAppWidth() / 2 - 15, getAppHeight() / 2 - 15);

        Random random = new Random();

        run(() -> {

            Entity e = getGameWorld().create("wanderer", new SpawnData(random.nextInt(getAppWidth()), random.nextInt(getAppHeight())));

            spawnWithScale(e, Duration.seconds(0.3), Interpolator.EASE_OUT);
        }, Duration.seconds(2));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
