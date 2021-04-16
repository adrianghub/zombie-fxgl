package com.adrianghub.zombie.components;

import com.adrianghub.zombie.WeaponType;
import com.adrianghub.zombie.factories.WeaponsFactory;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitters;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class SurvivorComponent extends Component {

    public void turnLeft() {
        entity.rotateBy(-3);
    }

    public void turnRight() {
        entity.rotateBy(3);
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
        Point2D zombiePosition = entity.getCenter();

        Point2D bulletSpawnDirection = (Vec2.fromAngle(entity.getRotation())).toPoint2D();

        WeaponType type = geto("weaponType");

        List<Entity> bullets = new ArrayList<>();

        switch (type) {
            case TRIPLE_SHOTGUN:

                bullets.add(spawnBullet(zombiePosition.subtract(
                        new Point2D(bulletSpawnDirection.getY(), -bulletSpawnDirection.getX())
                                .normalize()
                                .multiply(10)), bulletSpawnDirection));
                inc("ammo", -1);


            case SHOTGUN:

                bullets.add(spawnBullet(zombiePosition.add
                        (new Point2D(bulletSpawnDirection.getY(), -bulletSpawnDirection.getX())
                        .normalize()
                        .multiply(10)), bulletSpawnDirection));
                inc("ammo", -1);


            case PISTOL:
            default:
                bullets.add(spawnBullet(zombiePosition, bulletSpawnDirection));
                inc("ammo", -1);
                break;
        }

    }

    private Entity spawnBullet(Point2D position, Point2D direction) {
        var data = new SpawnData(position.getX(), position.getY())
                .put("dir", direction);
        var e =  spawn("bullet", data);

        WeaponsFactory.respawnBullet(e, data);

        return e;
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
