package com.adrianghub.zombie.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class SpyComponent extends Component {

    private Entity player;

    private int speed;

    private Point2D velocity = Point2D.ZERO;

    private LocalTimer adjustDirectionTimer = FXGL.newLocalTimer();
    private Duration adjustDelay = Duration.seconds(0.15);

    public SpyComponent(Entity player, int speed) {
        this.player = player;
        this.speed = speed;
    }

    private static final String[] bloodTrace = {
            "blood-trace.png",
            "blood-trace2.png",
            "blood-trace3.png",
            "blood-trace4.png",
    };

    private static String getRandomBloodTrace() {
        return bloodTrace[random(0, 3)];
    }

    @Override
    public void onAdded() {
        adjustVelocity(0.016);
    }

    @Override
    public void onUpdate(double tpf) {
        move(tpf);
        rotate();
    }

    private void move(double tpf) {
        if (adjustDirectionTimer.elapsed(adjustDelay)) {
            adjustVelocity(tpf);
            adjustDirectionTimer.capture();
        }

        entity.translate(velocity);
    }

    private void adjustVelocity(double tpf) {
        Point2D directionToPlayer = player.getCenter()
                .subtract(entity.getCenter())
                .normalize()
                .multiply(speed);

        velocity = velocity.add(directionToPlayer).multiply(tpf);
    }

    private void rotate() {
        if (!velocity.equals(Point2D.ZERO)) {
            entity.rotateToVector(velocity);
        }
    }

    public void playDeathAnimation(Point2D spawnPoint) {
            entityBuilder()
                    .at(spawnPoint)
                    .view(texture("explosion.png").toAnimatedTexture(16, Duration.seconds(0.66)).play())
                    .with(new ExpireCleanComponent(Duration.seconds(0.66)))
                    .buildAndAttach();
    }

    public void playBloodTraceAnimation(Point2D spawnPoint) {
        entityBuilder()
                .at(spawnPoint)
                .view(getRandomBloodTrace())
                .with(new ExpireCleanComponent(Duration.seconds(5)).animateOpacity())
                .buildAndAttach();

        animationBuilder()
                .fadeIn(entity)
                .buildAndPlay();
    }
}