package com.gameengine.game.MapObjects;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Map {
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

    private ArrayList<Chunk> chunks;
    private List<Integer> objects;

    private int chunk_width = 10;
    private int chunk_height = 6;
    private int tile_size = 128;

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
        System.out.println("CHunk filled called");
        int cTileX = width/chunk_width;
        int cTileY = height/chunk_height;
        for(int i =0; i < (width*height)/(chunk_width*chunk_height); i++){
            int[] chunk_tiles = new int[chunk_width*chunk_height];
            int[] chunk_objects = new int[chunk_width*chunk_height];
            for (int y = 0; y < chunk_height; y++){
                for(int x =0; x < chunk_width; x++){
                    chunk_tiles[x+y*chunk_width]=data.get((x+(i%chunk_width))+(y+(i%chunk_height))*width);
                    chunk_objects[x+y*chunk_width]=objects.get((x+(i%chunk_width))+(y+(i%chunk_height))*width);
                }
            }
            System.out.println(i);
            Chunk t = new Chunk(chunk_width*tile_size,chunk_height*tile_size, chunk_tiles, chunk_objects, i, width/chunk_width);
            chunks.add(t);
        }
    }

    public int getChunk_width() {
        return width/chunk_width;
    }

    public int getChunk_height() {
        return width/chunk_height;
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

    private int get_cWidth(){
        return chunk_width;
    }

    private int get_cHeight(){
        return chunk_height;
    }

}
