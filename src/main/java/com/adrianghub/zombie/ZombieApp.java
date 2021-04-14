package com.adrianghub.zombie;

import com.adrianghub.zombie.components.SpyComponent;
import com.adrianghub.zombie.components.SurvivorComponent;
import com.adrianghub.zombie.components.WandererComponent;
import com.adrianghub.zombie.factories.AnotherFactory;
import com.adrianghub.zombie.factories.ZombieAppFactory;
import com.adrianghub.zombie.menu.ZombieMainMenu;
import com.adrianghub.zombie.service.HighScoreService;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.SimpleGameMenu;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import javafx.animation.Interpolator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Random;

import static com.adrianghub.zombie.Config.*;
import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.util.Duration.*;
import static javafx.util.Duration.seconds;

public class ZombieApp extends GameApplication {

    private Entity survivor;
    private SurvivorComponent survivorComponent;

    public enum EntityType {
        SURVIVOR, BULLET, LAVA, WANDERER, SPY
    }

    private static final String demoEndMessage = "You have finally finished the demo. Thank you!";

    private static final String[] deathMessage = {
            "Sooo close...",
            "Ah, shit...Here we go again",
            "You're a dead meat",
            "Come back later :)",
    };

    private static String getRandomDeathMessage() {
        return deathMessage[random(0, 3)];
    }

