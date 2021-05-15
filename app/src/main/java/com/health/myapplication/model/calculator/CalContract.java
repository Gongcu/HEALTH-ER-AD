package com.health.myapplication.model.calculator;


import android.provider.BaseColumns;

public class CalContract {
    private String date;
    private String exerciseName;
    private double one_rm;

    private CalContract(){}

    public CalContract(String exerciseName, double one_rm) {
        this.exerciseName = exerciseName;
        this.one_rm = one_rm;
    }
    public CalContract(String date, String exerciseName, double one_rm) {
        this.date=date;
        this.exerciseName = exerciseName;
        this.one_rm = one_rm;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public double getOne_rm() {
        return one_rm;
    }

    public void setOne_rm(double one_rm) {
        this.one_rm = one_rm;
    }
    //BaseColumn 인터페이스는 자동으로 _ID 고유 기본기 생성
    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME="onermTable";
        public static final String COLUMN_EXERCISE="exercise";
        public static final String COLUMN_ONERM = "onerm";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_KEY = "parentid";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
