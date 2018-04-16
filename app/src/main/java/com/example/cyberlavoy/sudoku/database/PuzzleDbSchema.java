package com.example.cyberlavoy.sudoku.database;

/**
 * Created by CyberLaVoy on 4/10/2018.
 */

public class PuzzleDbSchema {
        public static final class PuzzleTable {
            public static final String NAME = "puzzles";

            public static final class Cols {
                public static final String UUID = "uuid";
                public static final String BOARDLAYOUT = "board_layout";
                public static final String TITLE = "title";
                public static final String SOLVED = "solved";
                public static final String DATECREATED = "date_created";
                public static final String LASTPLAYED = "last_played";
                public static final String SECONDSPLAYED = "seconds_played";
                public static final String THUMBNAIL = "thumbnail";
            }
        }
}
