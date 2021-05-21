package com.gameengine.engine.gfx;
/*
 * The ImageTile class extends Image for set up and processing of sprites in a game
 * for further rendering
 * @author Lebedev Daniil
 */
import java.io.IOException;

public class ImageTile extends Image{
    // Sprite properties
    private int tileW, tileH;
    private float scale;
    private int[] pixelsRot;

    /*
     * Constructs an image tile object.
     * This public constructor is used for set height and weight image tile for further rendering
     * @param path - the current path of the image file
     * @param tilwW- the width of the image tile
     * @param tilwH- the height of the image tile
     * */
    public ImageTile(String path, int tileW, int tileH, float scale) throws IOException {
        super(path, scale);
        this.tileW = tileW*(int)scale;
        this.tileH = tileH*(int)scale;
        this.scale = scale;
    }

    public ImageTile(String path, int tileW, int tileH) throws IOException {
        super(path);
        this.tileW = tileW;
        this.tileH = tileH;
        this.scale = scale;
    }

    /*
    * Function is used for rotate image and make a animation effect
    * @param angle - for calculating angles and radians
    * @param pixels - for store rotated pixels
    * @param width - the current width of image tile
    * @param height - the current height fo image tile
    * @return the integer array of rotated pixels
    * */
    public static int[ ] rotate( double angle, int[ ] pixels, int width, int height ) {
        final double radians = Math.toRadians(angle);
        final double cos = Math.cos(radians);
        final double sin = Math.sin(radians);
        final int[] pixels2 = new int[pixels.length];
        for (int pixel = 0; pixel < pixels2.length; pixel++) {
            pixels2[pixel] = 0xFFFFFF; // make all pixels white
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int centerx = width / 2;
                final int centery = height / 2;
                final int m = x - centerx;
                final int n = y - centery;
                final int j = ((int) (m * cos + n * sin)) + centerx;
                final int k = ((int) (n * cos - m * sin)) + centery;
                if (j >= 0 && j < width && k >= 0 && k < height) {
                    pixels2[(y * width + x)] = pixels[(k * width + j)];
                }
            }
        }
        return pixels2;
    }

    /*
     * Function to return a Image object from ImageTile
     * Parametrs:
     * tileX - the current positon on X-asis
     * tileY - the current positon on Y-asis
     * @returns the Image object for further rendering
     * */
    public Image getTileImage(int tileX, int tileY){
        int[] p = new int[tileW * tileH];
        for(int y = 0; y < tileH; y++){
            for(int x = 0; x < tileW; x++){
                p[x + y * tileW] = this.getPixels()[(x+tileX * tileW) + (y+tileY * tileH) * this.getWidth()];
            }
        }
        return new Image(p, tileW, tileH);
    }

    /*
     * Obtains the current width of image tile
     * @returns the width of image tile
     * */
    public int getTileW() {
        return tileW;
    }

    /*
     * Obtains the pixel for rotation image tile
     * @returns the integer array of rotatted pixels
     * */
    public int[] getPixelsRot(){
        return pixelsRot;
    }

    /*
     * Set a image tile width
     * */
    public void setTileW(int tileW) {
        this.tileW = tileW;
    }

    /*
     * Obtains the current height of image tile
     * @returns the height of image tile
     * */
    public int getTileH() {
        return tileH;
    }

    /*
     * Set a image tile height
     * */
    public void setTileH(int tileH) {
        this.tileH = tileH;
    }
}
