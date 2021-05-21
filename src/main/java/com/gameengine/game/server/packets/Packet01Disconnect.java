package com.gameengine.game.server.packets;

import com.gameengine.game.server.GameClient;
import com.gameengine.game.server.GameServer;
/**
 * Packet00Disconnect class extend Packet. Class used for send information of disconnected user.
 * @author Vasily Levitskiy
 */
public class Packet01Disconnect extends Packet{
    /**
     * Parametrs for packet disconnect starts.
     */
    private String username;
    /**
     * Parametrs for packet disconnect ends.
     */

    /**
     * A public constructor for read data of disconnected user.
     * @param data - data of user
     */
    public Packet01Disconnect(byte[] data){
        super(01);
        this.username = readData(data);
    }

    /**
     * A public constructor for set name of disconnected user.
     * @param username - name of user
     */
    public Packet01Disconnect(String username){
        super(01);
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
        return ("01" + this.username).getBytes();
    }

    public String getUsername(){
        return this.username;
    }
}
