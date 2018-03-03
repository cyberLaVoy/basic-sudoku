package com.example.cyberlavoy.sudoku;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class PuzzleActivity extends SingleFragmentActivity {

    private static final String EXTRA_PUZZLE_ID = "com.example.cyberlavoy.sudoku.puzzle_id";

    public static Intent newIntent(Context packageContext, UUID puzzleId) {
        Intent intent = new Intent(packageContext, PuzzleActivity.class);
        intent.putExtra(EXTRA_PUZZLE_ID, puzzleId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID puzzleId = (UUID) getIntent().getSerializableExtra(EXTRA_PUZZLE_ID);
        return PuzzleFragment.newInstance(puzzleId);
    }
}
