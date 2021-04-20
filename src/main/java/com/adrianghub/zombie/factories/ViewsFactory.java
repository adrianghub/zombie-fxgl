package com.adrianghub.zombie.factories;

import com.adrianghub.zombie.components.AmmoComponent;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class ViewsFactory implements EntityFactory {

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

    @Spawns("ammo")
    public Entity newAmmo(SpawnData data) {

        return entityBuilder(data)
                .type(AMMO)
                .scale(1.25, 1.25)
                .viewWithBBox("bullet.png")
                .zIndex(100)
                .collidable()
                .with(new AmmoComponent())
                .with(new ExpireCleanComponent(Duration.seconds(10)))
                .build();
    }
}
