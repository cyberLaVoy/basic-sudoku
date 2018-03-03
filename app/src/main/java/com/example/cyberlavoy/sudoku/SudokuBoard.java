package com.example.cyberlavoy.sudoku;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SudokuBoard {

        private UUID mId;
        private String mTitle;
        private String[][] mBoard;
        private boolean mWasSolved;
        private String mDateCreated;
        private String mLastPlayed;
        private int mSecondsPlayed;
        private int mSize = 9;

        SudokuBoard() {
                mBoard = new String[mSize][mSize];
                for (int i = 0; i <=8 ; i++) {
                        for (int j = 0; j <=8 ; j++) {
                            mBoard[i][j] = "0n";
                        }
                }
                mSecondsPlayed = 0;
                mWasSolved = false;
                mId = UUID.randomUUID();
                String pattern = "MM-dd-yy hh:mm:ss a";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                mDateCreated = simpleDateFormat.format(new Date());
                mLastPlayed = simpleDateFormat.format(new Date());
        }

        public void clearBoard() {
            for (int i = 0; i <=8 ; i++) {
                for (int j = 0; j <=8 ; j++) {
                    if (mBoard[i][j].charAt(1) != 's') {
                            mBoard[i][j] = "0n";
                    }
                }
            }
        }

        public UUID getId() {
                return mId;
        }
        public String getDateCreated() {
                return mDateCreated;
        }
            public String getTitle() {
        return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public String getLastPlayed() {
                return mLastPlayed;
        }
        public boolean wasSolved() { return mWasSolved; }

        public void setLastPlayed() {
            String pattern = "MM-dd-yy hh:mm:ss a";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            mLastPlayed = simpleDateFormat.format(new Date());
        }

        public int getSecondsPlayed() {
                return mSecondsPlayed;
        }
        public void setSecondsPlayed(int secondsPlayed) {
                mSecondsPlayed = secondsPlayed;
        }

        public String getBox(int outer_location, int inner_location) {
                if (outer_location < mSize && inner_location < mSize) {
                        return mBoard[outer_location][inner_location];
                }
                else {
                        return null;
                }
        }
        public boolean setBox(int outer_location, int inner_location, String value) {
                mBoard[outer_location][inner_location] = value;
                if (isValidEntry(outer_location, inner_location, value)) {
                        return true;
                }
                return false;
        }
        public boolean isSolved() {
                boolean isSolved = true;
                for (int i = 0; i < mSize; i++) {
                        for (int j = 0; j < mSize; j++) {
                                if( ! isValidEntry(i, j, mBoard[i][j]) ) {
                                    return false;
                                }
                        }
                }
                return isSolved;
        }
        public boolean isValidEntry(int outer_location, int inner_location, String value) {
                value = value.substring(0,1);
                boolean isValid;
                for (int i = 0; i < mSize; i++) {
                        if (value.equals(mBoard[outer_location][i].substring(0,1)) && i != inner_location) {
                                return false;
                        }
                }
                isValid = isValidEntryHorizontal(outer_location,inner_location,value);
                if (!isValid) {
                        return false;
                }
                isValid = isValidEntryVertical(outer_location, inner_location, value);
                if (!isValid) {
                        return false;
                }
                return true;
        }
        public boolean isValidEntryHorizontal(int outer_location, int inner_location, String value) {
                int outer_iterator;
                int inner_iterator;
                if (outer_location >= 6) {
                        outer_iterator = 6;
                }
                else if (outer_location >= 3 && outer_location <= 5) {
                        outer_iterator = 3;
                }
                else {
                        outer_iterator = 0;
                }
                if (inner_location >= 6) {
                        inner_iterator = 6;
                }
                else if (inner_location >= 3 && inner_location <= 5) {
                        inner_iterator = 3;
                }
                else {
                        inner_iterator = 0;
                }
                for (int i = outer_iterator; i < outer_iterator+3; i++) {
                        for (int j = inner_iterator; j < inner_iterator+3; j++) {
                                if (value.equals(mBoard[i][j].substring(0,1)) && j != inner_location && i != outer_location) {
                                        return false;
                                }
                        }
                }
                return true;
        }
        public boolean isValidEntryVertical(int outer_location, int inner_location, String value) {
                int outer_iterator = outer_location % 3;
                int inner_iterator = inner_location % 3;
                for (int i = outer_iterator; i <= outer_iterator+6; i+=3) {
                       for (int j = inner_iterator; j <= inner_iterator+6; j+=3) {
                               if (value.equals(mBoard[i][j].substring(0,1)) && j != inner_location && j != outer_location) {
                                       return false;
                               }
                       }
                }
                return true;
        }


        public void makeTestBoard() {
                setBox(0,0,"2s");
                setBox(0,2,"7s");
                setBox(0,6,"1s");
                setBox(0,8,"4s");

                setBox(1,1,"3s");
                setBox(1,2,"9s");
                setBox(1,3,"1s");
                setBox(1,5,"2s");

                setBox(2,2,"6s");
                setBox(2,3,"4s");
                setBox(2,5,"8s");
                setBox(2,6,"7s");

                setBox(3,1,"6s");
                setBox(3,6,"3s");
                setBox(3,8,"2s");

                setBox(4,0,"5s");
                setBox(4,2,"7s");
                setBox(4,4,"9s");
                setBox(4,7,"1s");

                setBox(5,2,"2s");
                setBox(5,3,"1s");
                setBox(5,6,"9s");
                setBox(5,8,"4s");

                setBox(6,2,"9s");
                setBox(6,6,"4s");
                setBox(6,8,"6s");

                setBox(7,1,"5s");
                setBox(7,3,"3s");
                setBox(7,5,"6s");

                setBox(8,1,"3s");
                setBox(8,3,"2s");
                setBox(8,7,"7s");


        }
        public void testSolve() {
                int[][] solution = {{2,8,7,6,5,3,1,9,4},
                    {4,3,9,1,7,2,8,6,5},
                    {5,1,6,4,9,8,7,2,3},
                    {9,6,1,5,4,8,3,7,2},
                    {5,4,7,2,9,3,6,1,8},
                    {3,8,2,1,6,7,9,5,4},
                    {8,2,9,7,1,5,4,3,6},
                    {7,5,4,3,8,6,9,2,1},
                    {6,3,1,2,4,9,8,7,5}};
                 for (int i = 0; i < mSize; i++) {
                        for (int j = 0; j < mSize; j++) {
                                if (mBoard[i][j].charAt(1) != 's') {
                                    mBoard[i][j] = Integer.toString(solution[i][j]) + 'p';
                                }
                        }
                 }
                 mWasSolved = true;
        }

}

