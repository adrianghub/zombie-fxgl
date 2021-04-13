package com.adrianghub.zombie;

import javafx.util.Duration;

import static javafx.util.Duration.seconds;

public class Config {
    public static final boolean IS_NO_ZOMBIES = false;

    public static final String SAVE_FILE_NAME = "hall_of_fame.txt";

    public static final Integer DEMO_SCORE = 10_000;

    public static final Integer SURVIVOR_HP = 3;
    public static final Integer ZOMBIE_HP = 1;

    public static final Integer SPY_SPEED = 100;
    public static final Integer WANDERER_SPEED = 150;

    public static final Duration WANDERER_SPAWN_INTERVAL = seconds(2);
    public static final Duration SPY_SPAWN_INTERVAL = seconds(2.5);
}
