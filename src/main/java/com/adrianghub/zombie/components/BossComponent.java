package com.adrianghub.zombie.components;

import com.adrianghub.zombie.ZombieApp;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BossComponent extends Component {

    private Entity player;

    private int speed;

    private Point2D velocity = Point2D.ZERO;

    private LocalTimer adjustDirectionTimer = FXGL.newLocalTimer();
    private Duration adjustDelay = Duration.seconds(0.15);

    public BossComponent(Entity player, int speed) {
        this.player = player;
        this.speed = speed;
    }

    @Override
    public void onAdded() {
        adjustVelocity(0.016);
        inc("numBosses", -1);
    }

    @Override
    public void onUpdate(double tpf) {
        move(tpf);
        followPlayer(tpf);
        rotate();
    }

    private void move(double tpf) {
        if (adjustDirectionTimer.elapsed(adjustDelay)) {
            adjustVelocity(tpf);
            adjustDirectionTimer.capture();
        }

        entity.translate(velocity);
    }

    private void followPlayer(double tpf) {
        Entity player = getEntity().getWorld().getSingleton(ZombieApp.EntityType.SURVIVOR);
        if (getEntity().distance(player) < 50) {
            getEntity().translateTowards(player.getCenter(), 1000 * tpf);
        }
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

    public void playSpawnAnimation() {
        var emitter = ParticleEmitters.newExplosionEmitter(150);
        emitter.setSize(1, 16);
        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setStartColor(Color.color(1.0, 0.0, 0.5, 0.5));
        emitter.setEndColor(Color.BLUEVIOLET);
        emitter.setMaxEmissions(5);
        emitter.setEmissionRate(0.5);

        entityBuilder()
                .at(entity.getPosition())
                .with(new ParticleComponent(emitter))
                .collidable()
                .buildAndAttach();

        animationBuilder()
                .fadeIn(entity)
                .buildAndPlay();
    }

    public void playDeathAnimation(Point2D spawnPoint) {
        var emitter = ParticleEmitters.newExplosionEmitter(random(25, 75));
        emitter.setSize(1, random(8, 16));
        emitter.setBlendMode(BlendMode.DARKEN);
        emitter.setStartColor(Color.color(1.0, 0.2, 0.2, 0.5));
        emitter.setEndColor(Color.DARKRED);
        emitter.setMaxEmissions(1);
        emitter.setEmissionRate(0.5);

        entityBuilder()
                .at(spawnPoint)
                .with(new ParticleComponent(emitter))
                .with(new ExpireCleanComponent(Duration.seconds(0.66)))
                .buildAndAttach();
    }

    public void playBloodTraceAnimation(Point2D spawnPoint) {
        entityBuilder()
                .at(spawnPoint)
                .view("blood-trace.png")
                .with(new ExpireCleanComponent(Duration.seconds(5)).animateOpacity())
                .buildAndAttach();

        animationBuilder()
                .fadeIn(entity)
                .buildAndPlay();
    }
}