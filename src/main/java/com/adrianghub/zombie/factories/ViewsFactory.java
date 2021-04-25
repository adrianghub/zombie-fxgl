package com.adrianghub.zombie.factories;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.adrianghub.zombie.ZombieApp.EntityType.LAVA;
import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.util.Duration.seconds;

public class ViewsFactory implements EntityFactory {

    @Spawns("background")
    public Entity newBg(SpawnData data) {
        return entityBuilder(data)
                .view("dark-forest.png").opacity(0.5)
                .build();
    }

    @Spawns("dangerOverlay")
    public Entity newDOverlay(SpawnData data) {
        var e = entityBuilder(data)
                .view(new Rectangle(getAppWidth(), getAppHeight(), Color.color(0.375, 0, 0, 0.5)))
                .with(new ExpireCleanComponent(seconds(0.5)).animateOpacity())
                .build();

        animationBuilder()
                .duration(seconds(0.5))
                .fadeOut(e)
                .buildAndPlay();

        return e;
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
}
