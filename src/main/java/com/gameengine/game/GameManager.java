package com.gameengine.game;

import com.gameengine.engine.AbstractGame;
import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Renderer;
import com.gameengine.engine.audio.SoundClip;
import com.gameengine.game.gui.GameStage;
import com.gameengine.game.gui.ViewManager;
import com.gameengine.game.gameobjects.Camera;
import com.gameengine.game.gameobjects.Enemy;
import com.gameengine.game.gameobjects.GameObject;
import com.gameengine.game.gameobjects.Multiplayer.PlayerMP;
import com.gameengine.game.gameobjects.Player;
import com.gameengine.game.mapobjects.Chunk;
import com.gameengine.game.mapobjects.Map;
import com.gameengine.game.mapobjects.TiledMap;
import com.gameengine.game.server.GameClient;
import com.gameengine.game.server.GameServer;
import com.gameengine.game.server.packets.Packet00Login;
import com.gameengine.game.server.packets.Packet01Disconnect;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
 * The main runner of the game.
 * Calls all of the functions for the game(Update, render).
 * Can check for collisions.
 * Needs to have a GameContainer on creation.
 * @author Vasily Levitskiy
 */

public class GameManager extends AbstractGame {

    // Tileset tilesize
    public static final int TS = 64;
    public static final int WIDTH = 1280, HEIGHT = 720;

    private Camera camera;
    private List<GameObject> objects = new ArrayList<GameObject>();
    private String username;
    private Player player;

    public GameServer socketServer;
    public GameClient socketClient;

    private int levelW, levelH, levelWChunks, chunk_width, chunk_height;
    private int cur_chunk;
    private Map map;
    private List<Chunk> loaded_chunks;

    private Stage stage;
    private GameStage gameStage;
    private ViewManager viewManager;
    private GameContainer gc;

    private Logger logger;

    private SoundClip bgMusic = new SoundClip("/Sounds/gameBGmusic.mp3", "bg");

    public GameManager() throws IOException {

    }

    /**
     *
     * @param stage - At what stage to start the game
     * @param username - Players username
     * @param server - Start the server or no
     * @param IP - On which ip to start server/connect to
     * @throws IOException
     */
    public void startGame(Stage stage, String username, boolean server, String IP) throws IOException {
        gc = new GameContainer(this);
        bgMusic.loop();
        bgMusic.setVolume(0.6);
        bgMusic.play();

        gc.setWidth(WIDTH);
        gc.setHeight(HEIGHT);
        gc.setScale(1);
        gc.start();
        gameStage = new GameStage(gc, this);

        if(server){
            socketServer = new GameServer(this, IP);
            socketServer.start();
        }

        socketClient = new GameClient(IP, this);
        socketClient.start();

        loadLevel("/Maps/maptilesettest.json");

        this.username = username;
        player = new PlayerMP(1, 8, username, gc.getInput(), null, -1);
        Packet00Login loginPacket = new Packet00Login(username, player.getPosX(), player.getPosY());
        getObjects().add(player);

        if(socketServer != null){
            socketServer.addConnection((PlayerMP)player, loginPacket);
        }
        loginPacket.writeData(socketClient);

        camera = new Camera(username);

        // Manually add enemies
        getObjects().add(new Enemy(18, 9, this));

    }

    /**
     * Exit's the game and send a Disconnection packet.
     */
    @Override
    public void stop(){
        if(socketClient != null) {
            Packet01Disconnect packet = new Packet01Disconnect(player.getUsername());
            packet.writeData(socketClient);
        }
        System.exit(0);
    }

    /**
     * Shows in-game pause menu
     */
    public void showPMenu(){
        if(gameStage.isHidden()){
            gameStage.showPauseMenu();
        } else {
            gameStage.hidePauseMenu();
        }
    }

    /**
     * Shows death menu
     */
    public void showDeathMenu(){
        gameStage.showDeathMenu();
    }

    /**
     * Launches the game
     */
    public static void main(){
        launch();
    }

    /**
     * Exits the game
     * @param e
     */
    @FXML
    public void exitApplication(ActionEvent e){
        Platform.exit();
    }

