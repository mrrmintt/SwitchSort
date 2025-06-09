package com.example.switchsort.backend.game;

public class GameConfig {
    public static final int EASY_GRID_SIZE = 3;
    public static final int MEDIUM_GRID_SIZE = 5;
    public static final int HARD_GRID_SIZE = 7;
    public static final int EASY_TIME_LIMIT = 60;
    public static final int MEDIUM_TIME_LIMIT = 45;
    public static final int HARD_TIME_LIMIT = 30;


    // Holt Spielfeldgröße auf Schwieriegkeit basierend
    public static int getGridSize(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "MEDIUM":
                return MEDIUM_GRID_SIZE;
            case "HARD":
                return HARD_GRID_SIZE;
            default:
                return EASY_GRID_SIZE;
        }
    }

    // Holt Timelimit basierend auf Schwioereigkeit
    public static int getTimeLimit(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "MEDIUM":
                return MEDIUM_TIME_LIMIT;
            case "HARD":
                return HARD_TIME_LIMIT;
            default:
                return EASY_TIME_LIMIT;
        }
    }
}