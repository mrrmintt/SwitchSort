package com.example.switchsort.backend.game;

public class GameManager {
    private final String difficulty;
    private final String mode;
    private final boolean gameMode; // "CLASSIC" oder "TIMERUSH"
    private final int maxLives = 3;
    private int lives;
    private final GameBoard gameBoard;
    private final ScoreManager scoreManager;
    private long roundStartTime;
    private final int timeLimitPerRound; // in Sekunden für TimeRush

    public GameManager(String difficulty, String mode, boolean gameMode) {
        this.difficulty = difficulty.toUpperCase();
        this.mode = mode.toUpperCase(); // "LETTER" oder "NUMBER"
        this.gameMode = gameMode; // "CLASSIC" oder "TIMERUSH"
        this.lives = maxLives;
        int gridSize = GameConfig.getGridSize(difficulty);
        this.timeLimitPerRound = GameConfig.getTimeLimit(difficulty);
        this.gameBoard = new GameBoard(gridSize);
        this.scoreManager = new ScoreManager(difficulty, gameMode);
        startNewRound();
    }

    public void startNewRound() {
        if (isGameOver()) return;
        gameBoard.generateNewBoard(difficulty, mode);
        roundStartTime = System.currentTimeMillis();
    }

    public boolean handleUserInput(int position) {
        long timeSpent = (System.currentTimeMillis() - roundStartTime) / 1000;

        if (gameMode && timeSpent > timeLimitPerRound) {
            // Zeit abgelaufen
            lives = 0;
            return false;
        }

        boolean correct = gameBoard.checkPosition(position);
        scoreManager.recordMatch(correct, timeSpent);

        if (!correct) {
            lives--;
        }
        if (correct) {
            startNewRound();
        }

        return correct;
    }

    public boolean isGameOver() {
        if (gameMode) {
            long timeSpent = (System.currentTimeMillis() - roundStartTime) / 1000;
            return timeSpent > timeLimitPerRound || lives <= 0;
        } else {
            return lives <= 0;
        }
    }

    public GameState getCurrentGameState() {
        return new GameState(
                gameBoard.getFlatBoard(),
                gameBoard.getTargetCharacter(),
                lives,
                scoreManager.getCurrentScore(),
                scoreManager.getStreak(),
                gameMode
                        ? String.valueOf(Math.max(0, timeLimitPerRound - ((System.currentTimeMillis() - roundStartTime) / 1000)))
                        : ""
        );
    }


    private String getRemainingTimeFormatted() {
        long timeSpent = (System.currentTimeMillis() - roundStartTime) / 1000;
        long remaining = Math.max(0, timeLimitPerRound - timeSpent);
        return remaining + "s";
    }


    public void endGame() {
        System.out.println("Spiel ende");
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public String getTargetCharacter() {
        return gameBoard.getTargetCharacter();
    }

    public int getLives() {
        return lives;
    }

    public int getCurrentScore() {
        return scoreManager.getCurrentScore();
    }


    public int getStreak() {
        return scoreManager.getStreak();
    }
}
