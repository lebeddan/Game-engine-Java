package com.gameengine.engine;


import com.gameengine.engine.gfx.*;
import com.gameengine.engine.gfx.Font;
import com.gameengine.engine.gfx.Image;
import com.gameengine.game.MapObjects.Chunk;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class that does the pixel logic. Tells where the pixels should be.
 * Takes the pixel screen array from Window {@link Window}.
 * Different functions that manipulate the array.
 * Inspired by MajoolWhip.
 * @author Vasily Levitskiy
 */

public class Renderer {
    private Font font = Font.STANDARD;
    private ArrayList<ImageRequest> imageRequest = new ArrayList<ImageRequest>();
    private ArrayList<LightRequest> lightRequest = new ArrayList<LightRequest>();

    private  int pixelW, pixelH;
    private int pixel[];
    private int[] zBuffer;
    private int[] lightMap;
    private int[] lightBlock;

    private int ambientColor = 0xff232323;
    private int zDepth = 0;
    private boolean processing  = false;
    private int camX, camY;

    public Renderer(GameContainer gc, Canvas canvas){
        pixelW = (int)(gc.getWidth()*gc.getScale());
        pixelH = (int)(gc.getHeight()*gc.getScale());
        pixel = new int[pixelW*pixelH];
        pixel = gc.getWindow().getPixel();

        zBuffer = new int[pixel.length];
        lightMap = new int[pixel.length];
        lightBlock = new int[pixel.length];
    }

    /**
     * Deletes everything from screen/
     */
    public void clear(){
        for(int i = 0; i < pixel.length; i++){
            pixel[i] = 0;
            zBuffer[i] = 0;
            lightMap[i] = ambientColor;
            lightBlock[i] = 0;
        }
    }

    public void process(){
        processing = true;

        Collections.sort(imageRequest, new Comparator<ImageRequest>(){

            @Override
            public int compare(ImageRequest o1, ImageRequest o2) {
                if(o1.zDepth < o2.zDepth)
                    return -1;
                if(o1.zDepth> o2.zDepth)
                    return 1;
                return 0;
            }
        });

        for(int i = 0; i < imageRequest.size(); i++){
            ImageRequest ir = imageRequest.get(i);
            setzDepth(ir.zDepth);
//            ir.image.setAlpha(false);
            drawImage(ir.image, ir.offX, ir.offY);
        }

//        for(int i =0; i < lightRequest.size(); i++){
//            LightRequest l = lightRequest.get(i);
//            this.drawLightRequest(l.light, l.locX, l.locY);
//        }

        for(int i = 0; i < pixel.length; i++){
            float red = ((lightMap[i] >> 16) & 0xff) / 255f;
            float green = ((lightMap[i] >> 8) & 0xff) / 255f;
            float blue = ((lightMap[i]) & 0xff) / 255f;

            pixel[i] = ((int)(((pixel[i] >> 16) & 0xff) * red) << 16 | (int)(((pixel[i] >> 8) & 0xff) * green) << 8 | (int)(((pixel[i]) & 0xff) * blue));
        }

        imageRequest.clear();
        lightRequest.clear();
        processing = false;
    }

    /**
     * Sets the pixel specified at X and Y to the color specified by value.
     * @param x X coordinate of pixel
     * @param y Y coordinate of pixel
     * @param value hex value of the color
     */
    public void setPixel(int x, int y, int value){
        int alpha = (((value >> 24) & 0xff));

        if ((x < 0 || x >= pixelW || y < 0 || y >= pixelH) ||  alpha == 0){
            return;
        }

        int index = x + y * pixelW;

        if(zBuffer[index] > zDepth){
            return;
        }

        zBuffer[index] = zDepth;

        if(alpha == 255) {
            pixel[index] = value;
        } else {
            int pixelColor = pixel[index];

            int newRed = ((pixelColor >> 16) & 0xff) - (int) ((((pixelColor >> 16) & 0xff) - ((value >> 16) & 0xff)) * (alpha / 255f));
            int newGreen = ((pixelColor >> 8) & 0xff) -(int) ((((pixelColor >> 8) & 0xff) - ((value >> 8) & 0xff)) * (alpha / 255f));
            int newBlue = (pixelColor) & 0xff - (int)(((pixelColor & 0xff) - (value & 0xff)) * (alpha / 255f));

            pixel[index] = (255 << 24 | newRed << 16 | newGreen << 8 | newBlue);
        }
    }

