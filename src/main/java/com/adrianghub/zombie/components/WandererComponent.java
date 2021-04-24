package com.adrianghub.zombie.components;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitters;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class WandererComponent extends Component {

    public WandererComponent(int speed) {
    }

    private static final String[] bloodTrace = {
            "blood-trace.png",
            "blood-trace2.png",
            "blood-trace3.png",
            "blood-trace4.png",
    };

    private static String getRandomBloodTrace() {
        return bloodTrace[random(0, 3)];
    }

    public void playDeathAnimation(Point2D spawnPoint) {
        var emitter = ParticleEmitters.newExplosionEmitter(random(25, 75));
        emitter.setSize(1, random(8, 16));
        emitter.setBlendMode(BlendMode.DARKEN);
        emitter.setStartColor(Color.color(1.0, 0.2, 0.2, 0.5));
        emitter.setEndColor(Color.DARKRED);
        emitter.setMaxEmissions(1);
        emitter.setEmissionRate(0.5);

        entityBuilder()
                .at(spawnPoint)
                .with(new ParticleComponent(emitter))
                .with(new ExpireCleanComponent(Duration.seconds(0.66)))
                .buildAndAttach();
    }

    public void playBloodTraceAnimation(Point2D spawnPoint) {
        entityBuilder()
                .at(spawnPoint)
                .view(getRandomBloodTrace())
                .with(new ExpireCleanComponent(Duration.seconds(5)).animateOpacity())
                .buildAndAttach();

        animationBuilder()
                .fadeIn(entity)
                .buildAndPlay();
    }

    public int setHPPoints() {
        return geti("score") > 8000 ? 3 : 1;
    }
}
