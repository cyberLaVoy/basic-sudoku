package com.example.cyberlavoy.sudoku;

import android.content.Context;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by CyberLaVoy on 2/25/2018.
 */

public class PuzzleBook {
    private static PuzzleBook sPuzzleBook;
    private UUID mLastPlayedPuzzleId;
    private Map<UUID, SudokuBoard> mSudokuBoards;

    public static PuzzleBook get(Context context) {
        if (sPuzzleBook == null) {
            sPuzzleBook = new PuzzleBook(context);
        }
        return sPuzzleBook;
    }

    private PuzzleBook(Context context) {
        if (mSudokuBoards == null) {
            mSudokuBoards = new LinkedHashMap<>();
            for (int i = 0; i < 100; i++) {
                SudokuBoard sudokuBoard = new SudokuBoard();
                sudokuBoard.setTitle("Puzzle #" + Integer.toString(i));
                UUID boardId = sudokuBoard.getId();
                mSudokuBoards.put(boardId, sudokuBoard);
            }
        }
    }

    public Map<UUID, SudokuBoard> getSudokuBoards() {
        return mSudokuBoards;
    }

    public SudokuBoard getSudokuBoard(UUID id) {
        return mSudokuBoards.get(id);
    }

    public UUID getLastPlayedPuzzle() {
        return mLastPlayedPuzzleId;
    }

    public void setLastPlayedPuzzle(UUID id) {
        mLastPlayedPuzzleId = id;
    }

    public UUID addNewPuzzle() {
        SudokuBoard sudokuBoard = new SudokuBoard();
        sudokuBoard.setTitle("Sudoku Puzzle");
        UUID boardId = sudokuBoard.getId();
        mSudokuBoards.put(boardId, sudokuBoard);
        return boardId;
    }
}
