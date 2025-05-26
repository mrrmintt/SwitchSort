package com.example.switchsort.backend.game;



public class GameBoard {
    private final int size;
    private final String[][] board;
    private String targetCharacter;
    private int targetPosition;

    public GameBoard(int size) {
        this.size = size;
        this.board = new String[size][size];
    }
    public void generateNewBoard(String difficulty, String mode) {
        System.out.println("Board");
        RandomGenerator generator = new RandomGenerator(difficulty, mode);
        System.out.println("TEst generator");
        targetCharacter = generator.generateRandom();
        System.out.println("TEst generator"+ targetCharacter);
        // Zufällige Position für das Zielzeichen
        targetPosition = (int) (Math.random() * (size * size));
        // Brett mit zufälligen Zeichen füllen
        int currentPosition = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currentPosition == targetPosition) {
                    board[i][j] = targetCharacter;
                } else {
                    String value;
                    do {
                        value = generator.generateRandom();
                    } while (value.equals(targetCharacter));
                    board[i][j] = value;
                }
                currentPosition++;
            }
        }
    }

    public String[] getFlatBoard() {
        String[] flat = new String[size * size];
        int index = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                flat[index++] = board[i][j];
            }
        }
        return flat;
    }

    public String getCharacterAt(int position) {
        return board[position / size][position % size];
    }

    public String getTargetCharacter() {
        return targetCharacter;
    }

    public boolean checkPosition(int position) {
        return position == targetPosition;
    }

    public int getSize() {
        return size;
    }


}