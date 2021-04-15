package com.adrianghub.zombie.ui;

import com.adrianghub.zombie.service.HighScoreService;
import com.almasb.fxgl.animation.Interpolators;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.adrianghub.zombie.Config.DEMO_SCORE;
import static com.adrianghub.zombie.Config.SAVE_FILE_NAME;
import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static javafx.util.Duration.seconds;

public class UIText {

    private static final String demoEndMessage = "You have finally finished the demo. Thank you!";

    private static final String[] deathMessage = {
            "Sooo close...",
            "Ah, shit...Here we go again",
            "You're a dead meat",
            "Come back later :)",
    };

    private static String getRandomDeathMessage() {
        return deathMessage[random(0, 3)];
    }

    public static void setCenteredText(Text message) {
        addUINode(message);

        centerText(message);

        animationBuilder()
                .duration(seconds(3))
                .autoReverse(true)
                .repeat(2)
                .fadeIn(message)
                .buildAndPlay();
    }

    public static void setCenteredText(Text message, Duration delay) {
        addUINode(message);

        centerText(message);

        animationBuilder()
                .delay(delay)
                .interpolator(Interpolators.CUBIC.EASE_OUT())
                .duration(seconds(1))
                .autoReverse(true)
                .repeat(2)
                .fadeIn(message)
                .buildAndPlay();
    }

    public static Text setUIScoreText(int xPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setStroke(Color.GOLD);

        getWorldProperties().addListener("score", (prev, now) -> animationBuilder()
                .duration(seconds(0.5))
                .interpolator(Interpolators.BOUNCE.EASE_IN())
                .repeat(2)
                .autoReverse(true)
                .scale(textUI)
                .from(new Point2D(1, 1))
                .to(new Point2D(1.2, 1.2))
                .buildAndPlay());

        addUINode(textUI, xPosition, 50);

        return textUI;
    }

    public static Text setUIText(int xPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setStroke(Color.GOLD);

        addUINode(textUI, xPosition, 50);

        return textUI;
    }

    public static Text setUIText(int xPosition, int yPosition) {
        Text textUI = getUIFactoryService().newText("", Color.WHITE, 32);
        textUI.setTranslateX(xPosition);
        textUI.setTranslateY(yPosition);
        textUI.setStroke(Color.GOLD);

        addUINode(textUI, xPosition, yPosition);

        return textUI;
    }

    public static void displayEndGameInfoBox() {
        getDialogService().showInputBox(geti("score") >= DEMO_SCORE ? demoEndMessage + statsMessage() : getRandomDeathMessage() +
                statsMessage(), s -> s.matches("[a-zA-Z]*"), name -> {
            getService(HighScoreService.class).commit(name);

            getSaveLoadService().saveAndWriteTask(SAVE_FILE_NAME).run();

            getGameController().gotoMainMenu();

        });
    }

    public static String statsMessage() {
        Duration userTime = seconds(getd("time"));

        return "\n\nPoints: " + geti("score") +
                String.format("\nTime: %.2f sec!", userTime.toSeconds()) +
                "\n\nEnter your name";
    }
}
