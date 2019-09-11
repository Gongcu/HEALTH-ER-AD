package com.health.myapplication.data;

import android.provider.BaseColumns;

public class NoteContract {
    private String exerciseName;
    private int set;
    private int rep;
    private long id;

    public NoteContract(String exerciseName, int set, int rep) {
        this.exerciseName = exerciseName;
        this.set = set;
        this.rep = rep;
    }
    private NoteContract(){}
    //BaseColumn 인터페이스는 자동으로 _ID 고유 기본기 생성
    public static final class NoteDataEntry implements BaseColumns {
        public static final String TABLE_NAME="exerciseNote";
        public static final String COLUMN_EXERCISE_NAME = "exercisename";
        public static final String COLUMN_REP = "rep";
        public static final String COLUMN_SETTIME = "settime";
        public static final String COLUMN_KEY = "dateid";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
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
