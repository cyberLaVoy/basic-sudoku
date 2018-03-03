package com.example.cyberlavoy.sudoku;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by CyberLaVoy on 2/25/2018.
 */

public class PuzzleFragment extends Fragment {
    private static final String ARG_PUZZLE_ID = "puzzle_id";

    private final int[] mSelectionButtonRefs = {R.id.button1, R.id.button2, R.id.button3,
                                                R.id.button4, R.id.button5, R.id.button6,
                                                R.id.button7, R.id.button8, R.id.button9,};
    private FrameLayout mPuzzleBoard;
    private SudokuBoard mSudokuBoard;
    private Button mSubmitButton;
    private ImageView mSolveButton;
    private ImageView mResetButton;
    private ImageView mMenuButton;
    private EditText mPuzzleTitle;
    private String mSolvedTime;
    private TextView mTimer;
    private int[] mFocusedCellPosition;
    private TextView mFocusedCellView;
    private int mContainerWidth;

    private int time_elapsed;
    private Timer timer = new Timer();
    private boolean timer_is_running = false;


    public static PuzzleFragment newInstance(UUID puzzleId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PUZZLE_ID, puzzleId);

        PuzzleFragment fragment = new PuzzleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        UUID puzzleId = (UUID) getArguments().getSerializable(ARG_PUZZLE_ID);
        mSudokuBoard = PuzzleBook.get(getActivity()).getSudokuBoard(puzzleId);
        mSudokuBoard.setLastPlayed();
        PuzzleBook.get(getActivity()).setLastPlayedPuzzle(puzzleId);
        time_elapsed = mSudokuBoard.getSecondsPlayed();
    }
    @Override
    public void onPause() {
        super.onPause();
        mSudokuBoard.setSecondsPlayed(time_elapsed);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_puzzle, container, false);

        DisplayMetrics displayMetrics = v.getResources().getDisplayMetrics();
        mContainerWidth =  displayMetrics.widthPixels;

        mPuzzleBoard = v.findViewById(R.id.puzzle_board);
        mSubmitButton = v.findViewById(R.id.submit_button);
        mSolveButton = v.findViewById(R.id.solve_button);
        mResetButton = v.findViewById(R.id.reset_button);
        mMenuButton = v.findViewById(R.id.puzzle_view_menu_button);
        mPuzzleTitle = v.findViewById(R.id.puzzle_title);
        mTimer = v.findViewById(R.id.timer);

        mSudokuBoard.makeTestBoard();
        createSudokuBoardView();

        mPuzzleTitle.setText(mSudokuBoard.getTitle());

        if (! timer_is_running) {
            startTimer();
        }


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isSolved = mSudokuBoard.isSolved();
               if (isSolved) {
                   Intent intent = SolvedActivity.newIntent(getActivity(), mSolvedTime);
                   startActivity(intent);
                   timer.cancel();
                   timer_is_running = false;
                   timer = new Timer();
               }
               else {
                   Toast toast = new Toast(getActivity());
                   toast.makeText(getActivity(), "Not quite.", Toast.LENGTH_SHORT).show();
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
                mSudokuBoard.testSolve();
                mPuzzleBoard.removeAllViews();
                createSudokuBoardView();
            }
        });
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSudokuBoard.clearBoard();
                mPuzzleBoard.removeAllViews();
                createSudokuBoardView();
            }
        });
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MenuActivity.newIntent(getActivity());
                startActivity(intent);
                getActivity().finish();
            }
        });
        mPuzzleTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mSudokuBoard.setTitle( mPuzzleTitle.getText().toString() );
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPuzzleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPuzzleTitle.setCursorVisible(true);
            }
        });



        for (int i = 0; i < 9; i++) {
            Button selectionButton = v.findViewById(mSelectionButtonRefs[i]);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) selectionButton.getLayoutParams();
            params.width = mContainerWidth/9 - 5;
            selectionButton.setLayoutParams(params);

            selectionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPuzzleTitle.setCursorVisible(false);
                    Button button = (Button) view;
                    String value = (String) button.getText();
                    mFocusedCellView.setText(value);
                    int x = mFocusedCellPosition[0];
                    int y = mFocusedCellPosition[1];
                    if (!mSudokuBoard.isValidEntry(x, y, value)) {
                        mFocusedCellView.setTextColor(Color.parseColor("#dd0000"));
                    }
                    else {
                        mFocusedCellView.setTextColor(Color.parseColor("#000000"));
                    }
                    mSudokuBoard.setBox(x,y,value + 'p');
                }
            });
        }

        return v;
    }


    public void createSudokuBoardView() {
         for(int i = 0; i <= 8; i++) {
            Drawable outer_border = getResources().getDrawable(R.drawable.outer_border);
            FrameLayout frame_layout = new FrameLayout(getActivity());
            float d = getActivity().getResources().getDisplayMetrics().density;
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
            for (int j = 0; j <=2; j++) {
                LinearLayout linear_layout = new LinearLayout(getActivity());
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
                    final Drawable innerBorder = getResources().getDrawable(R.drawable.inner_border);
                    final Drawable innerBorderSelected = getResources().getDrawable(R.drawable.inner_border_selected);
                    final int[] boxLocation = {i, j*3+h};
                    String value = mSudokuBoard.getBox(boxLocation[0], boxLocation[1]);

                    TextView text_view = new TextView(getActivity());
                    text_view.setTag(boxLocation);
                    if (value.charAt(1) != 's') {
                        text_view.setTextColor(Color.parseColor("#000000"));
                        text_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mPuzzleTitle.setCursorVisible(false);
                                if (mFocusedCellView != null) {
                                    mFocusedCellView.setBackground(innerBorder);
                                }
                                view.setBackground(innerBorderSelected);
                                mFocusedCellPosition = boxLocation;
                                mFocusedCellView = (TextView) view;
                            }
                        });
                    }
                    if (value.charAt(1) != 'n') {
                        text_view.setText(value.substring(0, 1));
                    }


                    text_view.setTextSize(getResources().getDimension(R.dimen.textSize));
                    text_view.setGravity(Gravity.CENTER);
                    text_view.setBackground(innerBorder);
                    ViewGroup.LayoutParams  params = new ViewGroup.LayoutParams((int)(35*d), (int)(35*d));
                    text_view.setLayoutParams(params);
                    linear_layout.addView(text_view);

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
