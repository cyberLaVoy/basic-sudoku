package com.example.cyberlavoy.sudoku;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SolvedActivity extends AppCompatActivity {

    private static final String EXTRA_SOLVED_TIME =
            "com.example.cyberlavoy.sudoku.solved_time";
    private static final String EXTRA_NEW_GAME_BUTTON_PRESSED =
            "com.example.cyberlavoy.sudoku.new_game_button_pressed";

    private String mSolvedTime;
    private TextView mSolvedTimeView;
    private Button mViewSolutionButton;
    private Button mNewGameButton;

    public static Intent newIntent(Context packageContext, String solved_time) {
        Intent intent = new Intent(packageContext, SolvedActivity.class);
        intent.putExtra(EXTRA_SOLVED_TIME, solved_time);
        return intent;
    }

    private void setNewGameButtonPressed(boolean new_game_button_pressed) {
        Intent data = new Intent();
        data.putExtra(EXTRA_NEW_GAME_BUTTON_PRESSED, new_game_button_pressed);
        setResult(RESULT_OK, data);
    }

    public static boolean newGameButtonPressed(Intent result) {
        return result.getBooleanExtra(EXTRA_NEW_GAME_BUTTON_PRESSED, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solved);

        mSolvedTimeView = findViewById(R.id.solved_time);
        mViewSolutionButton = findViewById(R.id.view_solution_button);
        mNewGameButton = findViewById(R.id.new_game_button_solved);

        mSolvedTime = getIntent().getStringExtra(EXTRA_SOLVED_TIME);
        mSolvedTimeView.setText(mSolvedTime);

        mViewSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewGameButtonPressed(true);
                finish();
            }
        });
    }

}
