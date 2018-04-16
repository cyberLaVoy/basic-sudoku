package com.example.cyberlavoy.sudoku.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.example.cyberlavoy.sudoku.SudokuBoard;

import java.util.Date;
import java.util.UUID;

import static com.example.cyberlavoy.sudoku.database.DbBitmapUtility.getImage;

/**
 * Created by CyberLaVoy on 4/10/2018.
 */

public class PuzzleCursorWrapper extends CursorWrapper {
    public PuzzleCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public SudokuBoard getPuzzle() {
        String uuidString = getString(getColumnIndex(PuzzleDbSchema.PuzzleTable.Cols.UUID));
        String boardLayout = getString(getColumnIndex(PuzzleDbSchema.PuzzleTable.Cols.BOARDLAYOUT));
        String title = getString(getColumnIndex(PuzzleDbSchema.PuzzleTable.Cols.TITLE));
        int isSolved = getInt(getColumnIndex(PuzzleDbSchema.PuzzleTable.Cols.SOLVED));
        String dateCreated = getString(getColumnIndex(PuzzleDbSchema.PuzzleTable.Cols.DATECREATED));
        String lastPlayed = getString(getColumnIndex(PuzzleDbSchema.PuzzleTable.Cols.LASTPLAYED));
        int secondsPlayed = getInt(getColumnIndex(PuzzleDbSchema.PuzzleTable.Cols.SECONDSPLAYED));
        byte[] thumbnail = getBlob(getColumnIndex(PuzzleDbSchema.PuzzleTable.Cols.THUMBNAIL));

        SudokuBoard Puzzle = new SudokuBoard(UUID.fromString(uuidString));
        Puzzle.setBoardLayout(boardLayout);
        Puzzle.setTitle(title);
        Puzzle.setSolved(isSolved != 0);
        Puzzle.setDateCreated(dateCreated);
        Puzzle.setLastPlayed(lastPlayed);
        Puzzle.setSecondsPlayed(secondsPlayed);
        Puzzle.setThumbnail(getImage(thumbnail));

        return Puzzle;
    }
}
