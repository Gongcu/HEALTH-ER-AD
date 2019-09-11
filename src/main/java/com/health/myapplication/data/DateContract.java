package com.health.myapplication.data;

import android.provider.BaseColumns;

import java.util.ArrayList;

public class DateContract {
    private String date;
    private ArrayList<NoteContract> list;


    public DateContract(String date, NoteContract note) {
        this.date = date;
        list= new ArrayList<>();
        list.add(note);
    }

    //BaseColumn 인터페이스는 자동으로 _ID 고유 기본기 생성
    public static final class DateContractEntry implements BaseColumns {
        public static final String TABLE_NAME="dateTable";
        public static final String COLUMN_DATE="date";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<NoteContract> getList() {
        return list;
    }

    public void setList(ArrayList<NoteContract> list) {
        this.list = list;
    }


}