    public Entity getSurvivor() {
        return survivor;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Survivor");
        settings.setVersion("0.1");
        settings.setFontUI("zombie.ttf");
        settings.setWidth(1440);
        settings.setHeight(900);
        settings.addEngineService(HighScoreService.class);
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            @NotNull
            public FXGLMenu newMainMenu() {
                return new ZombieMainMenu();
            }

            @Override
            @NotNull
            public FXGLMenu newGameMenu() {
                return new SimpleGameMenu();
            }
        });
    }

    @Override
    protected void onPreInit() {
        // preload explosion sprite sheet
        getAssetLoader().loadTexture("explosion.png", 80 * 48, 80);
    }

    @Override
    protected void initGame() {

        getGameWorld().addEntityFactory(new ZombieAppFactory());
        getGameWorld().addEntityFactory(new AnotherFactory());

        getGameScene().setBackgroundColor(Color.color(0, 0, 0.05, 0.5));

        spawn("verticalLava", 0, 0);
        spawn("verticalLava", getAppWidth() - 10, 0);

        spawn("horizontalLava", 0, 0);
        spawn("horizontalLava", 0, getAppHeight() - 10);

        survivor = spawn("survivor", getAppWidth() / 2.0 - 15, getAppHeight() / 2.0 - 15);
        survivorComponent = survivor.getComponent(SurvivorComponent.class);
        survivorComponent.playSpawnAnimation();


        getWorldProperties().<Integer>addListener("score", (prev, now) -> {
            getService(HighScoreService.class).setScore(now);

            if (now >= DEMO_SCORE)
                gameOver();
        });

        getWorldProperties().<Integer>addListener("lives", (prev, now) -> {
            if (now == 0)
                gameOver();
        });

        if (!IS_NO_ZOMBIES) {
            BooleanProperty wanderersLeft = new SimpleBooleanProperty();
            wanderersLeft.bind(getip("numWanderers").greaterThan(0));

            getGameTimer().runAtIntervalWhile(this::spawnWanderer, WANDERER_SPAWN_INTERVAL, wanderersLeft);

            BooleanProperty spiesLeft = new SimpleBooleanProperty();
            spiesLeft.bind(getip("numSpies").greaterThan(0));

            getGameTimer().runAtIntervalWhile(this::spawnSpy, SPY_SPAWN_INTERVAL, spiesLeft);

        }
    }

    private void spawnWanderer() {

        if (geti("numWanderers") < 1) return;

        inc("numWanderers", -1);

        Entity e = getGameWorld().create("wanderer", new SpawnData(random(0, getAppWidth()), random(0, getAppHeight())));

        runOnce(() -> {
            spawnWithScale(e, seconds(0.3), Interpolator.EASE_OUT);
        }, seconds(5));

    }

    private void spawnSpy() {

        if (geti("numSpies") < 1) return;

        inc("numSpies", -1);

        runOnce(() -> {
            spawnFadeIn("spy", new SpawnData(0, 0), seconds(0.3));

            spawnFadeIn("spy", new SpawnData(getAppWidth(), getAppHeight()), seconds(0.5));
        }, seconds(5));
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
        onKeyDown(KeyCode.SPACE, "Single shot", () -> survivor.getComponent(SurvivorComponent.class).shoot());
    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physics = getPhysicsWorld();

        CollisionHandler survivorZombie = new CollisionHandler(SURVIVOR, WANDERER) {

            @Override
            protected void onCollisionBegin(Entity survivor, Entity zombie) {

                killZombie(zombie);

                var hp = survivor.getComponent(HealthIntComponent.class);
                hp.setValue(hp.getValue() - 1);

                if (hp.isZero()) {
                    killZombie(zombie);

                    inc("lives", -1);

                    survivor.setPosition(getAppWidth() / 2.0 - 15, getAppHeight() / 2.0 - 15);
                    survivorComponent.playSpawnAnimation();
                    hp.setValue(3);
                }
            }
        };

        physics.addCollisionHandler(survivorZombie);
        physics.addCollisionHandler(survivorZombie.copyFor(SURVIVOR, SPY));

        CollisionHandler bulletZombie = new CollisionHandler(BULLET, WANDERER) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity zombie) {
                var hp = zombie.getComponent(HealthIntComponent.class);

                if (hp.getValue() > 1) {
                    bullet.removeFromWorld();
                    hp.damage(1);
                    return;
                }

                spawn("textScore", new SpawnData(zombie.getPosition()).put("text", "+1 kill"));
                bullet.removeFromWorld();

                killZombie(zombie);

                inc("score", +100);
            }
        };

        physics.addCollisionHandler(bulletZombie);
        physics.addCollisionHandler(bulletZombie.copyFor(BULLET, SPY));
    }

    private void killZombie(Entity zombie) {
        Point2D spawnPosition = zombie.getCenter().subtract(64, 64);

        if (zombie.isType(SPY)) {
            SpyComponent spyComponent = zombie.getComponent(SpyComponent.class);
            spyComponent.playDeathAnimation(spawnPosition);
            spyComponent.playBloodTraceAnimation(spawnPosition);
        } else {
            WandererComponent wandererComponent = zombie.getComponent(WandererComponent.class);
            wandererComponent.playDeathAnimation();
            wandererComponent.playBloodTraceAnimation();
        }

        zombie.removeFromWorld();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("time", 0.0);
        vars.put("score", 0);
        vars.put("lives", LIVES_AMOUNT);
        vars.put("ammo", 999);
        vars.put("numWanderers", WANDERERS_AMOUNT);
        vars.put("numSpies", SPIES_AMOUNT);
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

        Text introMessage = getUIFactoryService().newText("Alone against hordes of zombies...", Color.DARKRED, 38);

        setCenteredText(introMessage);
    }

    public void setCenteredText(Text message) {
        addUINode(message);

        centerText(message);

        animationBuilder()
                .duration(seconds(4))
                .autoReverse(true)
                .repeat(2)
                .fadeIn(message)
                .buildAndPlay();
    }

    public Text setUIScoreText(int xPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setStroke(Color.GOLD);

        getWorldProperties().addListener("score", (prev, now) -> animationBuilder()
                .duration(seconds(0.5))
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

    public Text setUIText(int xPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setStroke(Color.GOLD);

        addUINode(textUI, xPosition, 50);

        return textUI;
    }

    public Text setUIText(int xPosition, int yPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setTranslateX(xPosition);
        textUI.setTranslateY(yPosition);
        textUI.setStroke(Color.GOLD);

        addUINode(textUI, xPosition, yPosition);

        return textUI;
    }

    @Override
    protected void onUpdate(double tpf) {

        inc("time", tpf);

        if (survivor.getRightX() > getAppWidth() ||
                survivor.getBottomY() > getAppHeight() ||
                survivor.getX() <= 0 || survivor.getY() <= 0
        ) {

            var hp = survivor.getComponent(HealthIntComponent.class);
            hp.setValue(hp.getValue() - 1);
            survivor.setPosition(getAppWidth() / 2.0 - 15, getAppHeight() / 2.0 - 15);

            if (hp.isZero()) {

                inc("lives", -1);

                survivorComponent.playSpawnAnimation();

                hp.setValue(3);
            }
        }

        if (geti("numWanderers") < 1 && geti("numSpies") < 1) {
            Text levelMessage = getUIFactoryService().newText("Hordes of zombies coming up...", Color.DARKRED, 38);

            setCenteredText(levelMessage);

            inc("numWanderers", +5);
            inc("numSpies", +5);
        }
    }

    public String statsMessage() {
        Duration userTime = seconds(getd("time"));

        return "\n\nPoints: " + geti("score") +
                String.format("\nTime: %.2f sec!", userTime.toSeconds()) +
                "\n\nEnter your name";
    }

    public void gameOver() {
        getDialogService().showInputBox(geti("score") >= DEMO_SCORE ? demoEndMessage + statsMessage() : getRandomDeathMessage() +
                statsMessage(), s -> s.matches("[a-zA-Z]*"), name -> {
            getService(HighScoreService.class).commit(name);

            getSaveLoadService().saveAndWriteTask(SAVE_FILE_NAME).run();

            getGameController().gotoMainMenu();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
