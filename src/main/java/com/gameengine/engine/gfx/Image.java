package com.gameengine.engine.gfx;
/*
 * The Image class for set up and processing of any image in a game
 * @author Lebedev Daniil
 */

import javafx.scene.image.PixelReader;

import java.io.File;
import java.io.IOException;

public class Image {
    // Image properties
    private int width, height;
    private int[] pixels;
    private int[] pixelsFlip;

    private boolean alpha = false;
    /*
     * Block with making a light in image start
     * */
//    private int lightBlock = Light.NONE;

//    public int getLightBlock() {
//        return lightBlock;
//    }
//
//    public void setLightBlock(int lightBlock) {
//        this.lightBlock = lightBlock;
//    }
    /*
     * Block with making a light in image end
     * */

    /*
     * Indicates whether the alpha rendering is running.
     * @return alpha Returns boolean value
     * */
    public boolean isAlpha() {
        return alpha;
    }

    /*
    * The stadnard public constructor
    * @param pixels - the array of pixels
    * @param width - the width of the image file
    * @param height - the height of the image file*/
    public Image(int[] p, int w, int h){
        this.pixels = p;
        this.width = w;
        this.height = h;
    }

    /*
     * Constructs an image file object.
     * This public constructor is used for get from image array of pixels for further rendering
     * @param path - the current path of the image file
     * */
    public Image(String path) throws IOException {
        javafx.scene.image.Image image = new javafx.scene.image.Image(this.getClass().getResource(path).toExternalForm());

        width = (int)image.getWidth();
        height = (int)image.getHeight();
        pixels = new int[width * height];
        pixelsFlip = new int[width * height];

//        System.out.println(width +" " + height);
        PixelReader pr = image.getPixelReader();
        double radian = Math.toRadians(30);
//        System.out.println("Pixel format: " + pr.getPixelFormat());
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                pixels[x + y * width] = pr.getArgb(x, y);
                if(pixels[x + y * width] == 0xffff3300) {
                    pixels[x + y * width] = 0xff00ff00;
                }
            }
        }
        for(int i =0 ; i < pixelsFlip.length; i++){
            pixelsFlip[i] = pixels[i - 2 * (i % width) + width - 1];
        }
//        pixels = resizePixels(pixels, width, height, width*2, height*2);
    }

    public Image(String path, float scale) throws IOException {
        javafx.scene.image.Image image = new javafx.scene.image.Image(this.getClass().getResource(path).toExternalForm());

        width = (int)image.getWidth()*(int)scale;
        height = (int)image.getHeight()*(int)scale;
        pixels = new int[width*height];
        int[] spixels = new int[width * height];
        pixelsFlip = new int[width * height];

//        System.out.println(width +" " + height);
        PixelReader pr = image.getPixelReader();
        double radian = Math.toRadians(30);
//        System.out.println("Pixel format: " + pr.getPixelFormat());
        for(int x = 0; x < width/(int)scale; x++){
            for(int y = 0; y < height/(int)scale; y++){
                spixels[x + y * width] = pr.getArgb(x, y);
            }
        }
        for(int i =0 ; i < pixelsFlip.length; i++){
            pixelsFlip[i] = pixels[i - 2 * (i % width) + width - 1];
        }
        resizePixels(spixels, pixels, width, height, scale);
    }

    public void resizePixels(int[] spixels, int[] pixels, int w1,int h1, float scale) {
        int trg = 0;
        for(int i = 0; i < h1; i++) {
            float iUnscaled = i / scale;
            for(int j = 0; j < w1; j++){
                float jUnscaled = j / scale;
                pixels[trg++] = spixels[(int)iUnscaled*w1 + (int)jUnscaled];
            }
        }
    }

    /*
    * The standard public constructor
    * @param path - the current path of the image file
    * @param i -
    * @param i1 -
    */
    public Image(String path, int i, int i1) {
    }

    /*
     * Obtains a current width of the image
     * @returns the width of the image
     * */
    public int getWidth() {
        return width;
    }

    /*
     * Set a width of image
     * */
    public void setWidth(int width) {
        this.width = width;
    }

    /*
     * Obtains a current height of the image
     * @returns the height of the image
     * */
    public int getHeight() {
        return height;
    }

    /*
     * Set a height of image
     * */
    public void setHeight(int height) {
        this.height = height;
    }

    /*
     * Obtains the array of pixels
     * @returns the integer array of pixels
     * */
    public int[] getPixels() {
        return pixels;
    }

    /*
     * Obtains the array of pixels to flip
     * @returns the integer array of flipped pixels
     * */
    public int[] getPixelsFlip(){
        return pixelsFlip;
    }

    /*
     * Set pixels of image
     * */
    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    /*
     * Set a alpha rendering
     * */
    public void setAlpha(boolean alpha) {
        this.alpha = alpha;
    }
}
