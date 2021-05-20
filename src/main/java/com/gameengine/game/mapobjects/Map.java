package com.gameengine.game.mapobjects;

import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.server.GameServer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Map {
    private static final Logger logger = Logger.getLogger(String.valueOf(Map.class));

    private List<Integer> data;
    private int height;
    private int id;
    private String name;
    private int opacity;
    private String type;
    private boolean visible;
    private int width;
    private int x;
    private int y;

    private int chunk_width = 20;
    private int chunk_height = 12;
    private int tile_size = 64;

    private ArrayList<Chunk> chunks;
    private List<Integer> objects;
    private ImageTile tileSet;

    {
        try {
            tileSet = new ImageTile("src/Resources/Tile/myspritesheet.png", 16, 16,tile_size/16);
        } catch (IOException e) {
            logger.finest("IOException catch" + e);
        }
    }

    Type listType = new TypeToken<List<Integer>>() {}.getType();

    public Map(JsonElement list, JsonElement jse){
        Gson gson = new Gson();
//        System.out.println(gson.fromJson(list, Map.class).x);
        Map mp = gson.fromJson(list, Map.class);
        chunks = new ArrayList<Chunk>(chunk_width*chunk_height);
        this.data = mp.data;
        this.height = mp.height;
        this.id = mp.id;
        this.name = mp.name;
        this.opacity = mp.opacity;
        this.type = mp.type;
        this.visible = mp.visible;
        this.width = mp.width;
        this.x = mp.x;
        this.y = mp.y;
        setObjects(jse);
//        System.out.println("HIII");
        System.out.println(chunk_width);
        fill_chunks();
    }

    public void setObjects(JsonElement dta) {
        Gson gson = new Gson();
        this.objects = gson.fromJson(dta, listType);
    }

    public List<Integer> getData(){
        return data;
    }

    private void fill_chunks(){
        logger.info("Chunk filled called");
        int cTileX = width/chunk_width;
        int cTileY = height/chunk_height;
        int[] chunk_tiles = new int[chunk_width * chunk_height];
        int[] chunk_objects = new int[chunk_width * chunk_height];
        int num = 0;

        for(int ay = 0; ay < height; ay += chunk_height){
            for(int ax = 0; ax < width; ax += chunk_width){
                //
                for(int y = 0; y < chunk_height; y++){
                    for(int x = 0; x < chunk_width; x++){
                        chunk_tiles[x + y*chunk_width] = data.get((x+ax) + (ay+y)*width);
                        chunk_objects[x + y*chunk_width] = objects.get((x+ax) + (ay+y)*width);
                    }
                }
                Chunk t = new Chunk(chunk_width * tile_size, chunk_height * tile_size, chunk_tiles, chunk_objects, num, this, tileSet);
                chunks.add(t);
                num++;
            }
        }
    }

    public int getChunk_width() {
        return width/chunk_width;
    }

    public int getChunk_height() {
        return height/chunk_height;
    }

    public int getLevel_width(){
        return width*tile_size;
    }

    public int getLevel_height(){
        return height*tile_size;
    }

    public Chunk get_chunk(int chunk_num){
        return chunks.get(chunk_num);
    }

    public int getTile_size(){
        return tile_size;
    }

    public int get_cWidth(){
        return chunk_width;
    }

    public int get_cHeight(){
        return chunk_height;
    }

}
