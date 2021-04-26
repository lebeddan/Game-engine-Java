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

    private int chunk_width = 4;
    private int chunk_height = 3;
    private int tile_size = 64;

    Type listType = new TypeToken<List<Integer>>() {}.getType();

    public Map(JsonElement list){
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
//        System.out.println("HIII");
        System.out.println(chunk_width);
        fill_chunks();
    }

    public static Map creat_m(JsonElement list){
        Map mp = null;
        return mp;
    }

    public void setObjects(JsonElement dta) {
        Gson gson = new Gson();
        this.objects = gson.fromJson(dta, listType);
    }

    public List<Integer> getData(){
        return data;
    }

    private void fill_chunks(){
//        System.out.println(chunk_width);
        int cTileX = width/chunk_width;
        int cTileY = height/chunk_height;
        for(int i =0; i < chunk_height*chunk_width; i++){
            int[] chunk_tiles = new int[cTileX*cTileY];
            for (int y = 0; y < cTileY; y++){
                for(int x =0; x < cTileX; x++){
                    chunk_tiles[x+y*cTileX]=data.get((x+i*cTileX)+(y+i*cTileY)*chunk_width);
                }
            }
            System.out.println(cTileX*tile_size);
            Chunk t = new Chunk(cTileX*tile_size,cTileY*tile_size, chunk_tiles);
            chunks.add(t);
        }
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
