package com.gameengine.game.mapobjects;

import com.gameengine.game.GameManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.*;
import java.util.List;

public class TiledMap {
    private int compressionLevel;
    private int height;
    private boolean infinite;
    private JsonArray layers;
    private int nextlayerid;
    private int nextobjectid;
    private String orientation;
    private String renderorder;
    private String tiledversion;
    private int tileheight;
    private List tilesets;
    private int tilewidth;
    private String type;
    private String version;
    private int width;

    public TiledMap(){

    }

//    public TiledMap(int compressionLevel, int height, boolean infinite, List<Map> layers, int nextlayerid, int nextobjectid, String orientation, String renderorder, String tiledversion, int tileheight, List tilesets, int tilewidth, String type, String version, int width) {
//        this.compressionLevel = compressionLevel;
//        this.height = height;
//        this.infinite = infinite;
//        this.layers = layers;
//        this.nextlayerid = nextlayerid;
//        this.nextobjectid = nextobjectid;
//        this.orientation = orientation;
//        this.renderorder = renderorder;
//        this.tiledversion = tiledversion;
//        this.tileheight = tileheight;
//        this.tilesets = tilesets;
//        this.tilewidth = tilewidth;
//        this.type = type;
//        this.version = version;
//        this.width = width;
//    }

    public Map create_map(String path, GameManager gm){
        Gson gson = new Gson();
        TiledMap tile = null;
        Map map = null;
            InputStream in = getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            tile = gson.fromJson(reader, TiledMap.class);
            JsonElement jse = tile.layers.getAsJsonArray().get(1).getAsJsonObject().get("data");
            map = new Map(tile.layers.getAsJsonArray().get(0), jse, gm);
        return map;
    }
}
