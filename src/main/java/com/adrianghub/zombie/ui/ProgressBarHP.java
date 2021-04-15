package com.adrianghub.zombie.ui;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.ui.ProgressBar;
import javafx.scene.paint.Color;

public class ProgressBarHP {
    public static ProgressBar createHpView(HealthIntComponent hp, Color color) {
        var hpView = new ProgressBar(false);

        hpView.setFill(color);
        hpView.setMaxValue(hp.getValue());
        hpView.setWidth(48);
        hpView.setTranslateY(-10);
        hpView.currentValueProperty().bind(hp.valueProperty());

        return hpView;

    }

    public static ProgressBar createHpView(HealthIntComponent hp, Color color, int width, double xPosition, double yPosition, double rotation) {
        var hpView = new ProgressBar(false);

        hpView.setFill(color);
        hpView.setMaxValue(hp.getValue());
        hpView.setWidth(width);
        hpView.setTranslateX(xPosition);
        hpView.setTranslateY(yPosition);
        hpView.setRotate(rotation);
        hpView.currentValueProperty().bind(hp.valueProperty());

        return hpView;

    }
}
