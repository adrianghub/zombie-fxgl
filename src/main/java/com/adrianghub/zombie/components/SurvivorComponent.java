package com.adrianghub.zombie.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitters;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class SurvivorComponent extends Component {

    public void turnLeft() {
        entity.rotateBy(-5);
    }

    public void turnRight() {
        entity.rotateBy(5);
    }

    public void moveForward() {
        Vec2 dir = Vec2.fromAngle(entity.getRotation())
                .mulLocal(4);
        entity.translate(dir);
    }

    public void moveBackward() {
        Vec2 dir = Vec2.fromAngle(-entity.getRotation())
                .mulLocal(2);
        entity.translate(dir);
    }

    public void shoot() {
        Point2D center = entity.getCenter();

        Vec2 dir = Vec2.fromAngle(entity.getRotation());

        spawn("bullet", new SpawnData(center.getX(), center.getY()).put("dir", dir.toPoint2D()));

        inc("ammo", -1);
    }

    public void playSpawnAnimation() {
        var emitter = ParticleEmitters.newExplosionEmitter(150);
        emitter.setSize(1, 16);
        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setStartColor(Color.color(1.0, 0.0, 0.5, 0.5));
        emitter.setEndColor(Color.LIGHTGOLDENRODYELLOW);
        emitter.setMaxEmissions(5);
        emitter.setEmissionRate(0.5);

        entityBuilder()
                .at(entity.getPosition())
                .with(new ParticleComponent(emitter))
                .with(new ExpireCleanComponent(Duration.seconds(3)))
                .buildAndAttach();

        animationBuilder()
                .fadeIn(entity)
                .buildAndPlay();
    }
}
