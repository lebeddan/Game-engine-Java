package com.gameengine.game.MapObjects;

import com.gameengine.engine.gfx.ImageTile;

import java.io.IOException;
import java.util.Arrays;

public class Tile {
    private int width;
    private int height;
    private int pixel_foreground[];
    private ImageTile grasstileSprite;
    private int imWidth;


    {
        try {
            grasstileSprite = new ImageTile("src/Resources/Tile/grass.png", 32, 32, 2f);
            imWidth = grasstileSprite.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tile(int width, int height, int[] tiles) {
        System.out.println(Arrays.toString(tiles));
        this.width = width;
        this.height = height;
        this.pixel_foreground = new int[width*height];
        int i = 0;
        for(int y =0; y<height;y+= grasstileSprite.getTileH()) {
            for (int x = 0; x < width; x+= grasstileSprite.getTileW()) {
                if(tiles[i] == 1){
                    for(int a = 0; a < grasstileSprite.getTileH(); a++){
                        for(int b = 0; b < grasstileSprite.getTileW(); b++){
                            pixel_foreground[(b+x)+(a+y)*width] = grasstileSprite.getPixels()[b+a*imWidth];
//                                    System.out.println(grasstileSprite.getPixels()[b+a*32]);
                        }
                    }
                }
                else if(tiles[i] == 2){
                    for(int a = 0; a < grasstileSprite.getTileH(); a++){
                        for(int b = 0; b < grasstileSprite.getTileW(); b++){
                            pixel_foreground[(b+x)+(a+y)*width] = grasstileSprite.getPixels()[(b+ grasstileSprite.getTileW())+a*imWidth];
//                                    System.out.println(grasstileSprite.getPixels()[b+a*32]);
                        }
                    }
                }
//
                i++;
            }
        }

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixel_foreground;
    }
}
