package com.example.switchsort;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.switchsort.backend.game.GameBoard;

public class GameBoardTest {

    private GameBoard board;
    private final int size = 3;

    @Before
    public void setUp() {
        board = new GameBoard(size);
        board.generateNewBoard("EASY", "LETTER");
    }

    @Test
    public void testBoardIsCorrectSize() {
        String[] flat = board.getFlatBoard();
        assertEquals(size * size, flat.length);
    }

    @Test
    public void testTargetCharacterIsOnBoard() {
        String[] flat = board.getFlatBoard();
        String target = board.getTargetCharacter();
        boolean found = false;

        for (String cell : flat) {
            if (cell.equals(target)) {
                found = true;
                break;
            }
        }

        assertTrue("Target character should be present on board", found);
    }

    @Test
    public void testCharacterAtReturnsCorrectValue() {
        String[] flat = board.getFlatBoard();

        for (int i = 0; i < flat.length; i++) {
            assertEquals(flat[i], board.getCharacterAt(i));
        }
    }

    @Test
    public void testCheckPosition() {
        String[] flat = board.getFlatBoard();
        String target = board.getTargetCharacter();

        for (int i = 0; i < flat.length; i++) {
            if (flat[i].equals(target)) {
                assertTrue(board.checkPosition(i));
            } else {
                assertFalse(board.checkPosition(i));
            }
        }
    }

    @Test
    public void testTargetCharacterIsNotDuplicatedExcessively() {
        String[] flat = board.getFlatBoard();
        String target = board.getTargetCharacter();
        int count = 0;

        for (String cell : flat) {
            if (cell.equals(target)) count++;
        }

        // Sollte nur einmal vorkommen
        assertEquals("Target character should appear exactly once", 1, count);
    }
}
