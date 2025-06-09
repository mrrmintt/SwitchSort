package com.example.switchsort.backend.game;

// Datenklasse für den aktuellen Zustand des Spiels
public class GameState {
    private final String[] board;
    private final String targetCharacter;
    private final int lives;
    private final int currentScore;


    private final int streak;
    private final String timerText;



    // Konstruktor initialisiert alle Felder
    public GameState(String[] board, String targetCharacter, int lives, int currentScore,
                      int streak, String timerText) {
        this.board = board;
        this.targetCharacter = targetCharacter;
        this.lives = lives;
        this.currentScore = currentScore;

        this.streak = streak;
        this.timerText = timerText;
    }

    public String[] getBoard() { return board; }
    public String getTargetCharacter() { return targetCharacter; }
    public int getLives() { return lives; }
    public int getCurrentScore() { return currentScore; }


    public int getStreak() { return streak; }
    public String getTimerText() { return timerText; }
}


