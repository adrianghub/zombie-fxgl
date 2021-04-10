package com.adrianghub.zombie;

import com.adrianghub.zombie.components.SurvivorComponent;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.animation.Interpolator;
import javafx.beans.binding.StringBinding;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;
import java.util.Random;

import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class ZombieApp extends GameApplication {

    private Entity survivor;

    public enum EntityType {
        SURVIVOR, ZOMBIE, BULLET, WALL
    }

    private static final String[] deathMessage = {
            "Sooo close...",
            "Ah, shit...Here we go again",
            "You're a dead meat",
            "Come back later :)",
    };

    private static String getRandomDeathMessage() {
        return deathMessage[FXGLMath.random(0, 3)];
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

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> survivor.getComponent(SurvivorComponent.class).move());
        onKey(KeyCode.A, () -> survivor.getComponent(SurvivorComponent.class).turnLeft());
        onKey(KeyCode.D, () -> survivor.getComponent(SurvivorComponent.class).turnRight());
        onKeyDown(KeyCode.SPACE,"Single shot", () -> survivor.getComponent(SurvivorComponent.class).shoot());
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(ZOMBIE, BULLET, (zombie, bullet) -> {
            spawn("explosion", zombie.getPosition());

            zombie.removeFromWorld();
            bullet.removeFromWorld();
            getWorldProperties().increment("score", +100);
        });

        onCollisionBegin(SURVIVOR, ZOMBIE, (survivor, zombie) -> {
            Duration userTime = Duration.seconds(getd("time"));
            Integer userScore = geti("score");

            zombie.removeFromWorld();
            showMessage("\n" + getRandomDeathMessage() +
                    String.format("\n\nPoints: %d", userScore) +
                    String.format("\n\nTime: %.2f sec!", userTime.toSeconds()),
                    () -> getGameController().startNewGame());
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("time", 0.0);
        vars.put("score", 0);
    }

    @Override
    protected void initUI() {
        StringBinding playerScore = getWorldProperties().intProperty("score").asString();
        StringBinding playerTime = getWorldProperties().doubleProperty("time").asString("Time:  %.2f");

        Text textScore = getUIFactoryService().newText("", Color.WHITE, 32);
        textScore.setTranslateX(500);
        textScore.setTranslateY(50);
        textScore.setStroke(Color.GOLD);

        getGameScene().addUINode(textScore);

        Text timeCounter = getUIFactoryService().newText("", Color.WHITE, 32);
        timeCounter.setTranslateX(800);
        timeCounter.setTranslateY(50);
        timeCounter.setStroke(Color.GOLD);

        getGameScene().addUINode(timeCounter);

        textScore.textProperty().bind(playerScore);
        timeCounter.textProperty().bind(playerTime);
    }

    @Override
    protected void onUpdate(double tpf) {
        inc("time", tpf);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
