package com.gameengine.game.gameobjects.Multiplayer;

import com.gameengine.engine.GameContainer;
import com.gameengine.engine.Input;
import com.gameengine.game.GameManager;
import com.gameengine.game.gameobjects.Player;

import java.io.IOException;
import java.net.InetAddress;

public class PlayerMP extends Player {

    public InetAddress ipAddress;
    public int port;

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
