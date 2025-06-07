package com.example.switchsort;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

import com.example.switchsort.backend.database.DatabaseHelper;
import com.example.switchsort.backend.database.Player;

@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

    private DatabaseHelper dbHelper;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase("switchsort.db"); // Wichtig: alte, fehlerhafte DB löschen

        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // → ruft intern onCreate() auf, falls noch nicht
        db.close();
    }

    @After
    public void tearDown() {
        context.deleteDatabase("switchsort.db"); // sauber aufräumen
    }

    @Test
    public void testAddScore_createsEntry() {
        Player p = new Player("Test", "device123", 100, "EASY", "CLASSIC");
        dbHelper.addScore(p);

        List<Player> topScores = dbHelper.getTopScores("EASY", "CLASSIC", 10);
        assertEquals(1, topScores.size());
        assertEquals("Test", topScores.get(0).getName());
    }
}
