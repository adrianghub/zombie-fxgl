package com.adrianghub.zombie.factories;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.ui.FontType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.util.Duration.seconds;

public class UIPartsFactory implements EntityFactory {

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

    @Spawns("textScore")
    public Entity newTextS(SpawnData data) {
        String text = data.get("text");

        var e = entityBuilder(data)
                .view(getUIFactoryService().newText(text, Color.GOLD, FontType.TEXT, 24))
                .with(new ExpireCleanComponent(seconds(1)).animateOpacity())
                .build();

        animationBuilder()
                .duration(seconds(1))
                .interpolator(Interpolators.CUBIC.EASE_OUT())
                .translate(e)
                .from(new Point2D(data.getX(), data.getY()))
                .to(new Point2D(data.getX(), data.getY() - 30))
                .buildAndPlay();

        return e;
    }
}
