package com.adrianghub.zombie;

import com.adrianghub.zombie.components.SurvivorComponent;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.components.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.ui.FontType;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class ZombieAppFactory implements EntityFactory {

    @Spawns("survivor")
    public Entity newSurvivor(SpawnData data) {

        var hp = new HealthIntComponent(3);

        var hpView = createHpView(hp, Color.LIMEGREEN, 60, -45, 15 , 90);

        return entityBuilder(data)
                .type(SURVIVOR)
                .viewWithBBox("survivor.png")
                .view(hpView)
                .with(hp)
                .with(new SurvivorComponent())
                .collidable()
                .build();
    }

    @Spawns("wanderer")
    public Entity newWanderer(SpawnData data) {

        var hp = new HealthIntComponent(1);

        var hpView = createHpView(hp, Color.VIOLET);

        return entityBuilder(data)
                .type(ZOMBIE)
                .viewWithBBox("zombie-wan.png")
                .view(hpView)
                .with(hp)
                .with(new RandomMoveComponent(
                        new Rectangle2D(0, 0,
                                getAppWidth() + 100, getAppHeight() + 100), 150))
                .collidable()
                .build();
    }


    public ProgressBar createHpView(HealthIntComponent hp, Color color) {
        var hpView = new ProgressBar(false);

        hpView.setFill(color);
        hpView.setMaxValue(hp.getValue());
        hpView.setWidth(48);
        hpView.setTranslateY(-10);
        hpView.currentValueProperty().bind(hp.valueProperty());

        return hpView;

    }

    public ProgressBar createHpView(HealthIntComponent hp, Color color, int width, double xPosition, double yPosition, double rotation) {
        var hpView = new ProgressBar(false);

        hpView.setFill(color);
        hpView.setMaxValue(hp.getValue());
        hpView.setWidth(width);
        hpView.setTranslateX(xPosition);
        hpView.setTranslateY(yPosition);
        hpView.setRotate(rotation);
        hpView.currentValueProperty().bind(hp.valueProperty());

        return hpView;

    }

    @Spawns("bullet")
    public Entity newBullet(SpawnData data) {
        Point2D direction = data.get("dir");

        return entityBuilder(data)
                .type(BULLET)
                .viewWithBBox("bullet.png")
                .collidable()
                .with(new ProjectileComponent(direction, 500))
                .with(new OffscreenCleanComponent())
                .build();
    }

    @Spawns("explosion")
    public Entity newExplosion(SpawnData data) {
        return entityBuilder(data)
                .view(texture("explosion.png").toAnimatedTexture(16, Duration.seconds(0.66)).play())
                .with(new ExpireCleanComponent(Duration.seconds(0.66)))
                .build();
    }

    @Spawns("bloodTrace")
    public Entity newBloodT(SpawnData data) {
        return entityBuilder(data)
                .view("blood-trace.png")
                .build();
    }

    @Spawns("verticalLava")
    public Entity newVLava(SpawnData data) {
        return entityBuilder(data)
                .type(LAVA)
                .viewWithBBox(new Rectangle(10, getAppHeight(), Color.ORANGERED))
                .collidable()
                .build();
    }

    @Spawns("horizontalLava")
    public Entity newHLava(SpawnData data) {
        return entityBuilder(data)
                .type(LAVA)
                .viewWithBBox(new Rectangle(getAppWidth(), 10, Color.RED))
                .collidable()
                .build();
    }

    @Spawns("textScore")
    public Entity newTextS(SpawnData data) {
        String text = data.get("text");

        var e = entityBuilder(data)
                .view(getUIFactoryService().newText(text, Color.GOLD, FontType.TEXT, 24))
                .with(new ExpireCleanComponent(Duration.seconds(1)).animateOpacity())
                .build();

        animationBuilder()
                .duration(Duration.seconds(1))
                .interpolator(Interpolators.CUBIC.EASE_OUT())
                .translate(e)
                .from(new Point2D(data.getX(), data.getY()))
                .to(new Point2D(data.getX(), data.getY() - 30))
                .buildAndPlay();

        return e;
    }
}
