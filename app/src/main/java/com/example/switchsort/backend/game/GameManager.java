package com.example.switchsort.backend.game;

public class GameManager {
    private static final float TIME_BONUS = 0.5f;
    private final String difficulty;
    private final String mode;
    private final boolean gameMode; // "CLASSIC" oder "TIMERUSH"
    private final int maxLives = 3;
    private int lives;
    private final GameBoard gameBoard;
    private final ScoreManager scoreManager;
    private long roundStartTime;
    private float currentTime;
    private final int timeLimitPerRound; // in Sekunden für TimeRush
    private boolean isGameOver;
    private static final int INITIAL_TIME = 60;

    public GameManager(String difficulty, String mode, boolean gameMode) {
        this.difficulty = difficulty.toUpperCase();
        this.mode = mode.toUpperCase(); // "LETTER" oder "NUMBER"
        this.gameMode = gameMode; // "CLASSIC" oder "TIMERUSH"
        this.lives = maxLives;
        int gridSize = GameConfig.getGridSize(difficulty);
        this.timeLimitPerRound = GameConfig.getTimeLimit(difficulty);
        this.gameBoard = new GameBoard(gridSize);
        this.scoreManager = new ScoreManager(difficulty, gameMode);
        this.currentTime = INITIAL_TIME;
        startNewRound();
    }

    public void startNewRound() {
        if (isGameOver()) return;
        System.out.println("Test1");
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
            if (gameMode){
                addTimeBonus();
            }
            startNewRound();
        }

        return correct;
    }
    public void addTimeBonus() {
        currentTime += TIME_BONUS;
    }

    public boolean isGameOver() {
        if (gameMode) {
            return lives <=0;
        } else {
            return lives <= 0;
        }
    }

    public void updateTimer() {
        currentTime -= 0.1;
        if (currentTime <= 0) {
            currentTime = 0;
            isGameOver = true;
        }
    }
    public GameState getCurrentGameState() {
        System.out.println("CHARACTER:"+ gameBoard.getTargetCharacter());
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

    public String getCurrentTimeFormatted() {
        return String.format("%.1f", currentTime);
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
