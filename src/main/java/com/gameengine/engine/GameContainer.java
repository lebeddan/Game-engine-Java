package com.gameengine.engine;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;

/*
 * Contains the main game loop of the game.
 * Controls when to call the main functions of the game.
 * Needs to have a GameManager on creation.
 * @author Vasily Levitskiy
 */
public class GameContainer implements Runnable{

    // Main components of the game loop
    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;
    private AbstractGame game;

    // Window properties
    private final double NINE_BIL = 1000000000.0;
    private int width = 320, height = 240;
    private float scale = 1f;
    private String title = "TestEngine";

    // javaFX components
    private Canvas gameCanvas;

    public GameContainer(AbstractGame game){
        this.game = game;
    }

    /*
     * Main function that starts the game
     * Creates the main components that will be used in game
     */
    public void start(){
        window = new Window(this, gameCanvas);
        renderer = new Renderer(this, gameCanvas);
        input = new Input(this);

        // Run game on another thread.
        thread = new Thread(this);
        thread.run();
    }

    /*
    * Stops the game loop
    * TODO: Add game state saving.
     */
    public void stop(){

    }

    /**
     * The main game loop that calls the main function
     * every X seconds.
     */
    public void run(){


        // Initialize the game(make ready all elements)
        game.init(this);
        GameContainer gc = this;
        final long startNanoTime = System.nanoTime();
        final long[] lastNanoTime = {System.nanoTime()};


        final long[] frameTimes = new long[100];
        final int[] frameTimeIndex = {0};
        final boolean[] arrayFilled = {false};

        // The main game loop timer. Calls the function Tick() every X seconds.
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double frameRate = 0;
                long elapsedNanosPerFrame = 0;
                long oldFrameTime = frameTimes[frameTimeIndex[0]] ;
                frameTimes[frameTimeIndex[0]] = currentNanoTime ;
                frameTimeIndex[0] = (frameTimeIndex[0] + 1) % frameTimes.length ;
                if (frameTimeIndex[0] == 0) {
                    arrayFilled[0] = true ;
                }
                if (arrayFilled[0]) {
                    long elapsedNanos = currentNanoTime - oldFrameTime ;
                    elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                    frameRate = NINE_BIL / elapsedNanosPerFrame ;
//                    System.out.println("Framerate: " + frameRate);
                }
                if(frameRate > 70){
                    elapsedNanosPerFrame = (long) (NINE_BIL/70);
                }
                    game.update(gc, (float)(elapsedNanosPerFrame/NINE_BIL));
                    input.update();
                    //Draw updated version onto screen.
                    renderer.clear();
                    game.render(gc, renderer);
                    renderer.setCamX(0);
                    renderer.setCamY(0);
                    renderer.drawText("FPS IS: " + frameRate, 0, 0, 0xffff0000);
                    window.update();
            }
        }.start();

    }

    /**
     * Function to get the main Renderer
     * @return renderer The main renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return window The main window containing the game
     */
    public Window getWindow() {
        return window;
    }

    /**
     * @return input Returns the input class
     */
    public Input getInput() {
        return input;
    }
}
