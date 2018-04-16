package com.example.cyberlavoy.sudoku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cyberlavoy.sudoku.database.PuzzleBaseHelper;
import com.example.cyberlavoy.sudoku.database.PuzzleCursorWrapper;
import com.example.cyberlavoy.sudoku.database.PuzzleDbSchema;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.cyberlavoy.sudoku.database.DbBitmapUtility.getBytes;

/**
 * Created by CyberLaVoy on 2/25/2018.
 */

public class PuzzleBook {
    private static PuzzleBook sPuzzleBook;

    private UUID mLastPlayedPuzzleId;
    private UUID mLastCreatedPuzzleId;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PuzzleBook get(Context context) {
        if (sPuzzleBook == null) {
            sPuzzleBook = new PuzzleBook(context);
        }
        return sPuzzleBook;
    }

    private PuzzleBook(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PuzzleBaseHelper(mContext).getWritableDatabase();
    }

    public Map<UUID, SudokuBoard> getSudokuBoards() {
        Map<UUID, SudokuBoard> puzzles = new LinkedHashMap<>();

        PuzzleCursorWrapper cursor = queryPuzzles(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                SudokuBoard sudokuBoard = cursor.getPuzzle();
                puzzles.put(sudokuBoard.getId(), sudokuBoard);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return puzzles;
    }

    public SudokuBoard getSudokuBoard(UUID id) {
        PuzzleCursorWrapper cursor = queryPuzzles(
                PuzzleDbSchema.PuzzleTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPuzzle();
        } finally {
            cursor.close();
        }
    }

    public UUID getLastPlayedPuzzle() {
        return mLastPlayedPuzzleId;
    }

    public void setLastPlayedPuzzle(UUID id) {
        mLastPlayedPuzzleId = id;
    }


    public UUID getLastCreatedPuzzleId() {
        return mLastCreatedPuzzleId;
    }

    public void setLastCreatedPuzzleId(UUID lastCreatedPuzzleId) {
        mLastCreatedPuzzleId = lastCreatedPuzzleId;
    }

// database operations
    public UUID addNewPuzzle() {
        SudokuBoard sudokuBoard = new SudokuBoard();
        UUID boardId = sudokuBoard.getId();
        mLastCreatedPuzzleId = boardId;
        ContentValues values = getContentValues(sudokuBoard);
        mDatabase.insert(PuzzleDbSchema.PuzzleTable.NAME, null, values);
        return boardId;
    }
    public void updatePuzzle(SudokuBoard sudokuBoard) {
        String uuidString = sudokuBoard.getId().toString();
        ContentValues values = getContentValues(sudokuBoard);

        mDatabase.update(PuzzleDbSchema.PuzzleTable.NAME, values,
                PuzzleDbSchema.PuzzleTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }
    public void deletePuzzle(SudokuBoard sudokuBoard) {
        String uuidString = sudokuBoard.getId().toString();
        mDatabase.delete(PuzzleDbSchema.PuzzleTable.NAME,
                PuzzleDbSchema.PuzzleTable.Cols.UUID + " = ?",
                new String[] { uuidString });
        mLastPlayedPuzzleId = null;
    }

    private PuzzleCursorWrapper queryPuzzles(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PuzzleDbSchema.PuzzleTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new PuzzleCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(SudokuBoard sudokuBoard) {
        ContentValues values = new ContentValues();
        values.put(PuzzleDbSchema.PuzzleTable.Cols.UUID, sudokuBoard.getId().toString());
        values.put(PuzzleDbSchema.PuzzleTable.Cols.BOARDLAYOUT, sudokuBoard.getBoardLayout());
        values.put(PuzzleDbSchema.PuzzleTable.Cols.TITLE, sudokuBoard.getTitle());
        values.put(PuzzleDbSchema.PuzzleTable.Cols.SOLVED, sudokuBoard.isSolved() ? 1 : 0);
        values.put(PuzzleDbSchema.PuzzleTable.Cols.DATECREATED, sudokuBoard.getDateCreated());
        values.put(PuzzleDbSchema.PuzzleTable.Cols.LASTPLAYED, sudokuBoard.getLastPlayed());
        values.put(PuzzleDbSchema.PuzzleTable.Cols.SECONDSPLAYED, sudokuBoard.getSecondsPlayed());
        values.put(PuzzleDbSchema.PuzzleTable.Cols.THUMBNAIL, getBytes(sudokuBoard.getThumbnail()));
        return values;
    }

//Fill operations
    public File getPhotoFile(SudokuBoard sudokuBoard) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, sudokuBoard.getPhotoFilename());
    }

}
