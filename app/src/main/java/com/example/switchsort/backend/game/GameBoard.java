package com.example.switchsort.backend.game;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
        RandomGenerator generator = new RandomGenerator(difficulty, mode);
        targetCharacter = generator.generateRandom();
        int totalCells = size * size;
        targetPosition = (int) (Math.random() * totalCells);

        // Versuche, eindeutige Werte zu sammeln (ohne targetCharacter)
        Set<String> uniqueValues = new HashSet<>();
        int maxAttempts = totalCells * 10; // Sicherheitslimit gegen Endlosschleife
        int attempts = 0;
        while (uniqueValues.size() < totalCells - 1 && attempts < maxAttempts) {
            String value = generator.generateRandom();
            if (!value.equals(targetCharacter)) {
                uniqueValues.add(value);
            }
            attempts++;
        }

        // Wenn nicht genug eindeutige Werte vorhanden, fülle mit Duplikaten auf
        List<String> fillValues = new ArrayList<>(uniqueValues);
        Random rnd = new Random();

        // Falls fillValues leer ist (extremer Fall), fülle mit targetCharacter als Fallback
        if (fillValues.isEmpty()) {
            fillValues.add(targetCharacter);
        }

        while (fillValues.size() < totalCells - 1) {
            // Nimm zufällige Elemente aus fillValues zum Auffüllen
            String filler = fillValues.get(rnd.nextInt(fillValues.size()));
            fillValues.add(filler);
        }

        Collections.shuffle(fillValues);

        // Board füllen
        int index = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (index == targetPosition) {
                    board[i][j] = targetCharacter;
                } else {
                    // index - 1, wenn index > targetPosition, sonst index
                    int fillIndex = (index < targetPosition) ? index : index - 1;
                    board[i][j] = fillValues.get(fillIndex);
                }
                index++;
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
        System.out.println("POSITION:"+ position);
        System.out.println("TARGETPOSITION:"+ targetPosition);
        return position == targetPosition;
    }

    public int getSize() {
        return size;
    }


}