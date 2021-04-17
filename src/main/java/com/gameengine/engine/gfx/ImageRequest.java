package com.gameengine.engine.gfx;
/*
 * Access to the image
 * @author Lebedev Daniil*/
public class ImageRequest {
    public Image image;
    public int zDepth;
    public int offX, offY;

    /*
     * Constructs an image request.
     * This public constructor is used for store a info of image location for rendering.
     * */
    public ImageRequest(Image image, int zDepth, int offX, int offY){
        this.image = image;
        this.zDepth = zDepth;
        this.offX = offX;
        this.offY = offY;
    }
}