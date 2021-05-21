package com.gameengine.game.gui;

import com.gameengine.engine.GameContainer;
import com.gameengine.game.GameManager;
import com.gameengine.game.StartFile;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;

public class GameStage {
    private static final double WIDTH = 1280;
    private static final double HEIGHT = 720;

    private Stage mainStage;
    private AnchorPane mainPane;
    private AnchorPane subPane;
    private Rectangle r;

    private boolean isHidden;
    private MenuSubScene menuSubScene;

    public GameStage(GameContainer gc, GameManager gm){
        Scene sc = gc.getWindow().getMainScene();
        mainPane = gc.getWindow().getMainPane();
        subPane = new AnchorPane();

        r = new Rectangle(0, 0, (int)WIDTH, (int)HEIGHT);
        r.setFill(Color.BLACK);
        r.setOpacity(0.4);
        menuSubScene = new MenuSubScene(1);
        CustomButton cb = new CustomButton("Exit");
        cb.setLayoutX(170);
        cb.setLayoutY(200);
        cb.setOnMouseClicked(mouseEvent -> {
            try {
                gc.stop();
                restart(gm);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        subPane.setOpacity(0);

        mainPane.setOpacity(1);
        subPane.getChildren().add(r);
        mainPane.getChildren().add(subPane);
        subPane.getChildren().add(menuSubScene);
        menuSubScene.getPane().getChildren().add(cb);
        isHidden = true;

        mainStage = new Stage();
        mainStage.setScene(sc);
        mainStage.show();
    }

    private void restart(GameManager gm){
        gm = null;
        Platform.exit();
    }

    public void showPauseMenu(){
        isHidden = false;
        FadeTransition ftm = new FadeTransition(Duration.seconds(0.5), subPane);

        ftm.setFromValue(0);
        ftm.setToValue(1);
        ftm.setOnFinished(actionEvent -> subPane.setDisable(false));

        ftm.play();
    }

    public void hidePauseMenu(){
        isHidden = true;
        FadeTransition ftm = new FadeTransition(Duration.seconds(0.5), subPane);

        ftm.setFromValue(1);
        ftm.setToValue(0);
        ftm.setOnFinished(actionEvent ->  subPane.setDisable(true));

        ftm.play();
    }

    public void showDeathMenu(){
        isHidden = false;
        r.setOpacity(1);
        Font FONT = Font.loadFont(this.getClass().getResource("/GUI/pixelFont.ttf").toExternalForm(), 35);
        Label infoLabel = new Label("You died!");
        infoLabel.setFont(FONT);
        infoLabel.setPadding(new Insets(3));
        infoLabel.setLayoutX(200);
        infoLabel.setLayoutY(60);
        menuSubScene.getPane().getChildren().add(infoLabel);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(1), subPane);
        FadeTransition ftm = new FadeTransition(Duration.seconds(1), subPane);

        tt.setFromY(-1280);
        tt.setToY(0);

        ftm.setFromValue(0);
        ftm.setToValue(1);
        ftm.setOnFinished(actionEvent -> subPane.setDisable(false));

        ftm.play();
    }

    public boolean isHidden(){
        return isHidden;
    }

    public Stage getMainStage(){
        return mainStage;
    }
}
