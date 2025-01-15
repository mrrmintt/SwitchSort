package com.example.switchsort.frontend.backend.game;

public class GameManager {
    private final GameBoard gameBoard;
    private final String difficulty;
    private int currentRound;
    private final int timeLimit;

    public GameManager(String difficulty) {
        this.difficulty = difficulty;
        this.currentRound = 0;
        this.gameBoard = new GameBoard(GameConfig.getGridSize(difficulty));
        this.timeLimit = GameConfig.getTimeLimit(difficulty);
    }

    public void startNewRound() {
        currentRound++;
        gameBoard.generateNewBoard(difficulty);
    }

    public boolean checkSelection(int position) {
        return gameBoard.checkPosition(position);
    }

    public boolean isGameOver() {
        return currentRound >= GameConfig.ROUNDS_PER_GAME;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public char getTargetCharacter() {
        return gameBoard.getTargetCharacter();
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getGridSize() {
        return gameBoard.getSize();
    }
}