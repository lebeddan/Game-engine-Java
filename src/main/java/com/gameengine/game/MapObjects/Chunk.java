package com.gameengine.game.MapObjects;

import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameObjects.GameObject;
import com.gameengine.game.GameObjects.world.Tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Chunk {
    private int width;
    private int height;
    private int pixel_foreground[];
    private ImageTile grasstileSprite;
    private int imWidth;
    private List<GameObject> objects;
    private int chunk_num;
    private int levelWidth;
    private int tileSize;

    private int posX, posY;


    public Chunk(int width, int height, int[] tiles, int[] objects_l, int chunk_num, Map map, ImageTile grasstileSprite) {
//        System.out.println(Arrays.toString(tiles));
        this.width = width;
        this.height = height;
        this.pixel_foreground = new int[width*height];
        this.objects = new ArrayList<GameObject>();
        this.chunk_num = chunk_num;
        this.levelWidth = map.getLevel_width();
        this.tileSize = map.getTile_size();
        this.posX = (chunk_num%map.getChunk_width())*(map.getTile_size()*map.get_cWidth());
        this.posY = (chunk_num/map.getChunk_width())*(map.getTile_size()*map.get_cHeight());
//        System.out.println(posX + " " +posY);
//        System.out.println(Arrays.toString(objects_l));
        imWidth = grasstileSprite.getWidth();
        int n = 0;
        if(tiles != null) {
            for (int y = 0; y < height; y += grasstileSprite.getTileH()) {
                for (int x = 0; x < width; x += grasstileSprite.getTileW()) {
                    if (tiles[n] == 1) {
                        for (int a = 0; a < grasstileSprite.getTileH(); a++) {
                            for (int b = 0; b < grasstileSprite.getTileW(); b++) {
//                            System.out.println((b+x)+(a+y)*width);
                                pixel_foreground[(b + x) + (a + y) * width] = grasstileSprite.getPixels()[b + a * imWidth];
                            }
                        }
                    } else if (tiles[n] == 2) {
                        for (int a = 0; a < grasstileSprite.getTileH(); a++) {
                            for (int b = 0; b < grasstileSprite.getTileW(); b++) {
                                pixel_foreground[(b + x) + (a + y) * width] = grasstileSprite.getPixels()[(b + grasstileSprite.getTileW()) + a * imWidth];
//                                    System.out.println(grasstileSprite.getPixels()[b+a*32]);
                            }
                        }
                    }

                    if (objects_l[n] == 3) {
                        try {
                            objects.add(new Tree(Integer.toString(n), x + posX, y + posY));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    n++;
                }
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

    public int getNumber(){
        return chunk_num;
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
