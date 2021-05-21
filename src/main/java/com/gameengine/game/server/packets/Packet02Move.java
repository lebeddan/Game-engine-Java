package com.gameengine.game.server.packets;

import com.gameengine.game.server.GameClient;
import com.gameengine.game.server.GameServer;
/**
 * Packet00Move class extend Packet. Class used for send information when user move.
 * @author Vasily Levitskiy
 */
public class Packet02Move extends Packet{
    /**
     * Parametrs of packet move starts
     */
    private String username;
    private float x, y, rotation, mouseRot;
    /**
     * Parametrs of packet move ends
     */

    /**
     * A public constructor for get position and position of a mouse user.
     * @param data -
     */
    public Packet02Move(byte[] data){
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
        this.rotation = Float.parseFloat(dataArray[3]);
        this.mouseRot = Float.parseFloat(dataArray[4]);
    }

    /**
     * A public constructor for process position and position of a mouse user.
     * @param username - name of user
     * @param x - position on X axis of user
     * @param y - position on Y axis of user
     * @param rotation - rotation of user
     * @param mouseRot - rotation of mouse
     */
    public Packet02Move(String username, float x, float y, float rotation, float mouseRot){
        super(02);
        this.username = username;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.mouseRot = mouseRot;
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
        return ("02" + this.username+","+this.x+","+this.y+","+this.rotation+","+this.mouseRot).getBytes();
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

    public float getRotation(){
        return this.rotation;
    }

    public float getMouseRot(){
        return this.mouseRot;
    }
}
