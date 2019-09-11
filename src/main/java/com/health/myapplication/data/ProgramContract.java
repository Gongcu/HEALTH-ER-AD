package com.health.myapplication.data;

import android.provider.BaseColumns;

public class ProgramContract {
    private String exercise;
    private String part;
    private int activity; //액티비티 분할
    private int date; //액티비티 속 일차
    private int set;
    private int rep;

    public ProgramContract(String part, String exercise, int date, int activity, int set, int rep) {
        this.part = part;
        this.exercise = exercise;
        this.activity= activity;
        this.date=date;
        this.set=set;
        this.rep=rep;
    }
    public ProgramContract(String part, String exercise, int date, int set, int rep) {
        this.part = part;
        this.exercise = exercise;
        this.date=date;
        this.set=set;
        this.rep=rep;
    }
    public ProgramContract(String exercise, int set, int rep) {
        this.part = part;
        this.exercise = exercise;
        this.set=set;
        this.rep=rep;
    }
    private ProgramContract(){}

    //BaseColumn 인터페이스는 자동으로 _ID 고유 기본기 생성
    public static final class ProgramDataEntry implements BaseColumns {
        public static final String TABLE_NAME="program";
        public static final String COLUMN_ACTIVITY="activity";
        public static final String COLUMN_PART = "part";
        public static final String COLUMN_EXERCISE = "exercise";
        public static final String COLUMN_DATE = "divide";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_SET = "settime";
        public static final String COLUMN_REP = "rep";
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int date) {
        this.activity = date;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getRep() {
        return rep;
    }

    public void setRep(int rep) {
        this.rep = rep;
    }
}
