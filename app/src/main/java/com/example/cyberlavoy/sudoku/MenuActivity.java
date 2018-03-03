package com.example.cyberlavoy.sudoku;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

/**
 * Created by CyberLaVoy on 2/26/2018.
 */

public class MenuActivity extends AppCompatActivity {

    private Button mResumeButton;
    private Button mNewGameButton;
    private Button mGameListButton;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MenuActivity.class);
        return intent;
    }


    @Override
    protected void onResume() {
        super.onResume();
        UUID lastPlayedId = PuzzleBook.get(MenuActivity.this).getLastPlayedPuzzle();
        if (lastPlayedId != null) {
            mResumeButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mResumeButton = findViewById(R.id.resume_button);
        mNewGameButton = findViewById(R.id.new_game_button);
        mGameListButton = findViewById(R.id.game_list_button);

        UUID lastPlayedId = PuzzleBook.get(MenuActivity.this).getLastPlayedPuzzle();
        if (lastPlayedId == null) {
            mResumeButton.setVisibility(View.GONE);
        }

        mGameListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PuzzleListActivity.newIntent(MenuActivity.this);
                startActivity(intent);
            }
        });
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               UUID puzzleId = PuzzleBook.get(MenuActivity.this).addNewPuzzle();
               Intent intent = PuzzleActivity.newIntent(MenuActivity.this, puzzleId);
               startActivity(intent);
            }
        });
        mResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               UUID lastPlayedId = PuzzleBook.get(MenuActivity.this).getLastPlayedPuzzle();
               if (lastPlayedId != null) {
                   Intent intent = PuzzleActivity.newIntent(MenuActivity.this, lastPlayedId);
                   startActivity(intent);
               }
            }
        });
    }
}
