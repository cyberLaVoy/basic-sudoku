package com.example.cyberlavoy.sudoku;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by CyberLaVoy on 2/25/2018.
 */

public class PuzzleListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PuzzleListFragment();
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, PuzzleListActivity.class);
        return intent;
    }

}
