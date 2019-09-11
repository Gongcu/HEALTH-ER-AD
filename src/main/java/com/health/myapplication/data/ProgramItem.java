package com.health.myapplication.data;

import android.provider.BaseColumns;

public class ProgramItem {

    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME="program";
        public static final String COLUMN_ACTIVITY="activity";
        public static final String COLUMN_TIMESTAMP="timestamp";
    }
}
