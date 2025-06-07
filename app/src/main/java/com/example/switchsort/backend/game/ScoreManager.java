package com.example.switchsort.backend.game;

public class ScoreManager {
    private int currentScore = 0;
    private int streak = 0;
    private int correctMatches = 0;
    private String difficulty;
    private long startTime;

    private boolean isTimeRush;

    // Weil man ein Spiel unendlich lange spielen kann
    private static final int MAX_SCORE = 500; //1_000_000;
    private boolean isMaxScoreReached = false;

    public boolean hasReachedMaxScore() {
        return isMaxScoreReached;
    }

    public ScoreManager(String difficulty, boolean isTimeRush) {
        this.difficulty = difficulty;
        this.startTime = System.currentTimeMillis();
        this.isTimeRush = isTimeRush;
    }
    public void recordMatch(boolean correct, long timeSpentInSeconds) {
        if (correct) {
            int points = calculatePoints(timeSpentInSeconds);
            correctMatches++;
            streak++;
            // Add streak bonuses
            if (streak % 10 == 0) {
                points += 200; // 10 streak
            } else if (streak % 5 == 0) {
                points += 100; // 5 streak
            } else if (streak % 3 == 0) {
                points += 50;  // 3 streak
            }
            currentScore += points;
            if (currentScore >= MAX_SCORE) {
                currentScore = MAX_SCORE;
                // Signalisiert GameManager, dass Schluss ist
                isMaxScoreReached = true;
            }
        } else {
            streak = 0;
        }
    }
    private int calculatePoints(long timeSpentInSeconds) {
        int basePoints = 100;
        float multiplier = getDifficultyMultiplier();
        float speedMultiplier = getSpeedMultiplier(timeSpentInSeconds);

        return (int)(basePoints * multiplier * speedMultiplier);
    }
    private float getDifficultyMultiplier() {
        switch (difficulty) {
            case "HARD":
                return 2.0f;
            case "MEDIUM":
                return 1.5f;
            default:
                return 1.0f;
        }
    }
    private float getSpeedMultiplier(long timeSpentInSeconds) {
        switch (difficulty) {
            case "HARD":
                if (timeSpentInSeconds <= 0.5) return 8.0f;
                if (timeSpentInSeconds <= 1.0) return 5.0f;
                return 3.0f;

            case "MEDIUM":
                if (timeSpentInSeconds <= 1.0) return 5.0f;
                if (timeSpentInSeconds <= 3.0) return 3.0f;
                return 2.0f;

            default: // EASY
                if (timeSpentInSeconds <= 2.0) return 3.0f;
                if (timeSpentInSeconds <= 5.0) return 2.0f;
                return 1.0f;
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }
    public int getStreak() {
        return streak;
    }
}