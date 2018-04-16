package com.example.cyberlavoy.sudoku;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cyberlavoy.sudoku.api.PuzzleFetch;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by CyberLaVoy on 2/26/2018.
 */

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";
    private static final String KEY_PICTURE_TAKEN = "picture_taken";
    private static final int REQUEST_PHOTO = 2;
    private static boolean mPictureTaken = false;
    private boolean mReadyToTakePicture = false;

    private ImageView mPictureButton;
    private Button mResumeButton;
    private Button mGameListButton;
    private ProgressDialog mDialog;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MenuActivity.class);
        return intent;
    }
    public static void setPictureTaken(boolean taken) {
        mPictureTaken = taken;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.d(TAG, "On create.");

        if (savedInstanceState != null) {
            mPictureTaken = savedInstanceState.getBoolean(KEY_PICTURE_TAKEN);
        }
        mDialog = new ProgressDialog(MenuActivity.this);
        mDialog.setMessage("Analyzing puzzle, please wait...");
        mDialog.setCanceledOnTouchOutside(false);

        mPictureButton = findViewById(R.id.picture_button);
        mResumeButton = findViewById(R.id.resume_button);
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

//Picture operations
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID puzzleId = PuzzleBook.get(MenuActivity.this).addNewPuzzle();
                SudokuBoard sudokuBoard = PuzzleBook.get(MenuActivity.this).getSudokuBoard(puzzleId);
                File photoFile = PuzzleBook.get(MenuActivity.this).getPhotoFile(sudokuBoard);
                Uri uri = FileProvider.getUriForFile(MenuActivity.this, "com.example.cyberlavoy.sudoku.fileprovider", photoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                mReadyToTakePicture = true;
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "On start.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "On activity result.");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_PHOTO) {
            mPictureTaken = true;
            UUID puzzleId = PuzzleBook.get(MenuActivity.this).getLastCreatedPuzzleId();
            new FetchPuzzleTask(MenuActivity.this).execute(puzzleId);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "On resume.");
        if (mPictureTaken) {
            mDialog.show();
        }
        if (mReadyToTakePicture && !mPictureTaken) {
           UUID lastCreatedId = PuzzleBook.get(MenuActivity.this).getLastCreatedPuzzleId();
           SudokuBoard sudokuBoard = PuzzleBook.get(MenuActivity.this).getSudokuBoard(lastCreatedId);
           if (sudokuBoard != null) {
                PuzzleBook.get(MenuActivity.this).deletePuzzle(sudokuBoard);
           }
           mReadyToTakePicture = false;
        }
        UUID lastPlayedId = PuzzleBook.get(MenuActivity.this).getLastPlayedPuzzle();
        if (lastPlayedId != null) {
            mResumeButton.setVisibility(View.VISIBLE);
        }
        else {
            mResumeButton.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "On pause.");

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(KEY_PICTURE_TAKEN, mPictureTaken);
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "On stop.");
        mDialog.dismiss();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "On destroy.");
    }

    private class FetchPuzzleTask extends AsyncTask<UUID,Void,Void> {
        private Context mContext;
        FetchPuzzleTask(Context context) {
            mContext = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(UUID... params) {
            UUID puzzleId = params[0];
            SudokuBoard sudokuBoard = PuzzleBook.get(mContext).getSudokuBoard(puzzleId);
            File puzzleImage = PuzzleBook.get(mContext).getPhotoFile(sudokuBoard);
            Uri uri = FileProvider.getUriForFile(mContext, "com.example.cyberlavoy.sudoku.fileprovider", puzzleImage);
            revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            String boardLayout = new PuzzleFetch().fetchBoardLayout(puzzleImage);
            sudokuBoard.setBoardLayout(boardLayout);
            sudokuBoard.setThumbnail(mContext);
            PuzzleBook.get(mContext).updatePuzzle(sudokuBoard);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UUID puzzleId = PuzzleBook.get(mContext).getLastCreatedPuzzleId();
            Intent intent = PuzzleActivity.newIntent(mContext, puzzleId);
            MenuActivity.setPictureTaken(false);
            startActivity(intent);
        }
    }
}
