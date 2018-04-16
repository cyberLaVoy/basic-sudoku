package com.example.cyberlavoy.sudoku;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.cyberlavoy.sudoku.database.DbBitmapUtility.getBytes;

/**
 * Created by CyberLaVoy on 2/25/2018.
 */

public class PuzzleListFragment extends Fragment {
    private RecyclerView mPuzzleRecyclerView;
    private PuzzleAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puzzle_list, container, false);
        mPuzzleRecyclerView = (RecyclerView) view.findViewById(R.id.puzzle_recycler_view);
        mPuzzleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    private void updateUI() {
        PuzzleBook puzzleBook = PuzzleBook.get(getActivity());
        Map<UUID, SudokuBoard> sudoduPuzzles = puzzleBook.getSudokuBoards();
        if (mAdapter == null) {
            mAdapter = new PuzzleAdapter(sudoduPuzzles);
            mPuzzleRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setPuzzles(sudoduPuzzles);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class PuzzleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SudokuBoard mSudokuBoard;
        private TextView mDateCreated;
        private TextView mLastPlayed;
        private TextView mPuzzleTitle;
        private ImageView mStatusImageView;
        private ImageView mPuzzleThumbnail;

        private Drawable solvedIcon = getResources().getDrawable(R.drawable.ic_complete);
        private Drawable inProgressIcon = getResources().getDrawable(R.drawable.ic_not_complete);

        public PuzzleHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_puzzle, parent, false));
            mDateCreated = (TextView) itemView.findViewById(R.id.date_created);
            mLastPlayed = (TextView) itemView.findViewById(R.id.last_played);
            mPuzzleTitle = (TextView) itemView.findViewById(R.id.puzzle_title);
            mStatusImageView = (ImageView) itemView.findViewById(R.id.status_icon);
            mPuzzleThumbnail = (ImageView) itemView.findViewById(R.id.puzzle_thumbnail);
            itemView.setOnClickListener(this);
        }
        public void bind(SudokuBoard sudokuBoard) {
            mSudokuBoard = sudokuBoard;
            if (sudokuBoard.wasSolved()) {
                mStatusImageView.setBackground(solvedIcon);
            }
            else {
                mStatusImageView.setBackground(inProgressIcon);
            }
            mPuzzleTitle.setText(sudokuBoard.getTitle());
            String dateCreated = "Created: ";
            String lastPlayed = "Last Played: ";
            dateCreated += sudokuBoard.getDateCreated();
            lastPlayed += sudokuBoard.getLastPlayed();
            mDateCreated.setText(dateCreated);
            mLastPlayed.setText(lastPlayed);
            Bitmap thumbnail = mSudokuBoard.getThumbnail();
            if (thumbnail != null) {
                mPuzzleThumbnail.setImageBitmap(thumbnail);
                mPuzzleThumbnail.setRotation(90);
                mPuzzleThumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
        @Override
        public void onClick(View view) {
            Intent intent = PuzzleActivity.newIntent(getActivity(), mSudokuBoard.getId());
            startActivity(intent);
        }
    }


    private class PuzzleAdapter extends RecyclerView.Adapter<PuzzleHolder> {

        private Map<UUID, SudokuBoard> mSudokuPuzzles;
        private List<UUID> mPuzzleBookKeys;

        public PuzzleAdapter(Map<UUID, SudokuBoard> sudokuPuzzles) {
            mSudokuPuzzles = sudokuPuzzles;
            mPuzzleBookKeys = new ArrayList<>( mSudokuPuzzles.keySet() );
            Collections.reverse(mPuzzleBookKeys);
        }
        @Override
        public PuzzleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PuzzleHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PuzzleHolder holder, int position) {
            UUID key = mPuzzleBookKeys.get(position);
            SudokuBoard sudokuBoard = mSudokuPuzzles.get(key);
            holder.bind(sudokuBoard);
        }

        @Override
        public int getItemCount() {
            return mSudokuPuzzles.size();
        }

        public void setPuzzles(Map<UUID, SudokuBoard> puzzles) {
            mSudokuPuzzles = puzzles;
            mPuzzleBookKeys = new ArrayList<>( mSudokuPuzzles.keySet() );
            Collections.reverse(mPuzzleBookKeys);
        }
    }
}
