package com.gameengine.game.MapObjects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

    private ArrayList<Tile> chunks;

    private int chunk_width = 4;
    private int chunk_height = 3;
    private int tile_size = 32;

    Type listType = new TypeToken<List<Integer>>() {}.getType();

    public Map(JsonArray list){
    }

    public static Map creat_m(JsonElement list){
        Gson gson = new Gson();
//        System.out.println(gson.fromJson(list, Map.class).x);
        Map mp = gson.fromJson(list, Map.class);
        mp.chunks = new ArrayList<Tile>(mp.chunk_width*mp.chunk_height);
        mp.fill_chunks();
        return mp;
    }

    public List<Integer> getData(){
        return data;
    }

    private void fill_chunks(){
        int cTileX = width/chunk_width;
        int cTileY = height/chunk_height;
        for(int i =0; i < chunk_height*chunk_width; i++){
            int[] chunk_tiles = new int[cTileX*cTileY];
            for (int y = 0; y < cTileY; y++){
                for(int x =0; x < cTileX; x++){
                    chunk_tiles[x+y*cTileX]=data.get((x+i*cTileX)+(y+i*cTileY)*chunk_width);
                }
            }
            Tile t = new Tile(cTileX*tile_size,cTileY*tile_size, chunk_tiles);
            chunks.add(t);
        }
    }

    public Tile get_chunk(int chunk_num){
        return chunks.get(chunk_num);
    }

    private int get_cWidth(){
        return chunk_width;
    }

    private int get_cHeight(){
        return chunk_height;
    }

}