    /**
     * Draws text onto the screen. Uses default font specified in Font {@link Font}
     * @param text The string to be drawn
     * @param offX X location of the text
     * @param offY Y location of the text
     * @param color Hex value of the text color
     */
    public void drawText(String text, int offX, int offY, int color){
        offX -= camX;
        offY -= camY;

        int offset = 0;

        for(int i = 0; i < text.length(); i++){
            int unicode = text.codePointAt(i);

            for(int y = 0; y < font.getFontImage().getHeight(); y++){
                for(int x = 0; x < font.getWidths()[unicode]; x++){
                    if(font.getFontImage().getPixels()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getWidth()] == 0xffffffff){
                        setPixel(x + offX + offset, y + offY, color);
                    }
                }
            }

            offset += font.getWidths()[unicode];
        }
    }

    public int getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(int ambientColor) {
        this.ambientColor = ambientColor;
    }

    /**
     * Draws an image
     * @param image The image to draw
     * @param offX X location of upper left corner of image
     * @param offY Y location of upper left corner of image
     */
    public void drawImage(Image image, int offX, int offY){
        offX -= camX;
        offY -= camY;

        if(image.isAlpha() && !processing){
            imageRequest.add(new ImageRequest(image, zDepth, offX, offY));
            return;
        }

        //Don't render code
        if(offX < -image.getWidth()) return;
        if(offY < -image.getHeight()) return;
        if(offX >= pixelW) return;
        if(offY >= pixelH) return;

        int newX = 0;
        int newY = 0;
        int newWidth = image.getWidth();
        int newHeight = image.getHeight();

        // Clipping code
        if (offX < 0){ newX -= newX; }
        if (offY < 0){ newY -= newY; }
        if(newWidth + offX > pixelW){ newWidth -= newWidth + offX - pixelW;}
        if(newHeight + offY > pixelH){ newHeight -= newHeight + offY - pixelH;}

        for(int y = newY; y < newHeight; y++){
            for(int x = newX; x < newWidth; x++){
                setPixel(x + offX, y + offY, image.getPixels()[x + y * image.getWidth()]);
//                setLightBlock(x + offX, y + offY, image.getLightBlock());
            }
        }
    }

    /**
     * Draws a chunk
     * @param chunk The chunk to draw
     * @param offX X location of upper left corner of image
     * @param offY Y location of upper left corner of image
     */
    public void drawChunk(Chunk chunk, int offX, int offY){
        offX -= camX;
        offY -= camY;

//        if(image.isAlpha() && !processing){
//            imageRequest.add(new ImageRequest(image, zDepth, offX, offY));
//            return;
//        }

        //Don't render code
        if(offX < -chunk.getWidth()) return;
        if(offY < -chunk.getHeight()) return;
        if(offX >= pixelW) return;
        if(offY >= pixelH) return;

        int newX = 0;
        int newY = 0;
        int newWidth = chunk.getWidth();
        int newHeight = chunk.getHeight();

        // Clipping code
        if (offX < 0){ newX -= newX; }
        if (offY < 0){ newY -= newY; }
        if(newWidth + offX > pixelW){ newWidth -= newWidth + offX - pixelW;}
        if(newHeight + offY > pixelH){ newHeight -= newHeight + offY - pixelH;}

        for(int y = newY; y < newHeight; y++){
            for(int x = newX; x < newWidth; x++){
                setPixel(x + offX, y + offY, chunk.getPixels()[x + y * chunk.getWidth()]);
//                setLightBlock(x + offX, y + offY, image.getLightBlock());
            }
        }
    }

    /**
     * Draws a tile from a bigger sprite sheet
     * Supports animation and rotation
     * @param image The image to draw
     * @param offX X location of upper left corner
     * @param offY Y location of the upper left corner
     * @param tileX X coordinate of the tile
     * @param tileY Y coordinate of the tile
     * @param angle The angle of image to rate(in degrees)
     * @param axis The axis around which to rotate
     */
    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY, float angle, Point2D axis){
        offX -= camX;
        offY -= camY;

        if(image.isAlpha() && !processing){
            imageRequest.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
            return;
        }
        setPixel(offX, offY, 0xffff0000);
        setPixel(offX+1, offY+1, 0xffff0000);
        setPixel(offX+1, offY, 0xffff0000);
        setPixel(offX, offY+1, 0xffff0000);

        //Don't render code
        if(offX < -image.getTileW()) return;
        if(offY < -image.getTileH()) return;
        if(offX >= pixelW) return;
        if(offY >= pixelH) return;

        final double radians = Math.toRadians(angle);
        final double cos = Math.cos(radians);
        final double sin = Math.sin(radians);
        final int centerx = (int) axis.getX();
        final int centery = (int) axis.getY();

        int newX = -image.getTileH();
        int newY = -image.getTileH();
        int newWidth = image.getTileW() + centery;
        int newHeight = image.getTileH() + centery;

        // Clipping code
        if (offX < 0){ newX -= newX; }
        if (offY < 0){ newY -= newY; }
        if(newWidth + offX > pixelW){ newWidth -= newWidth + offX - pixelW;}
        if(newHeight + offY > pixelH){ newHeight -= newHeight + offY - pixelH;}

        for(int y = newY; y < newHeight; y++){
            for(int x = newX; x < newWidth; x++){
                final int m = x - centerx - centerx/2;
                final int n = y - centery - centery/2;
                final int j = ((int) (m * cos + n * sin)) + centerx;
                final int k = ((int) (n * cos - m * sin)) + centery;
                if (j >= 0 && j < image.getTileW() && k >= 0 && k < image.getTileH()) {
                    setPixel(x + offX, y + offY, image.getPixels()[(k+ tileY * image.getTileH()) * image.getWidth() + (j+tileX * image.getTileW())]);
                }

            }
        }
    }

    /**
     * Draws a tile from a bigger sprite sheet
     * Supports animation
     * @param image The image to draw
     * @param offX X location of upper left corner
     * @param offY Y location of the upper left corner
     * @param tileX X coordinate of the tile
     * @param tileY Y coordinate of the tile
     * @param direction The direction of the tile(left right) as int
     */
    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY, int direction){
        offX -= camX;
        offY -= camY;

        if(image.isAlpha() && !processing){
            imageRequest.add(new ImageRequest(image.getTileImage(tileX, tileY), zDepth, offX, offY));
            return;
        }

        //Don't render code
        if(offX < -image.getTileW()) return;
        if(offY < -image.getTileH()) return;
        if(offX >= pixelW) return;
        if(offY >= pixelH) return;

        int newX = 0;
        int newY = 0;
        int newWidth = image.getTileW();
        int newHeight = image.getTileH();

        // Clipping code
        if (offX < 0){ newX -= newX; }
        if (offY < 0){ newY -= newY; }
        if(newWidth + offX > pixelW){ newWidth -= newWidth + offX - pixelW;}
        if(newHeight + offY > pixelH){ newHeight -= newHeight + offY - pixelH;}

        for(int y = newY; y < newHeight; y++){
            for(int x = newX; x < newWidth; x++){
                if(direction == 1) {
                    setPixel(x + offX, y + offY, image.getPixelsFlip()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getWidth()]);
                } else {
                    System.out.println(image.getTileW());
                    setPixel(x + offX, y + offY, image.getPixels()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getWidth()]);
                }
            }
        }
    }

    /**
     * Draw hollow rectangle
     * @param offX X position left upper corner
     * @param offY Y position of left upper corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @param color Color of the rectangle
     */
    public void drawRect(int offX, int offY, int width, int height, int color){
        offX -= camX;
        offY -= camY;

        for(int y = 0; y <= height; y++){
            setPixel(offX, y + offY, color);
            setPixel(offX + width, y + offY, color);
        }

        for(int x = 0; x <= width; x++){
            setPixel(offX + x, offY, color);
            setPixel(x + offX, offY + height, color);
        }
    }

    /**
     * Draws a filled rectangle
     * @param offX X position left upper corner
     * @param offY Y position of left upper corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @param color Color of the rectangle
     */
    public void drawFillRect(int offX, int offY, int width, int height, int color){
        offX -= camX;
        offY -= camY;

//        Don't render code
        if(offX < -width) return;
        if(offY < -height) return;
        if(offX >= pixelW) return;
        if(offY >= pixelH) return;

        int newX = 0;
        int newY = 0;
        int newWidth = width;
        int newHeight = height;

//         Clipping code
        if (offX < 0){ newX -= newX; }
        if (offY < 0){ newY -= newY; }
        if(newWidth + offX > pixelW){ newWidth -= newWidth + offX - pixelW;}
        if(newHeight + offY > pixelH){ newHeight -= newHeight + offY - pixelH;}

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                setPixel(x+offX, y+offY, color);
            }
        }
    }

    /**
     *
     * Code that handles light
     * Currently not needed
     */

