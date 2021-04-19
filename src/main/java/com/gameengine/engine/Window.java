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

    public Window(GameContainer gc, Canvas canvas){
        width = gc.getWidth();
        height = gc.getHeight();

//        intBuffer = IntBuffer.allocate(width * height);
//        pixelBuffer = new PixelBuffer<>(width, height, intBuffer, intArgbPreInstance);
//        WritableImage img = new WritableImage(pixelBuffer);

        pixel = new int[(int)(width*height*gc.getScale()*gc.getScale())];
        finalPixel = new int[(int)(width*gc.getScale()*height*gc.getScale())];
        gameCanvas = new Canvas(width*gc.getScale(), height*gc.getScale());
        BorderPane borderPane = new BorderPane();

//        ImageView imageView = new ImageView();
//        imageView.setPreserveRatio(true);
//        imageView.setImage(img);

        borderPane.setCenter(gameCanvas);

//        borderPane.setCenter(imageView);

//        gameCanvas.setCache(true);
//        gameCanvas.setCacheHint(CacheHint.SPEED);

        mainScene = new Scene(borderPane);

        gctx = gameCanvas.getGraphicsContext2D();

        pw = gctx.getPixelWriter();

//        pw = img.getPixelWriter();
    }

    /**
     * THe function that updates the contents of the window.
     */
    public void update(){
        pw.setPixels(0,0,width,height,intArgbPreInstance,pixel,0,(int)gameCanvas.getWidth());
    }

    public int[] getPixel() {
        return pixel;
    }

    public Scene getMainScene(){
        return mainScene;
    }

    public Canvas getCanvas() {
        return gameCanvas;
    }
}