    /**
     * Starts the game
     * @param stage Tells the first stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Tank Souls");
        viewManager = new ViewManager(this, stage);
        stage = viewManager.getMainStage();
        this.stage = stage;
        this.stage.show();
    }

    @Override
    public void init(GameContainer gc){
        logger = Logger.getLogger(String.valueOf(GameManager.class));;
        logger.info("Starting the game.");
        gc.getRenderer().setAmbientColor(-1);
    }

    /**
     * Loads the game level
     * @param path Path to the map containing the level
     * @throws IOException
     */
    public void loadLevel(String path) throws IOException {
        TiledMap tm = new TiledMap();
        map = tm.createMap(path, this);

        loaded_chunks = new ArrayList<Chunk>();
        loaded_chunks.add(map.get_chunk(0));
        cur_chunk = -1;

        levelW = map.getLevel_width();
        levelH = map.getLevel_height();
        levelWChunks = map.getChunk_width();
        chunk_width = map.get_cWidth()*TS;
        chunk_height = map.get_cHeight()*TS;

        logger.info("Level loaded");
    }

    /**
     * Updates every object in loaded chunks and the general game objects
     * @param gc The GameContainer
     * @param deltaTime Time between frames
     */
    @Override
    public void update(GameContainer gc, float deltaTime){
        if (player.isDead()){
            return;
        }

        // Checks the current chunk.
        int cur_pos_x = 0;
        int cur_pos_y = 0;
        try {
            cur_pos_x = (int) Math.floor(getObject(username).getPosX() / chunk_width);
            cur_pos_y = (int) Math.floor(getObject(username).getPosY() / chunk_height);
        } catch (NullPointerException e){
            cur_pos_x = 0;
            cur_pos_y = 0;
        }

        // Load other chunks if the chunk has changed
        if(cur_chunk != cur_pos_x + cur_pos_y*levelWChunks){
            cur_chunk = cur_pos_x + cur_pos_y*levelWChunks;
            System.out.println(cur_pos_y);
            System.out.println(cur_pos_x);
            loaded_chunks.clear();

            for(int i = cur_pos_y-1; i <= cur_pos_y+1; i++) {
                for(int j = cur_pos_x-1; j <= cur_pos_x+1; j++) {
                    if(j >= 0 && j < levelWChunks && i >= 0 && i < map.getChunk_height()) {
                        if(j + i*levelWChunks < map.getChunk_height()*levelWChunks){
                            loaded_chunks.add(map.get_chunk(j + i*levelWChunks));
                        }
//                        System.out.println("Chunk to load: " + i + " " + j);
                    }
                }
            }
        }
        for(int i = 0; i < getObjects().size(); i++){
            getObjects().get(i).update(gc, this, deltaTime);
            if(getObjects().get(i).isDead()){
                getObjects().remove(i);
                i--;
            }
        }
        for(Chunk chunk : loaded_chunks){
            chunk.getObjects().removeIf(GameObject::isDead);
            for(GameObject obj : chunk.getObjects()){
                if(obj instanceof Enemy){
                    obj.update(gc, this, deltaTime);
                }
            }
        }
        camera.update(gc, this, deltaTime);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        camera.render(r);

        for(int i = 0; i < loaded_chunks.toArray().length; i++){
            Chunk chnk = loaded_chunks.get(i);
            r.drawChunk(chnk, chnk.getPosX(), chnk.getPosY());
        }

        for(GameObject obj : new ArrayList<>(objects)){
            obj.render(gc, r);
        }

        for(Chunk cnk : loaded_chunks){
            for(GameObject obj : cnk.getObjects()){
                obj.render(gc, r);
            }
        }
        r.drawFillRect(0,0, 100, 100, 0x80ff0000);
    }

    public static void main(String args[]) throws IOException {
        launch(args);
    }

    public synchronized List<GameObject> getObjects(){
        return this.objects;
    }

    public void addObject(GameObject obj){
        getObjects().add(obj);
    }

    public GameObject getObject(String tag){
        for(int i = 0; i < getObjects().size(); i++){
            if(getObjects().get(i).getTag().equals(tag)){
                return getObjects().get(i);
            }
        }

        return null;
    }

    private int getObjectIndex(String username){
        int index = -1;
        for(GameObject obj : getObjects()){
            if(obj instanceof PlayerMP && ((PlayerMP)obj).getUsername().equals(username)){
                break;
            }
            index++;
        }
        return index;
    }

