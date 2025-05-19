package com.example.switchsort.backend.game;

public class ScoreManager {
    private int currentScore = 0;
    private int streak = 0;
    private int correctMatches = 0;
    private String difficulty;
    private long startTime;

    public ScoreManager(String difficulty) {
        this.difficulty = difficulty;
        this.startTime = System.currentTimeMillis();
    }

    public void recordMatch(boolean correct, long timeSpentInSeconds) {
        if (correct) {
            int points = calculatePoints(timeSpentInSeconds);
            correctMatches++;
            streak++;

            // Add streak bonuses
            if (streak % 10 == 0) {
                points += 200; // 10 streak bonus
            } else if (streak % 5 == 0) {
                points += 100; // 5 streak bonus
            } else if (streak % 3 == 0) {
                points += 50;  // 3 streak bonus
            }

            currentScore += points;
        } else {
            streak = 0; // Reset streak on wrong answer
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

    public void addPerfectRoundBonus(boolean isGameComplete) {
        if (isGameComplete && correctMatches == 20) {
            currentScore += 1000; // Perfect game bonus
        } else if (correctMatches == 10) {
            currentScore += 500;  // Perfect first half bonus
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getStreak() {
        return streak;
    }
}