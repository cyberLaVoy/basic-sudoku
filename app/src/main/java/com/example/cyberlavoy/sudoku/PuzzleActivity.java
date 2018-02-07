package com.example.cyberlavoy.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class PuzzleActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NEW_GAME = 0;

    private FrameLayout mPuzzleBoard;
    private SudokuBoard mSudokuBoard;
    private Button mSubmitButton;
    private Button mSolveButton;
    private Button mResetButton;
    private Button mNewGameButton;
    private String mSolvedTime;
    private TextView mTimer;

    private int time_elapsed = -1;
    private Timer timer = new Timer();
    private boolean timer_is_running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        mPuzzleBoard = findViewById(R.id.puzzle_board);
        mSubmitButton = findViewById(R.id.submit_button);
        mSolveButton = findViewById(R.id.solve_button);
        mResetButton = findViewById(R.id.reset_button);
        mNewGameButton = findViewById(R.id.new_game_button);
        mTimer = findViewById(R.id.timer);
        mSudokuBoard = new SudokuBoard(9);
        mSudokuBoard.makeTestBoard();
        createSudokuBoardView();

        if (! timer_is_running) {
            startTimer();
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSolved = mSudokuBoard.isSolved();
               if (isSolved) {
                   Intent intent = SolvedActivity.newIntent(PuzzleActivity.this, mSolvedTime);
                   startActivityForResult(intent, REQUEST_CODE_NEW_GAME);
                   timer.cancel();
                   timer_is_running = false;
                   timer = new Timer();
               }
               else {
                   Toast toast = new Toast(PuzzleActivity.this);
                   toast.makeText(PuzzleActivity.this, "Not quite.", Toast.LENGTH_SHORT).show();
               }
            }
        });
        mSolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timer_is_running = false;
                clearTime();
                timer = new Timer();
                mSudokuBoard = mSudokuBoard.getTestSolution();
                mPuzzleBoard.removeAllViews();
                createSudokuBoardView();
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSudokuBoard.clearBoard();
                mSudokuBoard.makeTestBoard();
                mPuzzleBoard.removeAllViews();
                createSudokuBoardView();
            }
        });
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTime();
                mSudokuBoard = new SudokuBoard(9);
                mSudokuBoard.makeTestBoard();
                mPuzzleBoard.removeAllViews();
                createSudokuBoardView();
                timer.cancel();
                timer = new Timer();
                startTimer();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_NEW_GAME) {
            if (data == null) {
                return;
            }
            if ( SolvedActivity.newGameButtonPressed(data) ) {
               mNewGameButton.performClick();
            }
        }
    }

    public void createSudokuBoardView() {
         for(int i = 0; i <= 8; i++) {
            Drawable outer_border = getResources().getDrawable(R.drawable.outer_border);
            FrameLayout frame_layout = new FrameLayout(PuzzleActivity.this);
            float d = PuzzleActivity.this.getResources().getDisplayMetrics().density;
            FrameLayout.LayoutParams frame_parameters = new FrameLayout.LayoutParams((int)(109*d), (int)(109*d));
            int first_gravity = Gravity.TOP;
            int second_gravity = Gravity.LEFT;
            if ( i >= 3 && i <= 5) {
                first_gravity = Gravity.CENTER_VERTICAL;
            }
            if ( i >= 6) {
                first_gravity = Gravity.BOTTOM;
            }
            if ( i % 3 == 1) {
                second_gravity = Gravity.CENTER_HORIZONTAL;
            }
            if ( i % 3 == 2) {
                second_gravity = Gravity.RIGHT;
            }
            frame_parameters.gravity = first_gravity | second_gravity;
            frame_layout.setBackground(outer_border);
            mPuzzleBoard.addView(frame_layout, frame_parameters);
            int box_number = 0;
            for (int j = 0; j <=2; j++) {
                LinearLayout linear_layout = new LinearLayout(PuzzleActivity.this);
                int gravity = Gravity.TOP;
                if (j == 1) {
                    gravity = Gravity.CENTER_VERTICAL;
                }
                if (j == 2) {
                    gravity = Gravity.BOTTOM;
                }
                FrameLayout.LayoutParams layout_parameters = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                layout_parameters.gravity = gravity;
                linear_layout.setLayoutParams(layout_parameters);
                frame_layout.addView(linear_layout, layout_parameters);

                for (int h = 0; h <= 2; h++) {
                    Drawable inner_border = getResources().getDrawable(R.drawable.inner_border);
                    int value = mSudokuBoard.getBox(i, j*3+h);

                    if (value != 0) {
                        TextView text_view = new TextView(PuzzleActivity.this);

                        String box_value = Integer.toString(value);
                        text_view.setText(box_value);

                        text_view.setTextSize(getResources().getDimension(R.dimen.textSize));
                        text_view.setGravity(Gravity.CENTER);
                        text_view.setBackground(inner_border);
                        ViewGroup.LayoutParams  params = new ViewGroup.LayoutParams((int)(35*d), (int)(35*d));
                        text_view.setLayoutParams(params);
                        linear_layout.addView(text_view);
                    }
                    else {
                        EditText edit_text = new EditText(PuzzleActivity.this);

                        edit_text.setTag(box_number); //used for event listener
                        edit_text.setTextSize(getResources().getDimension(R.dimen.textSize));
                        edit_text.setGravity(Gravity.CENTER);
                        edit_text.setBackground(inner_border);
                        ViewGroup.LayoutParams  params = new ViewGroup.LayoutParams((int)(35*d), (int)(35*d));
                        edit_text.setLayoutParams(params);
                        linear_layout.addView(edit_text);
                    }
                    box_number++;
                }
            }
        }
    }


    private void startTimer() {
        timer_is_running = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                time_elapsed += 1;
                handler.obtainMessage(1).sendToTarget();
            }
        }, 0, 1000);
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mTimer.setText( formatTime(time_elapsed) );
        }
    };

    String formatTime(int time_in_seconds) {
       String formatted = new String();
       int hours = time_in_seconds/3600;
       int minutes = time_in_seconds/60;
       int seconds = time_in_seconds%60;
       String h = Integer.toString(hours);
       String m = Integer.toString(minutes);
       String s = Integer.toString(seconds);
       if (h.length() < 2) {
          h = "0" + h;
       }
       if (m.length() < 2) {
          m = "0" + m;
       }
        if (s.length() < 2) {
          s = "0" + s;
       }
       formatted = h + ":" + m + ":" + s;
       mSolvedTime = formatted;
       return formatted;
    }

    void clearTime() {
        time_elapsed = -1;
        mSolvedTime = "00:00:00";
        mTimer.setText("00:00:00");
    }
}
