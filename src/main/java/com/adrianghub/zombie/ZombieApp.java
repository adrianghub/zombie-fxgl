package com.adrianghub.zombie;

import com.adrianghub.zombie.components.SurvivorComponent;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
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
        SURVIVOR, ZOMBIE, BULLET, LAVA
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

        spawn("verticalLava", 0, 0);
        spawn("verticalLava", getAppWidth() - 10, 0);

        spawn("horizontalLava", 0, 0);
        spawn("horizontalLava", 0, getAppHeight() - 10);

        this.survivor = spawn("survivor", getAppWidth() / 2.0 - 15, getAppHeight() / 2.0 - 15);

        Random random = new Random();

        run(() -> {

            Entity e = getGameWorld().create("wanderer", new SpawnData(random.nextInt(getAppWidth()), random.nextInt(getAppHeight())));

            spawnWithScale(e, Duration.seconds(0.3), Interpolator.EASE_OUT);
        }, Duration.seconds(2));
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.W, () -> survivor.getComponent(SurvivorComponent.class).moveForward());
        onKey(KeyCode.S, () -> survivor.getComponent(SurvivorComponent.class).moveBackward());
        onKey(KeyCode.A, () -> survivor.getComponent(SurvivorComponent.class).turnLeft());
        onKey(KeyCode.D, () -> survivor.getComponent(SurvivorComponent.class).turnRight());

        onKey(KeyCode.UP, () -> survivor.getComponent(SurvivorComponent.class).moveForward());
        onKey(KeyCode.DOWN, () -> survivor.getComponent(SurvivorComponent.class).moveBackward());
        onKey(KeyCode.LEFT, () -> survivor.getComponent(SurvivorComponent.class).turnLeft());
        onKey(KeyCode.RIGHT, () -> survivor.getComponent(SurvivorComponent.class).turnRight());
        onKeyDown(KeyCode.SPACE,"Single shot", () -> survivor.getComponent(SurvivorComponent.class).shoot());
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(ZOMBIE, BULLET, (zombie, bullet) -> {

            spawn("textScore", new SpawnData(zombie.getPosition()).put("text", "+1 kill"));

            killZombie(zombie);
            bullet.removeFromWorld();

            Entity e = getGameWorld().create("bloodTrace", new SpawnData(zombie.getPosition()));

            spawnWithScale(e, Duration.seconds(0.3), Interpolators.CUBIC.EASE_IN());

            inc("score", +100);
        });

        onCollisionBegin(SURVIVOR, ZOMBIE, (survivor, zombie) -> {

            killZombie(zombie);

            survivor.setPosition(getAppWidth() / 2.0 - 15, getAppHeight() / 2.0 - 15);

            inc("lives", -1);
        });
    }

    private void killZombie(Entity zombie) {
        Point2D explosionSpawnPoint = zombie.getCenter().subtract(64, 64);

        spawn("explosion", explosionSpawnPoint);

        zombie.removeFromWorld();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("time", 0.0);
        vars.put("score", 0);
        vars.put("lives", 5);
        vars.put("ammo", 500);
    }

    @Override
    protected void initUI() {
        var playerScore = getip("score").asString();
        var playerLife = getip("lives").asString("Lives:  %d");
        var playerTime = getdp("time").asString("Time:  %.2f");
        var playerAmmo = getip("ammo").asString("Ammo:  %d");

        Text score = setUIScoreText(getAppWidth() / 2);
        Text lifeScore = setUIText(70);
        Text timeCounter = setUIText(getAppWidth() - 270);
        Text ammoQuantity = setUIText(70, getAppHeight() - 50);


        score.textProperty().bind(playerScore);
        lifeScore.textProperty().bind(playerLife);
        timeCounter.textProperty().bind(playerTime);
        ammoQuantity.textProperty().bind(playerAmmo);
    }

    public Text setUIScoreText (int xPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setStroke(Color.GOLD);

        getWorldProperties().addListener("score", (prev, now) -> animationBuilder()
                .duration(Duration.seconds(0.5))
                .interpolator(Interpolators.BOUNCE.EASE_IN())
                .repeat(2)
                .autoReverse(true)
                .scale(textUI)
                .from(new Point2D(1, 1))
                .to(new Point2D(1.2, 1.2))
                .buildAndPlay());

        addUINode(textUI, xPosition, 50);

        return textUI;
    }

    public Text setUIText (int xPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setStroke(Color.GOLD);

        addUINode(textUI, xPosition, 50);

        return textUI;
    }

    public Text setUIText (int xPosition, int yPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setTranslateX(xPosition);
        textUI.setTranslateY(yPosition);
        textUI.setStroke(Color.GOLD);

        addUINode(textUI, xPosition, yPosition);

        return textUI;
    }

    @Override
    protected void onUpdate(double tpf) {
        Duration userTime = Duration.seconds(getd("time"));
        Integer userScore = geti("score");

        inc("time", tpf);

        if (survivor.getRightX() > getAppWidth() ||
                survivor.getBottomY() > getAppHeight() ||
            survivor.getX() <= 0 || survivor.getY() <= 0
        ) {
            survivor.setPosition(getAppWidth() / 2.0 - 15, getAppHeight() / 2.0 - 15);

            inc("lives", -1);
        }

        if (getWorldProperties().getInt("lives") <= 0) {
            endGameMessage(userTime, userScore);
        }
    }

    private void endGameMessage(Duration userTime, Integer userScore) {
        showMessage("\n" + getRandomDeathMessage() +
                        String.format("\n\nPoints: %d", userScore) +
                        String.format("\n\nTime: %.2f sec!", userTime.toSeconds()),
                () -> getGameController().startNewGame());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
