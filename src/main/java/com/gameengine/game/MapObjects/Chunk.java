package com.gameengine.game.MapObjects;

import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Chunk {
    private int width;
    private int height;
    private int pixel_foreground[];
    private ImageTile grasstileSprite;
    private int imWidth;
    private List<GameObject> objects;


    public Chunk(int width, int height, int[] tiles) {
//        System.out.println(Arrays.toString(tiles));
        this.width = width;
        this.height = height;
        this.pixel_foreground = new int[width*height];

        {
            try {
                grasstileSprite = new ImageTile("src/Resources/Tile/grass.png", 32, 32,2);
                imWidth = grasstileSprite.getWidth();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(grasstileSprite.getTileH());
        int n = 0;
        for(int y =0; y<height;y+= grasstileSprite.getTileH()) {
            for (int x = 0; x < width; x+= grasstileSprite.getTileW()) {
                if(tiles[n] == 1){
                    for(int a = 0; a < grasstileSprite.getTileH(); a++){
                        for(int b = 0; b < grasstileSprite.getTileW(); b++){
                            pixel_foreground[(b+x)+(a+y)*width] = grasstileSprite.getPixels()[b+a*imWidth];
//                                    System.out.println(grasstileSprite.getPixels()[b+a*32]);
                        }
                    }
                }
                else if(tiles[n] == 2){
                    for(int a = 0; a < grasstileSprite.getTileH(); a++){
                        for(int b = 0; b < grasstileSprite.getTileW(); b++){
                            pixel_foreground[(b+x)+(a+y)*width] = grasstileSprite.getPixels()[(b+grasstileSprite.getTileW())+a*imWidth];
//                                    System.out.println(grasstileSprite.getPixels()[b+a*32]);
                        }
                    }
                }
//
                n++;
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
