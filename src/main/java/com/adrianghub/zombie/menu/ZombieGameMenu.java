package com.adrianghub.zombie.menu;

import com.adrianghub.zombie.service.HighScoreService;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.scene.Scene;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

import static com.adrianghub.zombie.Config.SAVE_FILE_NAME;
import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.scene.input.KeyCode.*;

public class ZombieGameMenu extends FXGLMenu {

    private VBox scoresRoot = new VBox(10);
    private Node highScores;

    public ZombieGameMenu() {
        super(MenuType.MAIN_MENU);

        getContentRoot().getChildren().setAll(new Rectangle(getAppWidth(), getAppHeight(), Color.color(0.2, 0, 0, 0.2)));

        var title = getUIFactoryService().newText(getSettings().getTitle(), Color.web("FF7433"), 56.0);
        title.setStroke(Color.web("291418"));
        title.setStrokeWidth(4.5);
        title.setEffect(new Bloom(0.6));
        centerTextBind(title, getAppWidth() / 2.0 - 20, 235);

        getContentRoot().getChildren().addAll(title);

        var color = Color.web("b70d01");

        var blocks = new ArrayList<ColorBlock>();

        var blockStartX = getAppWidth() / 2.0 - 380;

        for (int i = 0; i < 15; i++) {
            var block = new ColorBlock(40, color);
            block.setTranslateX(blockStartX + i * 50);
            block.setTranslateY(100);

            blocks.add(block);
            getContentRoot().getChildren().add(block);
        }

        for (int i = 0; i < 15; i++) {
            var block = new ColorBlock(40, color);
            block.setTranslateX(blockStartX + i * 50);
            block.setTranslateY(220);

            blocks.add(block);
            getContentRoot().getChildren().add(block);
        }

        for (int i = 0; i < blocks.size(); i++) {
            var block = blocks.get(i);

            animationBuilder()
                    .delay(Duration.seconds(i * 0.05))
                    .duration(Duration.seconds(0.5))
                    .repeatInfinitely()
                    .autoReverse(true)
                    .animate(block.fillProperty())
                    .from(color)
                    .to(color.brighter().brighter())
                    .buildAndPlay(this);
        }

        var menuBox = new VBox(
                5,
                new MenuButton("Resume", this::fireResume),
                new MenuButton("New Game", this::fireNewGame),
                new MenuButton("Manual", this::instructions),
                new MenuButton("Exit", this::fireExit)
        );
        menuBox.setAlignment(Pos.TOP_CENTER);

        menuBox.setTranslateX(getAppWidth() / 2.0 - 125);
        menuBox.setTranslateY(getAppHeight() / 2.0 + 125);

        // useful for checking if nodes are properly centered
        var centeringLine = new Line(getAppWidth() / 2.0, 0, getAppWidth() / 2.0, getAppHeight());
        centeringLine.setStroke(Color.WHITE);

        scoresRoot.setPadding(new Insets(30));
        scoresRoot.setAlignment(Pos.TOP_LEFT);

        StackPane hsRoot = new StackPane(new Rectangle(400, 250, Color.color(0.2, 0, 0, 0.5)), scoresRoot);
        hsRoot.setAlignment(Pos.TOP_CENTER);
        hsRoot.setCache(true);
        hsRoot.setCacheHint(CacheHint.SPEED);
        hsRoot.setTranslateX(getAppWidth());
        hsRoot.setTranslateY(menuBox.getTranslateY());

        highScores = hsRoot;

        getContentRoot().getChildren().addAll(menuBox, hsRoot);
    }

    private void instructions() {
        GridPane pane = new GridPane();

        pane.setEffect(new DropShadow(5, 3.5, 3.5, Color.DARKRED));
        pane.setHgap(25);
        pane.setVgap(10);
        pane.addRow(0, getUIFactoryService().newText("Movement"), new HBox(4, new KeyView(W), new KeyView(S), new KeyView(A), new KeyView(D)));
        pane.addColumn(2, new HBox(4, new KeyView(UP), new KeyView(DOWN), new KeyView(LEFT), new KeyView(RIGHT)));
        pane.addRow(2, getUIFactoryService().newText("Shoot"), new KeyView(SPACE));

        getDialogService().showBox("Manual", pane, getUIFactoryService().newButton("OK"));
    }

    private static class MenuButton extends Parent {
        MenuButton(String name, Runnable action) {
            var text = getUIFactoryService().newText(name, Color.WHITESMOKE, 52.0);
            text.setStrokeWidth(1.5);
            text.strokeProperty().bind(text.fillProperty());

            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.DARKRED)
                            .otherwise(Color.WHITESMOKE)
            );

            setOnMouseClicked(e -> action.run());

            setPickOnBounds(true);

            getChildren().add(text);
        }
    }
}