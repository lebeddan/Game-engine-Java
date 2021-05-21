package com.gameengine.game.mapobjects;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.logging.Logger;
/**
 * TiledMap class. Game object which loads the map.
 * @author Vasily Levitskiy
 */
public class TiledMap {
    private static final Logger logger = Logger.getLogger(String.valueOf(TiledMap.class));
    /**
     * Parametrs of tiled map starts.
     */
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
    /**
     * Parametrs of tiled map ends.
     */

    /**
     * A public constructor
     * @param infinite
     */
    public TiledMap(boolean infinite){
        this.infinite = true;
    }

    /**
     * A public function which created a map
     * @param path - path to map image
     * @return Map object
     */
    public static Map create_map(String path){
        Gson gson = new Gson();
        TiledMap tile = null;
        Map map = null;
        try {
            Reader reader = new FileReader(path);
            tile = gson.fromJson(reader, TiledMap.class);
            JsonElement jse = tile.layers.getAsJsonArray().get(1).getAsJsonObject().get("data");
            map = new Map(tile.layers.getAsJsonArray().get(0), jse);
        } catch (FileNotFoundException e) {
            logger.finest("File not found "+ e);
        }
        return map;
    }
}
