package com.gameengine.test;

import com.gameengine.game.gameobjects.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void positionXOfPlayer() {
        Player player = new Player(10, 20, "test");
        Assertions.assertEquals(320, player.getPosX());
    }

    @Test
    void positionYOfPlayer() {
        Player player = new Player(10, 20, "test");
        Assertions.assertEquals(640, player.getPosY());
    }

    @Test
    void PlayerNotNullTest() {
        Player player = new Player(10, 20, "test");
        Assertions.assertNotNull(player);
    }

    @Test
    void PathToPlayerImageTest() {
        String exceptedPath = "/Users/SamSeppi/Desktop/IdeaProjects/TestEngine/src/main/resources/Player/MagicTank.png";
        Player player = new Player(10, 23, "test");
        Assertions.assertEquals(exceptedPath, player.getPathTankImage());
    }
}