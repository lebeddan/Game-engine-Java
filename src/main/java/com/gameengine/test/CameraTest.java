package com.gameengine.test;

import com.gameengine.game.gameobjects.Camera;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CameraTest {
    @Test
    void CameraNotNullTest() {
        Camera camera = new Camera("player");
        Assertions.assertNotNull(camera);
    }

    @Test
    void OffXCamera() {
        Camera camera = new Camera("player");
        Assertions.assertEquals(0, camera.getOffX());
    }
}