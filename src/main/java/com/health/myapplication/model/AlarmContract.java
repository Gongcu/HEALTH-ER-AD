package com.health.myapplication.model;

import android.provider.BaseColumns;

public class AlarmContract {
    private int minute;
    private int second;

    public AlarmContract(int minute, int second) {
        this.minute = minute;
        this.second = second;
    }

    //BaseColumn 인터페이스는 자동으로 _ID 고유 기본기 생성
    public static final class AlarmContractEntry implements BaseColumns {
        public static final String TABLE_NAME="alarmTable";
        public static final String COLUMN_MINUTE = "minute";
        public static final String COLUMN_SECOND = "second";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
