package com.example.switchsort;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import com.example.switchsort.backend.game.RandomGenerator;

public class RandomGeneratorTest {

    private RandomGenerator easyLetter;
    private RandomGenerator mediumNumber;
    private RandomGenerator hardNumber;

    @Before
    public void setup() {
        easyLetter = new RandomGenerator("EASY", "LETTER");
        mediumNumber = new RandomGenerator("MEDIUM", "NUMBER");
        hardNumber = new RandomGenerator("HARD", "NUMBER");
    }

    @Test
    public void testGenerateLetter_Easy() {
        String result = easyLetter.generateLetter();
        assertNotNull(result);
        assertEquals(1, result.length());
        assertTrue("Must be Latin letter", result.matches("[A-Z]"));
    }

    @Test
    public void testGenerateLetter_Medium() {
        RandomGenerator rg = new RandomGenerator("MEDIUM", "LETTER");
        String result = rg.generateLetter();
        assertNotNull(result);
        assertEquals(1, result.length());
        assertTrue("Must be Greek letter", result.matches("[Α-Ω]"));
    }

    @Test
    public void testGenerateLetter_Hard() {
        RandomGenerator rg = new RandomGenerator("HARD", "LETTER");
        String result = rg.generateLetter();
        assertNotNull(result);
        assertEquals(1, result.length());
        assertTrue("Must be Arabic letter", "أبتثجحخدذرزسشصضطظعغفقكلمنهوي".contains(result));
    }

    @Test
    public void testGenerateNumber_Easy() {
        String number = easyLetter.generateNumber();
        assertNotNull(number);
        assertTrue("Must be numeric", number.matches("\\d+"));
        int val = Integer.parseInt(number);
        assertTrue("Must be between 0 and 20", val >= 0 && val <= 20);
    }

    @Test
    public void testGenerateNumber_Medium() {
        for (int i = 0; i < 100; i++) {
            String result = mediumNumber.generateNumber();
            assertNotNull(result);
            assertTrue("Must be Roman numeral or '0'", result.equals("0") || result.matches("^(I|II|III|IV|V|VI|VII|VIII|IX|X|XI|XII|XIII|XIV|XV|XVI|XVII|XVIII|XIX|XX)$"));
        }
    }

    @Test
    public void testGenerateNumber_Hard() {
        for (int i = 0; i < 100; i++) {
            String result = hardNumber.generateNumber();
            assertNotNull(result);
            assertTrue("Must be hex number", result.matches("^[0-9A-F]+$"));
            int decimal = Integer.parseInt(result, 16);
            assertTrue("Value must be between 0 and 20", decimal >= 0 && decimal <= 20);
        }
    }

    @Test
    public void testGenerateRandom_LetterMode() {
        String result = easyLetter.generateRandom();
        assertNotNull(result);
        assertEquals(1, result.length());
    }

    @Test
    public void testGenerateRandom_NumberMode() {
        String result = mediumNumber.generateRandom();
        assertNotNull(result);
        // Roman numerals or "0"
        assertTrue(result.matches("0|I{1,3}|IV|V|VI{0,3}|IX|X{1,2}|XI|XII|XIII|XIV|XV|XVI|XVII|XVIII|XIX|XX"));
    }

    @Test
    public void testConvertLatinToNumber() {
        assertEquals(1, RandomGenerator.convertLatinToNumber("I"));
        assertEquals(5, RandomGenerator.convertLatinToNumber("V"));
        assertEquals(10, RandomGenerator.convertLatinToNumber("X"));
        assertEquals(20, RandomGenerator.convertLatinToNumber("XX"));
        assertEquals(0, RandomGenerator.convertLatinToNumber("INVALID"));
    }

    @Test
    public void testGenerateTargetNumberHex() {
        String hex = "A";
        String expected = "10";
        assertEquals(expected, hardNumber.generateTargetNumberHex(hex));
    }

    @Test
    public void testGenerateRandomNumberBounds() {
        for (int i = 0; i < 100; i++) {
            int num = easyLetter.generateRandomNumber();
            assertTrue("Number must be between 0 and 20", num >= 0 && num <= 20);
        }
    }
}
