package com.gameengine.game.gui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class MenuSubScene extends SubScene {

    private final String BACKGROUND_IMAGE = "file:src/main/resources/GUI/pane.png";

    private boolean isHidden = true;

    public MenuSubScene() {
        super(new AnchorPane(), 640, 420);
        prefHeight(640);
        prefWidth(420);

        BackgroundImage image = new BackgroundImage(new Image(BACKGROUND_IMAGE, 640, 420, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(image));

        setLayoutX(1280);
        setLayoutY(130);

        isHidden = true;
    }

    public void moveSubScene(){
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.4));
        transition.setNode(this);

        transition.setToX(-720);
//        isHidden= false;

        System.out.println(isHidden);

        transition.play();
    }

    public void removeSubScene(){
        FadeTransition tt = new FadeTransition(Duration.seconds(0.3), this);

        tt.setFromValue(1);
        tt.setToValue(0);

        tt.setOnFinished(actionEvent -> {
            this.setOpacity(1);
            setTranslateX(720);
//            setLayoutX(getLayoutX() + 676);
        });
        tt.play();
    }

    public AnchorPane getPane(){
        return (AnchorPane) this.getRoot();
    }

    public boolean isHidden(){
        return this.isHidden;
    }
}
