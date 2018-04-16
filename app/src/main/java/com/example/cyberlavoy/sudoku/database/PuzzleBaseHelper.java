package com.example.cyberlavoy.sudoku.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CyberLaVoy on 4/10/2018.
 */

public class PuzzleBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "puzzleBase.db";

    public PuzzleBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PuzzleDbSchema.PuzzleTable.NAME + "(" +
                " _id integer PRIMARY KEY AUTOINCREMENT, " +
                PuzzleDbSchema.PuzzleTable.Cols.UUID + ", " +
                PuzzleDbSchema.PuzzleTable.Cols.BOARDLAYOUT + ", " +
                PuzzleDbSchema.PuzzleTable.Cols.TITLE + ", " +
                PuzzleDbSchema.PuzzleTable.Cols.SOLVED + ", " +
                PuzzleDbSchema.PuzzleTable.Cols.DATECREATED + ", " +
                PuzzleDbSchema.PuzzleTable.Cols.LASTPLAYED + ", " +
                PuzzleDbSchema.PuzzleTable.Cols.SECONDSPLAYED + ", " +
                PuzzleDbSchema.PuzzleTable.Cols.THUMBNAIL +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
    
