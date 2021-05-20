package com.gameengine.test;

import com.gameengine.game.GameManager;
import com.gameengine.game.server.GameClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.*;

class GameClientTest {
    @Test
    void ClientNullTest() {
        try {
            GameManager gm = new GameManager();
            GameClient client = new GameClient("192.168.30.12", gm);
            Assertions.assertNotNull(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void IPAddressEqualTest() {
        try {
            GameManager gm = new GameManager();
            GameClient client = new GameClient("192.168.30.12");
            InetAddress iptest = client.getIpAddress();
            Assertions.assertEquals("192.168.30.12", iptest.getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}