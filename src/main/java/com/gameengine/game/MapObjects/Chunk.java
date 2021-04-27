package com.gameengine.game.MapObjects;

import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameObject;
import com.gameengine.game.GameObjects.world.Tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chunk {
    private int width;
    private int height;
    private int pixel_foreground[];
    private ImageTile grasstileSprite;
    private int imWidth;
    private List<GameObject> objects;
    private int chunk_num;

    private int posX, posY;


    public Chunk(int width, int height, int[] tiles, int[] objects_l, int chunk_num, int levelWidth) {
//        System.out.println(Arrays.toString(tiles));
        this.width = width;
        this.height = height;
        this.pixel_foreground = new int[width*height];
        this.objects = new ArrayList<GameObject>();
        this.chunk_num = chunk_num;
        this.posX = (chunk_num%levelWidth)*1280;
        this.posY = (chunk_num/levelWidth)*720;
        try {
            grasstileSprite = new ImageTile("src/Resources/Tile/grass.png", 32, 32,width/(32*10));
            imWidth = grasstileSprite.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int n = 0;
        for(int y =0; y < height; y+= grasstileSprite.getTileH()) {
            for (int x = 0; x < width; x+= grasstileSprite.getTileW()) {
                if(tiles[n] == 1){
                    for(int a = 0; a < grasstileSprite.getTileH(); a++){
                        for(int b = 0; b < grasstileSprite.getTileW(); b++){
//                            System.out.println((b+x)+(a+y)*width);
                            pixel_foreground[(b+x)+(a+y)*width] = grasstileSprite.getPixels()[b+a*imWidth];
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

                if(objects_l[n] == 3){
                    try {
                        objects.add(new Tree(Integer.toString(n),x+(chunk_num%levelWidth)*1280,y+(chunk_num/levelWidth)*720));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                n++;
            }
        }

    }
    public int getPosX(){
        return posX;
    }

    public int getPosY(){
        return posY;
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

    public List<GameObject> getObjects(){
        return objects;
    }
}
