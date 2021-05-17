package com.gameengine.game.MapObjects;

import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameObjects.GameObject;
import com.gameengine.game.GameObjects.world.Tree;
import com.gameengine.game.GameObjects.world.Wall;

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
        System.out.println(grasstileSprite.getTileH());
        imWidth = grasstileSprite.getWidth();
        int imTWidth = grasstileSprite.getWidth()/ grasstileSprite.getTileW();
        int imTHeight = grasstileSprite.getHeight()/ grasstileSprite.getTileH();
        int n = 0;
        if(tiles != null) {
            for (int y = 0; y < height; y += grasstileSprite.getTileH()) {
                for (int x = 0; x < width; x += grasstileSprite.getTileW()) {
                    int number = tiles[n];
                    number--;
                    for (int a = 0; a < grasstileSprite.getTileH(); a++) {
                        for (int b = 0; b < grasstileSprite.getTileW(); b++) {
//                            System.out.println((b+x)+(a+y)*width);
                            pixel_foreground[(b + x) + (a + y) * width] =
                                    grasstileSprite.getPixels()[(b+(grasstileSprite.getTileW()*(number%imTWidth))) +
                                            (a+(grasstileSprite.getTileH()*(number/imTWidth))) * imWidth];
                        }
                    }

                    try {
                        switch (objects_l[n]) {
                            case 3 -> objects.add(new Tree(Integer.toString(n), x + posX, y + posY));
                            case 26 -> objects.add(new Wall(Integer.toString(n), x + posX, y + posY, 26));
                            case 27 -> objects.add(new Wall(Integer.toString(n), x + posX, y + posY, 27));
                            case 29 -> objects.add(new Wall(Integer.toString(n), x + posX, y + posY, 29));
                            case 31 -> objects.add(new Wall(Integer.toString(n), x + posX, y + posY, 31));
                            case 37 -> objects.add(new Wall(Integer.toString(n), x + posX, y + posY, 37));
                            case 39 -> objects.add(new Wall(Integer.toString(n), x + posX, y + posY, 39));

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
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