//    public void drawLight(Light light, int offX, int offY){
//        lightRequest.add(new LightRequest(light, offX, offY));
//    }
//
//    public void setLightMap(int x, int y, int value){
//        if ((x < 0 || x >= pixelW || y < 0 || y >= pixelH)){
//            return;
//        }
//
//        int baseColor = lightMap[x + y * pixelW];
//
//        int maxR = Math.max(((baseColor >> 16) & 0xff), ((value >> 16) & 0xff));
//        int maxG = Math.max(((baseColor >> 8) & 0xff), ((value >> 8) & 0xff));
//        int maxB = Math.max((baseColor & 0xff), (value & 0xff));
//
//        lightMap[x + y * pixelW] = (maxR << 16 | maxG << 8 | maxB);
//    }
//
//    public void setLightBlock(int x, int y, int value){
//        if ((x < 0 || x >= pixelW || y < 0 || y >= pixelH)){
//            return;
//        }
//
//        if(zBuffer[x + y * pixelW] > zDepth){
//            return;
//        }
//
//        lightBlock[x + y * pixelW] = value;
//    }
//
//    private void drawLightRequest(Light light, int offX, int offY){
//        offX -= camX;
//        offY -= camY;
//
//        for(int i = 0; i <= light.getDiameter(); i++){
//            drawLightLine(light, light.getRadius(), light.getRadius(), i, 0, offX, offY);
//            drawLightLine(light, light.getRadius(), light.getRadius(), i, light.getDiameter(), offX, offY);
//            drawLightLine(light, light.getRadius(), light.getRadius(), 0, i, offX, offY);
//            drawLightLine(light, light.getRadius(), light.getRadius(), light.getDiameter(), i, offX, offY);
//
//        }
//    }
//
//    public void drawLightLine(Light light, int x0, int y0, int x1, int y1, int offX, int offY){
//        int dx = Math.abs(x1 - x0);
//        int dy = Math.abs(y1 - y0);
//
//        int sx = x0 < x1 ? 1 : -1;
//        int sy = y0 < y1 ? 1 : -1;
//
//        int err = dx - dy;
//        int err2;
//
//        while(true){
//            int screenX = x0 - light.getRadius() + offX;
//            int screenY = y0 - light.getRadius() + offY;
//
//            if(screenX < 0 || screenX >= pixelW || screenY < 0 || screenY >= pixelH){
//                return;
//            }
//
//            int lightColor = light.getLightValue(x0, y0);
//            if(lightColor == 0){
//                break;
//            }
//
//            if(lightBlock[screenX + screenY * pixelW] == Light.FULL){
//                return;
//            }
//
//            setLightMap(screenX, screenY,
//                    lightColor);
//
//            if(x0 == x1 && y0 == y1)
//                break;
//
//            err2 = 2 * err;
//            if(err2 > -1 * dy){
//                err -= dy;
//                x0 += sx;
//            }
//
//            if(err2 < dx){
//                err += dx;
//                y0 += sy;
//            }
//
//        }
//    }

    public int getzDepth() {
        return zDepth;
    }

    public void setzDepth(int zDepth) {
        this.zDepth = zDepth;
    }

    public int getCamX() {
        return camX;
    }

    public void setCamX(int camX) {
        this.camX = camX;
    }

    public int getCamY() {
        return camY;
    }

    public void setCamY(int camY) {
        this.camY = camY;
    }
}
