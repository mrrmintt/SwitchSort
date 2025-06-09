package com.example.switchsort.backend.game;

public class ScoreManager {
    private int currentScore = 0;
    private int streak = 0;
    private int correctMatches = 0;
    private String difficulty;
    private long startTime;

    private boolean isTimeRush;

    // Weil man ein Spiel unendlich lange spielen kann
    private static final int MAX_SCORE = 1_000_000; // zum Testen auf 500 oder so!
    private boolean isMaxScoreReached = false;

    // Gibt zurück, ob der Maximalpunktestand erreicht wurde
    public boolean hasReachedMaxScore() {
        return isMaxScoreReached;
    }

    // Konstruktor – setzt Schwierigkeitsgrad und ob der Modus Timerush ist
    public ScoreManager(String difficulty, boolean isTimeRush) {
        this.difficulty = difficulty;
        this.startTime = System.currentTimeMillis();
        this.isTimeRush = isTimeRush;
    }

    // Verarbeitet das Ergebnis eines Versuchs (richtig/falsch) und berechnet Punkte
    public void recordMatch(boolean correct, long timeSpentInSeconds) {
        if (correct) {
            int points = calculatePoints(timeSpentInSeconds);
            correctMatches++;
            streak++;

            // Bonuspunkte bei Serien (Multiples von 3, 5, 10)
            if (streak % 10 == 0) {
                points += 200;
            } else if (streak % 5 == 0) {
                points += 100;
            } else if (streak % 3 == 0) {
                points += 50;
            }

            currentScore += points;

            // Maximalpunktzahl erreicht → Spiel beenden
            if (currentScore >= MAX_SCORE) {
                currentScore = MAX_SCORE;
                isMaxScoreReached = true;
            }
        } else {
            streak = 0; // Serie bei Fehler resetten
        }
    }

    // Berechnet Punkte anhand von Schwierigkeit und Zeit (je schneller, desto mehr)
    private int calculatePoints(long timeSpentInSeconds) {
        int basePoints = 100;
        float multiplier = getDifficultyMultiplier();
        float speedMultiplier = getSpeedMultiplier(timeSpentInSeconds);
        return (int)(basePoints * multiplier * speedMultiplier);
    }

    // Gibt Multiplikator basierend auf Schwierigkeitsgrad zurück
    private float getDifficultyMultiplier() {
        switch (difficulty) {
            case "HARD":
                return 2.0f;
            case "MEDIUM":
                return 1.5f;
            default: // EASY oder unbekannt
                return 1.0f;
        }
    }

    // Gibt Geschwindigkeits-Multiplikator zurück (je schneller geantwortet, desto höher)
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

    // Gibt aktuellen Punktestand zurück
    public int getCurrentScore() {
        return currentScore;
    }

    // Gibt aktuelle Treffer-Serie zurück
    public int getStreak() {
        return streak;
    }

}