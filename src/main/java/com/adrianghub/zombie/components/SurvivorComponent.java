package com.adrianghub.zombie.components;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;

public class SurvivorComponent extends Component {

    public void turnLeft() {
        entity.rotateBy(-5);
    }

    public void turnRight() {
        entity.rotateBy(5);
    }

    public void move() {
        Vec2 dir = Vec2.fromAngle(entity.getRotation())
                .mulLocal(4);
        entity.translate(dir);
    }
}
