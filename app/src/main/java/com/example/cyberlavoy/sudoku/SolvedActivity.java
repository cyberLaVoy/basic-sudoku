package com.example.cyberlavoy.sudoku;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SolvedActivity extends AppCompatActivity {

    private static final String EXTRA_SOLVED_TIME =
            "com.example.cyberlavoy.sudoku.solved_time";

    private String mSolvedTime;
    private TextView mSolvedTimeView;
    private FrameLayout mBackButton;
    private FrameLayout mMenuButton;

    public static Intent newIntent(Context packageContext, String solved_time) {
        Intent intent = new Intent(packageContext, SolvedActivity.class);
        intent.putExtra(EXTRA_SOLVED_TIME, solved_time);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solved);

        mSolvedTimeView = findViewById(R.id.solved_time);
        mBackButton = findViewById(R.id.solved_view_back_button);
        mMenuButton = findViewById(R.id.solved_view_menu_button);

        mSolvedTime = getIntent().getStringExtra(EXTRA_SOLVED_TIME);
        mSolvedTimeView.setText(mSolvedTime);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MenuActivity.newIntent(SolvedActivity.this);
                startActivity(intent);
                finish();
            }
        });

    }

}
