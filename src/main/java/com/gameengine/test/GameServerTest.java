package com.gameengine.test;

import com.gameengine.game.GameManager;
import com.gameengine.game.server.GameServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameServerTest {
    @Test
    void ServerNotNullTest() {
        GameManager gm = null;
        try {
            gm = new GameManager();
            GameServer server = new GameServer(gm);
            Assertions.assertNotNull(server);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void SocketNotNullTest() {
        GameManager gm = null;
        try {
            gm = new GameManager();
            GameServer server = new GameServer(gm);
            Assertions.assertNotNull(server.getSocket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}