package com.gameengine.game.mapobjects;

import com.gameengine.engine.gfx.ImageTile;
import com.gameengine.game.GameManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
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

    // MAP DIMENSION MUST BE DIVIDABLE BY :
    private int chunk_width = 20;
    private int chunk_height = 12;
    private int tile_size = 64;

    private GameManager gm;
    private ArrayList<Chunk> chunks;
    private List<Integer> objects;
    private ImageTile tileSet;

    {
        try {
            tileSet = new ImageTile("/Tile/myspritesheet.png", 16, 16,tile_size/16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Type listType = new TypeToken<List<Integer>>() {}.getType();
    public Map(){

    }

    public Map(JsonElement list, JsonElement jse, GameManager gm){
        this.gm = gm;
//        Gson gson = new Gson();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Map.class, new MapAdapter());
        builder.setPrettyPrinting();
        Gson gson = builder.create();
//        System.out.println(gson.fromJson(list, Map.class).x);
        System.out.println(list);
        Map mp = gson.fromJson(list.toString(), Map.class);
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

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
