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
     * Parametrs:
     * path - the current path of the image file
     * tilwW- the width of the image tile
     * tilwH- the height of the image tile
     * */
    public ImageTile(String path, int tileW, int tileH, float scale) throws IOException {
        super(path, scale);
        this.tileW = tileW*(int)scale;
        this.tileH = tileH*(int)scale;
        this.scale = scale;
//        for (int y = tileH; y < this.tileH; y++){
//            for(int x = tileW; x < this.tileW; x++){
//                scaledArray[x+y*this.tileW] = pix[a+b*tileH];
//                a++;
//            }
//            b++;
//        }
//        for (int i = 0; i < tileW/(int)scale; ++i) {
//            for (int j = 0; j < tileH/(int)scale; ++j) {
//                scaledArray[(i*2)+(j*2)*tileW] = pix[i+j*tileW/(int)scale];
//                scaledArray[((i*2) + 1)+(j*2)*tileW] = pix[i+j*tileW/(int)scale];
//                scaledArray[(i*2)+((j*2) + 1)*tileW] = pix[i+j*tileW/(int)scale];
//                scaledArray[((i*2) + 1)+((j*2) + 1)*tileW] = pix[i+j*tileW/(int)scale];
//            }
//        }
//        this.setPixels(resizePixels(super.getPixels(), getWidth(), getHeight(), getWidth()*(int)scale, getHeight()*(int)scale));
//        this.setPixels(scaledArray);
//        for(int x = 0; x < tileW; x++) {
//            for (int y = 0; y < this.getHeight(); y++) {
//                double centerX = tileW / 2;
//                double centerY = this.getHeight() / 2;
//
//                double dx = (double) (x - centerX);
//                double dy = (double) (y - centerY);
//
//                double finX = Math.cos(Math.toRadians(90)) * dx - Math.sin(Math.toRadians(90)) * dy + centerX;
//                double finY = Math.cos(Math.toRadians(90)) * dy + Math.sin(Math.toRadians(90)) * dx + centerY;
//
//                finX = Math.round(finX);
//                finY = Math.round(finY);
//
////                System.out.println(" " + (Math.sin(Math.toRadians(30)) * dx + centerY));
//                pixelsRot[x + y * this.getWidth()] = this.getPixels()[(int) (finX + finY * tileW)];
//            }
//        }
    }

    public ImageTile(String path, int tileW, int tileH) throws IOException {
        super(path);
        this.tileW = tileW;
        this.tileH = tileH;
        this.scale = scale;
        int[] pix = this.getPixels();
        int[] scaledArray = new int[tileW*tileH];
//        for (int i = 0; i < tileW/(int)scale; ++i) {
//            for (int j = 0; j < tileH/(int)scale; ++j) {
//                scaledArray[(i*2)+(j*2)*tileW] = pix[i+j*tileW/(int)scale];
//                scaledArray[((i*2) + 1)+(j*2)*tileW] = pix[i+j*tileW/(int)scale];
//                scaledArray[(i*2)+((j*2) + 1)*tileW] = pix[i+j*tileW/(int)scale];
//                scaledArray[((i*2) + 1)+((j*2) + 1)*tileW] = pix[i+j*tileW/(int)scale];
//            }
//        }
//        this.setPixels(resizePixels(super.getPixels(), getWidth(), getHeight(), getWidth()*(int)scale, getHeight()*(int)scale));
//        this.setPixels(scaledArray);
//        for(int x = 0; x < tileW; x++) {
//            for (int y = 0; y < this.getHeight(); y++) {
//                double centerX = tileW / 2;
//                double centerY = this.getHeight() / 2;
//
//                double dx = (double) (x - centerX);
//                double dy = (double) (y - centerY);
//
//                double finX = Math.cos(Math.toRadians(90)) * dx - Math.sin(Math.toRadians(90)) * dy + centerX;
//                double finY = Math.cos(Math.toRadians(90)) * dy + Math.sin(Math.toRadians(90)) * dx + centerY;
//
//                finX = Math.round(finX);
//                finY = Math.round(finY);
//
////                System.out.println(" " + (Math.sin(Math.toRadians(30)) * dx + centerY));
//                pixelsRot[x + y * this.getWidth()] = this.getPixels()[(int) (finX + finY * tileW)];
//            }
//        }
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
