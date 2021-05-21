package com.gameengine.engine.gfx;
/*
 * Access to the image
 * @author Lebedev Daniil*/
public class ImageRequest {
    private Image image;
    private int zDepth;
    private int offX, offY;

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public int getOffX() {
        return offX;
    }

    public void setOffX(int offX) {
        this.offX = offX;
    }

    public int getOffY() {
        return offY;
    }

    public void setOffY(int offY) {
        this.offY = offY;
    }
}