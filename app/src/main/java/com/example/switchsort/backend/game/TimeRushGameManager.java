package com.example.switchsort.backend.game;

public class TimeRushGameManager {
    private static final int INITIAL_TIME = 60;
    private static final int SECOND_PHASE_TIME = 20;
    private static final float TIME_BONUS = 0.5f;
    private static final int FINAL_PHASE_TIME = 10;
    private static final int MAX_LIVES = 3;
    private static final int BASE_POINTS = 100;
    private static final int STREAK_BONUS = 50;
    private static final int PHASE_MULTIPLIER_1 = 1;
    private static final int PHASE_MULTIPLIER_2 = 2;
    private static final int PHASE_MULTIPLIER_3 = 3;
    private float currentTime;
    private int currentPhase = 1;
    private int lives;
    private boolean isGameOver;
    private int score;
    private int currentStreak;
    private TimeRushNumberGenerator numberGenerator;
    public TimeRushGameManager(String difficulty) {
        this.currentTime = INITIAL_TIME;
        this.lives = MAX_LIVES;
        this.isGameOver = false;
        this.score = 0;
        this.currentStreak = 0;
        this.numberGenerator = new TimeRushNumberGenerator(difficulty);
    }
    public void updateTimer() {
        currentTime -= 0.1;
        if (currentTime <= 0) {
            currentTime = 0;
            isGameOver = true;
        }
    }
    public void addTimeBonus() {
        currentTime += TIME_BONUS;
    }
    public String getCurrentTimeFormatted() {
        return String.format("%.1f", currentTime);
    }
    private void switchToNextPhase() {
        currentPhase++;
        switch (currentPhase) {
            case 2:
                currentTime = SECOND_PHASE_TIME;
                break;
            case 3:
                currentTime = FINAL_PHASE_TIME;
                break;
            default:
                currentTime = 0;
                isGameOver = true;
                break;
        }
    }
    public void loseLife() {
        lives--;
        if (lives <= 0) {
            isGameOver = true;
        }
    }
    public void addScore(boolean isCorrect) {
        if (isCorrect) {
            currentStreak++;
            int points = BASE_POINTS;
            switch (currentPhase) {
                case 1:
                    points *= PHASE_MULTIPLIER_1;
                    break;
                case 2:
                    points *= PHASE_MULTIPLIER_2;
                    break;
                case 3:
                    points *= PHASE_MULTIPLIER_3;
                    break;
            }
            if (currentStreak >= 5) {
                points += STREAK_BONUS;
            }
            score += points;
        } else {
            currentStreak = 0;
        }
    }
    // Getters
    public float getCurrentTime() { return currentTime; }
    public int getCurrentStreak() {
        return currentStreak;
    }
    public int getLives() { return lives; }
    public boolean isGameOver() { return isGameOver; }
    public int getScore() { return score; }
    public int getCurrentPhase() { return currentPhase; }
    public String generateNewNumber() { return numberGenerator.generateTargetNumber(); }
}