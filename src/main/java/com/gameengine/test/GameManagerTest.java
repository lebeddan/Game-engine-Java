package com.gameengine.test;

import com.gameengine.game.GameManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    @Test
    void GameManagerNotNullTest() {
        try {
            GameManager gm = new GameManager();
            Assertions.assertNotNull(gm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void TSTest() {
        try {
            GameManager gm = new GameManager();
            Assertions.assertEquals(64, gm.TS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
