package com.example.cyberlavoy.sudoku;

public class SudokuBoard {
        private int[][] mBoard;
        private int mSize;

        SudokuBoard(int size) {
                mBoard = new int[size][size];
                mSize = size;
        }
        public void clearBoard() {
                mBoard = new int[mSize][mSize];
        }

        public int getBox(int outer_location, int inner_location) {
                if (outer_location < mSize && inner_location < mSize) {
                        return mBoard[outer_location][inner_location];
                }
                else {
                        return -1;
                }
        }
        public boolean setBox(int outer_location, int inner_location, int value) {
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
                                        isSolved = false;
                                        //return false;
                                }
                        }
                }
                return isSolved;
        }
        public boolean isValidEntry(int outer_location, int inner_location, int value) {
                boolean isValid;
                for (int i = 0; i < mSize; i++) {
                        if (value == mBoard[outer_location][i] && i != inner_location) {
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
        public boolean isValidEntryHorizontal(int outer_location, int inner_location, int value) {
                int outer_iterator = -1;
                int inner_iterator = -1;
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
                                if (value == mBoard[i][j] && j != inner_location && i != outer_location) {
                                        return false;
                                }
                        }
                }
                return true;
        }
        public boolean isValidEntryVertical(int outer_location, int inner_location, int value) {
                int outer_iterator = outer_location % 3;
                int inner_iterator = inner_location % 3;
                for (int i = outer_iterator; i <= outer_iterator+6; i+=3) {
                       for (int j = inner_iterator; j <= inner_iterator+6; j+=3) {
                               if (value == mBoard[i][j] && j != inner_location && j != outer_location) {
                                       return false;
                               }
                       }
                }
                return true;
        }


        public void makeTestBoard() {
                setBox(0,0,2);
                setBox(0,2,7);
                setBox(0,6,1);
                setBox(0,8,4);

                setBox(1,1,3);
                setBox(1,2,9);
                setBox(1,3,1);
                setBox(1,5,2);

                setBox(2,2,6);
                setBox(2,3,4);
                setBox(2,5,8);
                setBox(2,6,7);

                setBox(3,1,6);
                setBox(3,6,3);
                setBox(3,8,2);

                setBox(4,0,5);
                setBox(4,2,7);
                setBox(4,4,9);
                setBox(4,7,1);

                setBox(5,2,2);
                setBox(5,3,1);
                setBox(5,6,9);
                setBox(5,8,4);

                setBox(6,2,9);
                setBox(6,6,4);
                setBox(6,8,6);

                setBox(7,1,5);
                setBox(7,3,3);
                setBox(7,5,6);

                setBox(8,1,3);
                setBox(8,3,2);
                setBox(8,7,7);


        }
        public SudokuBoard getTestSolution() {
                SudokuBoard solutionBoard = new SudokuBoard(mSize);
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
                                solutionBoard.setBox(i, j, solution[i][j]);
                        }
                 }
                return solutionBoard;
        }
}

