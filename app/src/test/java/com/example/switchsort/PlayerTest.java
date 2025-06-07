package com.example.switchsort;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.switchsort.backend.database.Player;

public class PlayerTest {

    @Test
    public void testConstructorAndGetters() {
        String name = "TestUser";
        String deviceId = "abc123";
        int score = 900;
        String difficulty = "HARD";
        String gameMode = "TIMERUSH";

        Player player = new Player(name, deviceId, score, difficulty, gameMode);

        assertEquals(name, player.getName());
        assertEquals(deviceId, player.getDeviceId());
        assertEquals(score, player.getScore());
        assertEquals(difficulty, player.getDifficulty());
        assertEquals(gameMode, player.getGameMode());
    }
}

