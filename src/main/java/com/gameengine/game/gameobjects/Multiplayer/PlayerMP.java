package com.gameengine.game.gameobjects.Multiplayer;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Input;
import com.gameengine.game.GameManager;
import com.gameengine.game.gameobjects.Player;

import java.io.IOException;
import java.net.InetAddress;
/**
 * PlayerMP class. Sets a IP, usernmae and port for multiplayer.
 * @author Vasily Levitskiy
 */
public class PlayerMP extends Player {
    /**
     * Parameters for connection starts.
     */
    public InetAddress ipAddress;
    public int port;
    /**
     * Parameters for connection ends.
     */

    /**
     * A public constructor for setting ip,username and port for player.
     * @param posX - start position of player
     * @param posY - start position of player
     * @param username - username of player
     * @param input - input of player
     * @param ipAddress - IP of player
     * @param port - port of player
     * @throws IOException
     */
    public PlayerMP(int posX, int posY, String username, Input input, InetAddress ipAddress, int port) throws IOException {
        super(posX, posY, username, input);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public PlayerMP(int posX, int posY, String username, InetAddress ipAddress, int port) throws IOException {
        super(posX, posY, username, null);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public void update(GameContainer gc, GameManager gm, float dt){
        super.update(gc, gm, dt);
    }
}
