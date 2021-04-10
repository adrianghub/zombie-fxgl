package com.adrianghub.zombie;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ZombieApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Zombie Invasion");
        settings.setFontUI("zombie.ttf");
        settings.setWidth(1024);
        settings.setHeight(720);
    }

    @Override
    protected void initGame() {

        getGameScene().setBackgroundColor(Color.color(0, 0, 0.05, 0.5));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
