package com.gameengine.game.server.packets;

import com.gameengine.game.server.GameClient;
import com.gameengine.game.server.GameServer;
/**
 * The abstract class which defines the standard properties and methods of packet objects.
 * @author Vasily Levitskiy
 */
public abstract class Packet {
    /**
     * A enum which store types of packet
     */
    public static enum PacketTypes{
        INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), BULLET(03);

        private int packetId;
        private PacketTypes(int packetId){
            this.packetId = packetId;
        }

        public int getId(){
            return packetId;
        }
    }

    public byte packetId;

    public Packet(int packetId){
        this.packetId = (byte) packetId;
    }

    public abstract void writeData(GameClient client);

    public abstract void writeData(GameServer server);

    /**
     * A public function for read data
     * @param data - received data
     * @return String message
     */
    public String readData(byte[] data){
        String message = new String(data).trim();
        return message.substring(2); // Delete the id from the message
    }

    /**
     * A public function which search a packet
     * @param id - id of packet
     * @return a searched packet
     */
    public static PacketTypes lookupPacket(String id){
        try{
            return lookupPacket(Integer.parseInt(id));
        } catch (NumberFormatException e){
            return PacketTypes.INVALID;
        }
    }

    public static PacketTypes lookupPacket(int id){
        for (PacketTypes p : PacketTypes.values()){
            if (p.getId() == id){
                return p;
            }
        }
        return PacketTypes.INVALID;
    }

    public abstract byte[] getData();
}
