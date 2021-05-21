package com.gameengine.engine;

import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

import javafx.scene.image.*;

import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

import java.nio.IntBuffer;

/**
 * Main class that creates the displayed window.
 * Draws the pixel array onto the window.
 * @author Vasily Levitskiy
 */
public class Window {
    /*
    Properties of window starts
     */
    private WritableImage image;
    private PixelWriter pw;

    private GraphicsContext gctx;
    private PixelFormat intArgbPreInstance = PixelFormat.getIntArgbPreInstance();
    private Canvas gameCanvas;
    private Scene mainScene;
    private Group gameGroup;

    private int[] pixel;
    private int[] finalPixel;

    private int width;
    private int height;

    private IntBuffer intBuffer;
    private PixelBuffer<IntBuffer> pixelBuffer;
     /*
    Properties of window ends
     */

    /**
     * A public constructor for creating window object.
     * @param gc - the game container needed for getting width and height of window.
     * @param canvas - the canvas that will be displayed in the window
     */
    public Window(GameContainer gc, Canvas canvas){
        width = gc.getWidth();
        height = gc.getHeight();

        pixel = new int[(int)(width*height*gc.getScale()*gc.getScale())];
        finalPixel = new int[(int)(width*gc.getScale()*height*gc.getScale())];
        gameCanvas = new Canvas(width*gc.getScale(), height*gc.getScale());
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gameCanvas);
        mainScene = new Scene(borderPane);

        gctx = gameCanvas.getGraphicsContext2D();

        pw = gctx.getPixelWriter();
    }
    public Window(GameContainer gc) {
        width = gc.getWidth();
        height = gc.getHeight();
        pixel = new int[(int)(width*height*gc.getScale()*gc.getScale())];
        gameCanvas = new Canvas(width*gc.getScale(), height*gc.getScale());
    }

    /**
     * THe function that updates the contents of the window.
     */
    public void update(){
        pw.setPixels(0,0,width,height,intArgbPreInstance,pixel,0,(int)gameCanvas.getWidth());
    }

    /**
     * A getter function that return a array of pixels
     * @return integer array of pixels
     */
    public int[] getPixel() {
        return pixel;
    }

    /**
     * A getter function that return the main scene
     * @return Scene object
     */
    public Scene getMainScene(){
        return mainScene;
    }

    /**
     * A getter function that return the game canvas
     * @return Canvas object
     */
    public Canvas getCanvas() {
        return gameCanvas;
    }
}
