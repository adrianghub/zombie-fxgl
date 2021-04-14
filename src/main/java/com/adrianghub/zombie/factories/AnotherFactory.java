package com.adrianghub.zombie.factories;

import com.adrianghub.zombie.components.SpyComponent;
import com.adrianghub.zombie.components.SurvivorComponent;
import com.adrianghub.zombie.components.WandererComponent;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
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

import static com.adrianghub.zombie.Config.*;
import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.almasb.fxgl.dsl.FXGL.*;

public class AnotherFactory implements EntityFactory {


    @Spawns("wanderer2")
    public Entity newWanderer(SpawnData data) {

        return entityBuilder(data)
                .type(WANDERER)
                .viewWithBBox("zombie-wan.png")
                .with(new WandererComponent(WANDERER_SPEED))
                .with(new RandomMoveComponent(
                        new Rectangle2D(0, 0,
                                getAppWidth() + 100, getAppHeight() + 100), 150))
                .collidable()
                .build();
    }
}
