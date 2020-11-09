package com.health.myapplication.model;


import android.provider.BaseColumns;

public class CalDateContract {
    private String date;


    private CalDateContract(){}

    public CalDateContract(String date) {
        this.date=date;
    }


    //BaseColumn 인터페이스는 자동으로 _ID 고유 기본기 생성
    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME="caldateTable";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
