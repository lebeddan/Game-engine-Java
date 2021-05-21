package com.gameengine.game.gui;

import com.gameengine.engine.audio.SoundClip;
import com.gameengine.game.GameManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewManager {
    private static final double WIDTH = 1280;
    private static final double HEIGHT = 720;

    private static final int MENU_BUTTONS_START_X = 200;
    private static final int MENU_BUTTONS_START_Y = 150;

    private SoundClip clip = new SoundClip("/Sounds/menuMusic.mp3", "bg");

//    private final SoundClip bgMusic = new SoundClip("gameOver.wav");

    private final Font FONT;

    private Stage mainStage;
    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage gameStage;
    private GameManager gm;

    private MenuSubScene sceneToHide;

    private MenuSubScene startSubccsene;
    private MenuSubScene multiplayerSubcscene;
    private MenuSubScene optionsSubccsene;

//    private MenuSubScene shipChooseSubcsene;

    List<CustomButton> menuButton;

    public ViewManager(GameManager gm, Stage stage) throws IOException {
        this.FONT = Font.loadFont(this.getClass().getResource("/GUI/pixelFont.ttf").toExternalForm(), 35);
        this.gameStage = stage;
        this.gm = gm;
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainStage = new Stage();
        menuButton = new ArrayList<>();
//        mainStage.initStyle(StageStyle.UNDECORATED);

//        Media sound = new Media("C:\\Users\\Vasily\\IdeaProjects\\TestEngine\\src\\main\\resources\\Sounds\\gameOver.wav");
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);
//        System.out.println(this.getClass().getResource("/Sounds/gameBGmusic.mp3").toExternalForm());

//        File f = new File("src/main/resources/Sounds/gameOver.wav");

//        System.out.println(f.toURI().toString());
        clip.loop();
        clip.setVolume(0.5);
        clip.play();

//        bgMusic.play();

        mainStage.setScene(mainScene);
        createLogo();
        createBackground();
        createSubScenes();
        createButtons();
//        bgMusic.loop();
//        bgMusic.play();
//        mainStage.hide();
    }

    private void createButtons(){
        createStartButton();
        createMultiplayerButton();
        createOptionsButton();
        createExitButton();
    }

    private void createExitButton() {
        CustomButton exitButton = new CustomButton("Exit");
        addMenuButton(exitButton);

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               Platform.exit();
            }
        });
    }

    private void createOptionsButton() {
        CustomButton optionsButton = new CustomButton("Options");
        addMenuButton(optionsButton);

        optionsButton.setOnMouseClicked(mouseEvent -> {
            showSubScene(optionsSubccsene);
        });
    }

    private void createMultiplayerButton() {
        CustomButton multiplayerButton = new CustomButton("Multiplayer");
        addMenuButton(multiplayerButton);

        multiplayerButton.setOnMouseClicked(mouseEvent -> {
            showSubScene(multiplayerSubcscene);
        });
    }

    private void createStartButton() {
        CustomButton startButton = new CustomButton("Play");
        addMenuButton(startButton);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent actionEvent) {
               showSubScene(startSubccsene);
           }
         });

    }

    private void addMenuButton(CustomButton button){
        button.setLayoutX(MENU_BUTTONS_START_X);
        button.setLayoutY(MENU_BUTTONS_START_Y + menuButton.size() * 100);
        menuButton.add(button);
        mainPane.getChildren().add(button);
    }

    private void showSubScene(MenuSubScene subscene){
        if(sceneToHide != null) {
            if (sceneToHide == subscene) {
                System.out.println("tady");
                sceneToHide.removeSubScene();
                sceneToHide = null;
            } else {
                System.out.println("Here");
                sceneToHide.removeSubScene();
                subscene.moveSubScene();
                sceneToHide = subscene;
            }
        } else {
            System.out.println("THis");
            subscene.moveSubScene();
            sceneToHide = subscene;
        }
    }

    private void createSubScenes(){
        createPlayStartSubScene();

        createOptionsSubScene();

        createMultiplayerSubScene();
    }

    private void createOptionsSubScene() {
        optionsSubccsene = new MenuSubScene();
        mainPane.getChildren().add(optionsSubccsene);

        draw_volume(optionsSubccsene);

        Label infoLabel = new Label("Music volume in main menu");
        infoLabel.setFont(FONT);
        infoLabel.setPadding(new Insets(3));
        infoLabel.setLayoutX(100);
        infoLabel.setLayoutY(10);

        int curVol = -1;
        for(int i = 0; i < clip.getVolume()*10; i++){
            optionsSubccsene.getPane().getChildren().get(i).setOpacity(1);
            curVol++;
        }

        CustomButtonPlusMinus plus = new CustomButtonPlusMinus("", "+");
        plus.setLayoutX(330);
        plus.setLayoutY(150);

        AtomicInteger finalCurVol = new AtomicInteger(curVol);
        plus.setOnMouseClicked(mouseEvent -> {
            if(clip.getVolume() < 1) {
                clip.addVolume((float) 0.1);
                finalCurVol.getAndIncrement();
                optionsSubccsene.getPane().getChildren().get(finalCurVol.get()).setOpacity(1);
            }
        });

        CustomButtonPlusMinus minus = new CustomButtonPlusMinus("", "-");
        minus.setLayoutX(170);
        minus.setLayoutY(150);

        minus.setOnMouseClicked(mouseEvent -> {
            if(clip.getVolume() > 0) {
                optionsSubccsene.getPane().getChildren().get(finalCurVol.get()).setOpacity(0);
                finalCurVol.getAndDecrement();
                clip.addVolume((float) -0.1);
            }
        });

        optionsSubccsene.getPane().getChildren().add(plus);
        optionsSubccsene.getPane().getChildren().add(minus);
        optionsSubccsene.getPane().getChildren().add(infoLabel);
    }

    public void draw_volume(MenuSubScene scene){
        int firstPos = 120;
        for(float i = 0; i < 1; i+=0.1){
            Rectangle r = new Rectangle();
            r.setX(firstPos);
            r.setY(250);
            r.setWidth(30);
            r.setHeight(84);
            r.setFill(Color.rgb(129, 148, 71));
            r.setStroke(Color.BLACK);
            r.setOpacity(0);
            scene.getPane().getChildren().add(r);
            firstPos += 40;
        }
    }

    private void createMultiplayerSubScene() {
        multiplayerSubcscene = new MenuSubScene();
        mainPane.getChildren().add(multiplayerSubcscene);

        Label infoLabel = new Label("Join or create a server");
        infoLabel.setFont(FONT);
        infoLabel.setPadding(new Insets(3));
        infoLabel.setLayoutX(100);
        infoLabel.setLayoutY(10);

        Label nameLabel = new Label("Enter your name: ");
        nameLabel.setFont(FONT);
        nameLabel.setPadding(new Insets(3));
        nameLabel.setLayoutX(150);
        nameLabel.setLayoutY(45);

        CustomTextField tfname = new CustomTextField();
        tfname.setLayoutX(180);
        tfname.setLayoutY(95);

        CustomTextField tfServ = new CustomTextField();
        tfServ.setLayoutX(320);
        tfServ.setLayoutY(185);

        CustomTextField tfConnect = new CustomTextField();
        tfConnect.setLayoutX(320);
        tfConnect.setLayoutY(275);

        multiplayerSubcscene.getPane().getChildren().add(tfServ);
        multiplayerSubcscene.getPane().getChildren().add(nameLabel);
        multiplayerSubcscene.getPane().getChildren().add(infoLabel);
        multiplayerSubcscene.getPane().getChildren().add(tfname);
        multiplayerSubcscene.getPane().getChildren().add(tfConnect);
        multiplayerSubcscene.getPane().getChildren().add(createButtonToStartServ(tfname, tfServ));
        multiplayerSubcscene.getPane().getChildren().add(createButtonToStartConnect(tfname, tfConnect));
    }

    private CustomButton createButtonToStartConnect(CustomTextField tfname, CustomTextField tfConnect){
        CustomButton startButton = new CustomButton("Connect");
        startButton.setLayoutX(30);
        startButton.setLayoutY(270);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if(tfname.getText() != null && !tfname.getText().isEmpty()){
                        if(tfConnect.getText() != null && !tfConnect.getText().isEmpty()) {
                            startTheGame(tfname.getText(), false, tfConnect.getText());
                            clip.stop();
                        } else {
                            tfConnect.setPromptText("IP address!");
                        }
                    } else {
                        tfname.setPromptText("Your name!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return startButton;
    }

    private CustomButton createButtonToStartServ(CustomTextField tfname, CustomTextField tfServ){
        CustomButton startButton = new CustomButton("Start server");
        startButton.setLayoutX(30);
        startButton.setLayoutY(180);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if(tfname.getText() != null && !tfname.getText().isEmpty()){
                        if(tfServ.getText() != null && !tfServ.getText().isEmpty()) {
                            startTheGame(tfname.getText(), true, tfServ.getText());
                            clip.stop();
                        } else {
                            tfServ.setPromptText("IP address!");
                        }
                    } else {
                        tfname.setPromptText("Your name!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return startButton;
    }

    private void createPlayStartSubScene() {
        startSubccsene = new MenuSubScene();
        mainPane.getChildren().add(startSubccsene);

        Label nameLabel = new Label("Enter your name: ");
        nameLabel.setFont(FONT);
        nameLabel.setPadding(new Insets(3));
        nameLabel.setLayoutX(150);
        nameLabel.setLayoutY(25);

        Label helpLabel = new Label("Use WASD to move\nSPACE to shoot\nESC for menu");
        helpLabel.setFont(FONT);
        helpLabel.setPadding(new Insets(3));
        helpLabel.setLayoutX(150);
        helpLabel.setLayoutY(160);

        CustomTextField tf = new CustomTextField();
        tf.setLayoutX(180);
        tf.setLayoutY(80);

        startSubccsene.getPane().getChildren().add(tf);
        startSubccsene.getPane().getChildren().add(nameLabel);
        startSubccsene.getPane().getChildren().add(createButtonToStart(tf));
        startSubccsene.getPane().getChildren().add(helpLabel);
    }



    private CustomButton createButtonToStart(CustomTextField tf){
        CustomButton startButton = new CustomButton("Start");
        startButton.setLayoutX(180);
        startButton.setLayoutY(300);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if(tf.getText() != null && !tf.getText().isEmpty()){
                        startTheGame(tf.getText(), false, "localhost");
                        clip.stop();
                    } else {
                        tf.setPromptText("Your name!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return startButton;
    }

    private void createBackground() throws MalformedURLException {
//        Image backgroundImage = new Image(f.toURI().toString(), 1280, 720, false, true);
        Image backgroundImage = new Image("/GUI/bg.png");

        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        BackgroundFill myBF = new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));// or null for the padding
        mainPane.setBackground(new Background(background));
//        mainPane.getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("/Resources/GUI/scenePixe"))));
        System.out.println(background.getImage().getWidth());
    }

    private void createLogo(){
        Image logoim = new Image("/GUI/gameLogog.png", 366, 282, false, true);
        ImageView logo = new ImageView(logoim);
        logo.setLayoutX(700);
        logo.setLayoutY(100);

        logo.addEventHandler(MouseEvent.MOUSE_ENTERED ,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DropShadow drop = new DropShadow(30, Color.GREEN);
                drop.setInput(new Glow(0.2));
                logo.setEffect(drop);
            }
        });

        logo.addEventHandler(MouseEvent.MOUSE_EXITED ,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                logo.setEffect(null);
            }
        });

        mainPane.getChildren().add(logo);
    }

    public Stage getMainStage() {
        return this.mainStage;
    }

    private void startTheGame(String username, boolean server, String IP) throws IOException {
//        mainStage.hide();
        mainStage.hide();
        gm.startGame(gameStage, username, server, IP);
    }
}
