package com.adrianghub.zombie.handlers;

import com.adrianghub.zombie.components.BossComponent;
import com.adrianghub.zombie.components.SpyComponent;
import com.adrianghub.zombie.components.WandererComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

import static com.adrianghub.zombie.ZombieApp.EntityType.*;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

public class ActionHandler {

    public static void killZombie(Entity zombie) {
        Point2D spawnAlignedZombiePosition = zombie.getCenter().subtract(64, 64);
        Point2D spawnZombiePosition = zombie.getPosition();


        if (zombie.isType(BOSS)) {
            BossComponent bossComponent = zombie.getComponent(BossComponent.class);
            bossComponent.playBloodTraceAnimation(spawnZombiePosition);
            bossComponent.playSpawnAnimation();
        } else {

            if (zombie.isType(SPY)){
                SpyComponent spyComponent = zombie.getComponent(SpyComponent.class);
                spyComponent.playDeathAnimation(spawnAlignedZombiePosition);
                spyComponent.playBloodTraceAnimation(spawnZombiePosition);
            } else {
                WandererComponent wandererComponent = zombie.getComponent(WandererComponent.class);
                wandererComponent.playDeathAnimation(spawnZombiePosition);
                wandererComponent.playBloodTraceAnimation(spawnZombiePosition);
            }

            zombie.removeFromWorld();
        }
    }

    public static void spawnGameLava() {
        spawn("verticalLava", 0, 0);
        spawn("verticalLava", getAppWidth() - 10, 0);

        spawn("horizontalLava", 0, 0);
        spawn("horizontalLava", 0, getAppHeight() - 10);
    }

    public static int randomSpawnPosition(int coordinate) {
        return random(50, coordinate - 50);
    }

    public static void spawnRunner(String entityName, int times) {

        int[] timesArr = java.util.stream.IntStream.rangeClosed(0, times).toArray();

        for (int i = 1; i < timesArr.length; i++) {
            spawn(entityName, randomSpawnPosition(getAppWidth()), randomSpawnPosition(getAppHeight()));
        }
    }

    public static void incrementLevelRunner(int buffTimes, int minScore, int maxScore, int spawnPower) {
        inc("buff", buffTimes);
        inc("score", random(minScore, maxScore));

        inc("numWanderers", spawnPower * geti("buff"));
        inc("numSpies", spawnPower * geti("buff"));
    }
}
