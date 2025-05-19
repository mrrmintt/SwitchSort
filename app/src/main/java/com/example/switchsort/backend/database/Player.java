package com.example.switchsort.backend.database;

public class Player {
    private String name;
    private String deviceId;
    private int score;
    private String difficulty;
    private String gameMode;

    public Player(String name, String deviceId, int score, String difficulty, String gameMode) {
        this.name = name;
        this.deviceId = deviceId;
        this.score = score;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
    }

    // Add getters
    public String getName() { return name; }
    public String getDeviceId() { return deviceId; }
    public int getScore() { return score; }
    public String getDifficulty() { return difficulty; }
    public String getGameMode() { return gameMode; }
}