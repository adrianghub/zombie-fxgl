package com.adrianghub.zombie;

import javafx.util.Duration;

import static javafx.util.Duration.seconds;

public class Config {
    public static final boolean IS_NO_ZOMBIES = false;

    public static final String SAVE_FILE_NAME = "hall_of_fame.txt";

    public static final Integer DEMO_SCORE = 10_000;

    public static final int LIVES_AMOUNT = 3;

    public static final Integer SURVIVOR_HP = 3;
    public static final Integer WANDERER_HP = 2;
    public static final Integer SPY_HP = 1;

    public static final Integer SPY_SPEED = 100;
    public static final Integer WANDERER_SPEED = 150;

    public static Duration WANDERER_SPAWN_INTERVAL = seconds(3);
    public static final Duration SPY_SPAWN_INTERVAL = seconds(5);

    public static final int WANDERERS_AMOUNT = 10;
    public static final int SPIES_AMOUNT = 20;
}
