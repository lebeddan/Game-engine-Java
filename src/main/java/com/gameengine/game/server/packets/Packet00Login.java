package com.gameengine.game.server.packets;

import com.gameengine.game.server.GameClient;
import com.gameengine.game.server.GameServer;
/**
 * Packet00Login class extend Packet. Class used for send information of logging user.
 * @author Vasily Levitskiy
 */
public class Packet00Login extends Packet{
    /**
     * Parametrs for packet login starts
     */
    private String username;
    private float x,y;
    /**
     * Parametrs for packet login ends
     */

    /**
     * A public constructor for read data of logging user.
     * @param data - data of user
     */
    public Packet00Login(byte[] data){
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
    }

    /**
     * A public constructor for set info about logging user.
     * @param username - name of user
     * @param x - position on X axis of user
     * @param y - position on Y axis of user
     */
    public Packet00Login(String username, float x, float y){
        super(00);
        this.x = x;
        this.y = y;
        this.username = username;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }

    @Override
    public byte[] getData() {
        return ("00" + this.username+","+this.getX()+","+this.getY()).getBytes();
    }

    public String getUsername(){
        return this.username;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }
}