    public void check_collisions(GameObject mainObj){
        String mainType = mainObj.getShape();
        for(Chunk chunk : loaded_chunks) {
            ArrayList<GameObject> arr = new ArrayList<>(chunk.getObjects());
            for(GameObject ob : objects){
                if((ob instanceof PlayerMP && mainObj != ob) || ob instanceof Enemy){
                    arr.add(ob);
                }
            }
            for (GameObject obj : arr){
                if(obj.getShape().equals("square")) {
                    Point2D center = Point2D.ZERO.add(mainObj.getCenter().getX() + mainObj.getPosX(), mainObj.getCenter().getY() + mainObj.getPosY());
                    int sqHalfX = obj.getWidth()/2;
                    int sqHalfY = obj.getHeight()/2;

                    Point2D aabb = Point2D.ZERO.add(sqHalfX+obj.getPosX(), sqHalfY+obj.getPosY());

                    int diffX = (int) (center.getX() - aabb.getX());
                    int diffY = (int) (center.getY() - aabb.getY());

                    int diffClampX = clamp(diffX, -sqHalfX, sqHalfX);
                    int diffClampY = clamp(diffY, -sqHalfY, sqHalfY);

                    Point2D closest = Point2D.ZERO.add(aabb.getX() + diffClampX, aabb.getY() + diffClampY);

                    if(closest.distance(center) < mainObj.getRadius()){
                        obj.hit(mainObj, this);
                        mainObj.hit(obj, this);
                    }
                } else if (obj.getShape().equals("circle")){
                    Point2D mainCent = Point2D.ZERO.add(mainObj.getPosX()+mainObj.getCenter().getX(), mainObj.getPosY()+mainObj.getCenter().getY());
                    Point2D objCent = Point2D.ZERO.add(obj.getPosX()+obj.getCenter().getX(), obj.getPosY()+obj.getCenter().getY());
                    if(mainCent.distance(objCent) < (mainObj.getRadius()+obj.getRadius())){
                        obj.hit(mainObj, this);
                        mainObj.hit(obj, this);
                    }
                } else if (obj.getShape().equals("someShape")){
                    boolean collisionX = (mainObj.getPosX() + mainObj.getWidth() >= obj.getPosX()) && (obj.getPosX() + obj.getWidth() >= mainObj.getPosX());
                    boolean collisionY = (mainObj.getPosY() + mainObj.getHeight() >= obj.getPosY()) && (obj.getPosY() + obj.getHeight() >= mainObj.getPosY());

                    if (collisionX && collisionY) {
                        mainObj.hit(obj, this);
                        obj.hit(mainObj, this);
                    }
                }
            }
        }
    }

    public Point2D check_radius(Enemy mainObj){
        ArrayList<GameObject> arr = new ArrayList<>();
        for(GameObject ob : getObjects()){
            if(ob instanceof PlayerMP){
                arr.add(ob);
            }
        }
        for (GameObject obj : arr){
            if (obj.getShape().equals("circle")){
                Point2D mainCenter = Point2D.ZERO.add(mainObj.getPosX()+mainObj.getCenter().getX(), mainObj.getPosY()+mainObj.getCenter().getY());
                Point2D objCenter = Point2D.ZERO.add(obj.getPosX()+obj.getCenter().getX(), obj.getPosY()+obj.getCenter().getY());
                if(mainCenter.distance(objCenter) < (mainObj.getDetectRadius()+obj.getRadius())){
                    return Point2D.ZERO.add(objCenter);
                }
            } else if (obj.getShape().equals("someShape")){
                boolean collisionX = (mainObj.getPosX() + mainObj.getWidth() >= obj.getPosX()) && (obj.getPosX() + obj.getWidth() >= mainObj.getPosX());
                boolean collisionY = (mainObj.getPosY() + mainObj.getHeight() >= obj.getPosY()) && (obj.getPosY() + obj.getHeight() >= mainObj.getPosY());

                if (collisionX && collisionY) {
                    System.out.println("Nice");
                    mainObj.hit(obj, this);
                    obj.hit(mainObj, this);
                }
            }
        }
        return null;
    }

    public void removePlayerMP(String username){
        int index = 0;
        for(GameObject obj : getObjects()){
            if(obj instanceof PlayerMP && ((PlayerMP) obj).getUsername().equals(username)){
                break;
            }
            index++;
        }
        getObjects().remove(index);
    }


    public int getLevelW() {
        return levelW;
    }

    public int getLevelH() {
        return levelH;
    }

    public String getUsername(){
        return username;
    }

    public int clamp(int value, int min, int max){
        return  Math.max(min, Math.min(max, value));
    }
}
