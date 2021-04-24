package com.adrianghub.zombie.factories;

import com.adrianghub.zombie.ZombieApp;
import com.adrianghub.zombie.components.BossComponent;
import com.adrianghub.zombie.components.SpyComponent;
import com.adrianghub.zombie.components.SurvivorComponent;
import com.adrianghub.zombie.components.WandererComponent;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.RandomMoveComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

import static com.adrianghub.zombie.Config.*;
import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.adrianghub.zombie.ui.ProgressBarHP.createHpView;
import static com.almasb.fxgl.dsl.FXGL.*;

public class CharactersFactory implements EntityFactory {

    @Spawns("survivor")
    public Entity newSurvivor(SpawnData data) {

        var hp = new HealthIntComponent(SURVIVOR_HP);

        var hpView = createHpView(hp, Color.LIMEGREEN, 60, -45, 15, 90);

        return entityBuilder(data)
                .type(SURVIVOR)
                .viewWithBBox("survivor.png")
                .view(hpView)
                .with(hp)
                .zIndex(50)
                .with(new SurvivorComponent())
                .collidable()
                .build();
    }

    @Spawns("wanderer")
    public Entity newWanderer(SpawnData data) {

        var hp = new HealthIntComponent(new WandererComponent(WANDERER_SPEED).setHPPoints());

        var hpView = createHpView(hp, Color.VIOLET);

        return entityBuilder(data)
                .type(WANDERER)
                .viewWithBBox("zombie-wan.png")
                .view(hpView)
                .with(hp)
                .with(new WandererComponent(WANDERER_SPEED))
                .with(new RandomMoveComponent(
                        new Rectangle2D(0, 0,
                                getAppWidth() + 10, getAppHeight() + 10), 150))
                .collidable()
                .build();
    }

    @Spawns("spy")
    public Entity newSpy(SpawnData data) {
        var hp = new HealthIntComponent(SPY_HP);

        var hpView = createHpView(hp, Color.DIMGREY);

        return entityBuilder(data)
                .type(SPY)
                .viewWithBBox("zombie-spy.png")
                .view(hpView)
                .with(hp)
                .collidable()
                .with(new SpyComponent(FXGL.<ZombieApp>getAppCast().getSurvivor(), SPY_SPEED))
                .build();
    }

    @Spawns("boss")
    public Entity newBoss(SpawnData data) {
        var hp = new HealthIntComponent(BOSS_HP);

        var hpView = createHpView(hp, Color.ORANGERED);

        ParticleEmitter emitter = ParticleEmitters.newFireEmitter();
        emitter.setStartColor(Color.color(1.0, 1.0, 0.5, 0.5));
        emitter.setEndColor(Color.color(1.0, 1.0, 1.0, 0.5));

        emitter.setBlendMode(BlendMode.DIFFERENCE);
        emitter.setSize(5, 55);
        emitter.setEmissionRate(1);

        return entityBuilder(data)
                .type(BOSS)
                .viewWithBBox("zombie-boss.png")
                .view(hpView)
                .with(hp)
                .with(new ParticleComponent(emitter))
                .zIndex(100)
                .collidable()
                .with(new RandomMoveComponent(
                        new Rectangle2D(0, 0,
                                getAppWidth(), getAppHeight()), 50))
                .with(new BossComponent(FXGL.<ZombieApp>getAppCast().getSurvivor(), 100))
                .build();
    }
}
