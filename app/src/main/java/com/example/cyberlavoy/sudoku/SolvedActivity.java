package com.example.cyberlavoy.sudoku;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SolvedActivity extends AppCompatActivity {

    private static final String EXTRA_SOLVED_TIME =
            "com.example.cyberlavoy.sudoku.solved_time";

    private String mSolvedTime;
    private TextView mSolvedTimeView;

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
        mSolvedTime = getIntent().getStringExtra(EXTRA_SOLVED_TIME);
        if (mSolvedTime != null) {
            mSolvedTimeView.setText(mSolvedTime);
        }
        else {
            mSolvedTimeView.setText("00:00:00");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_solved, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.solved_menu_button:
                Intent intent = MenuActivity.newIntent(SolvedActivity.this);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            case R.id.solved_back_button:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
