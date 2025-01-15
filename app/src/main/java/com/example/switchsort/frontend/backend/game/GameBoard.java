package com.example.switchsort.frontend.backend.game;

import com.example.switchsort.frontend.backend.utils.CharacterGenerator;

public class GameBoard {
    private final int size;
    private final char[][] board;
    private char targetCharacter;
    private int targetPosition;

    public GameBoard(int size) {
        this.size = size;
        this.board = new char[size][size];
    }

    public void generateNewBoard(String difficulty) {
        CharacterGenerator generator = new CharacterGenerator(difficulty);
        targetCharacter = generator.generateTarget();

        // Zufällige Position für das Zielzeichen
        targetPosition = (int) (Math.random() * (size * size));

        // Brett mit zufälligen Zeichen füllen
        int currentPosition = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currentPosition == targetPosition) {
                    board[i][j] = targetCharacter;
                } else {
                    board[i][j] = generator.generateRandom();
                }
                currentPosition++;
            }
        }
    }

    public char getCharacterAt(int position) {
        return board[position / size][position % size];
    }

    public char getTargetCharacter() {
        return targetCharacter;
    }

    public boolean checkPosition(int position) {
        return position == targetPosition;
    }

    public int getSize() {
        return size;
    }
}