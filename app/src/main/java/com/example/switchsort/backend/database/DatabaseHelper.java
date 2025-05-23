package com.example.switchsort.backend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "switchsort.db";
    private static final int DATABASE_VERSION = 2;  // Increased version number

    // Table name and columns
    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DEVICE_ID = "device_id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_GAME_MODE = "game_mode";  // New column

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_SCORES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DEVICE_ID + " TEXT, " +
                COLUMN_SCORE + " INTEGER, " +
                COLUMN_DIFFICULTY + " TEXT, " +
                COLUMN_GAME_MODE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the old table and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }
    public void addScore(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, player.getName());
        values.put(COLUMN_DEVICE_ID, player.getDeviceId());
        values.put(COLUMN_SCORE, player.getScore());
        values.put(COLUMN_DIFFICULTY, player.getDifficulty());
        values.put(COLUMN_GAME_MODE, player.getGameMode());  // Added game mode
        Cursor cursor = db.query(TABLE_SCORES,
                new String[]{COLUMN_SCORE},
                COLUMN_DEVICE_ID + " = ? AND " + COLUMN_DIFFICULTY + " = ? AND " + COLUMN_GAME_MODE + " = ?",
                new String[]{player.getDeviceId(), player.getDifficulty(), player.getGameMode()},
                null, null, null);
        if (cursor.moveToFirst()) {
            int existingScore = cursor.getInt(0);
            if (player.getScore() > existingScore) {
                // Update if new score is higher
                db.update(TABLE_SCORES, values,
                        COLUMN_DEVICE_ID + " = ? AND " + COLUMN_DIFFICULTY + " = ? AND " + COLUMN_GAME_MODE + " = ?",
                        new String[]{player.getDeviceId(), player.getDifficulty(), player.getGameMode()});
            }
        } else {
            // Insert new record
            db.insert(TABLE_SCORES, null, values);
        }

        cursor.close();
        db.close();
    }
    public List<Player> getTopScores(String difficulty, String gameMode, int limit) {
        List<Player> topPlayers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_SCORES +
                " WHERE " + COLUMN_DIFFICULTY + " = ? AND " + COLUMN_GAME_MODE + " = ?" +
                " ORDER BY " + COLUMN_SCORE + " DESC" +
                " LIMIT " + limit;

        Cursor cursor = db.rawQuery(query, new String[]{difficulty, gameMode});

        if (cursor.moveToFirst()) {
            do {
                Player player = new Player(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEVICE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GAME_MODE))
                );
                topPlayers.add(player);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return topPlayers;
    }
}