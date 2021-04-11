package com.adrianghub.zombie.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

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
}
