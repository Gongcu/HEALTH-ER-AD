package com.health.myapplication.DbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.health.myapplication.data.DateContract;
import com.health.myapplication.data.NoteContract;

public class DbHelper_date_sub extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "exercisenote.db";
    private static final int DATABASE_VERSION = 2;

    public DbHelper_date_sub(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //실제 DB생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_EXERCISENOTE_TABLE="CREATE TABLE IF NOT EXISTS " +
                NoteContract.NoteDataEntry.TABLE_NAME + " (" +
                NoteContract.NoteDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
                NoteContract.NoteDataEntry.COLUMN_REP + " INTEGER NOT NULL, " +
                NoteContract.NoteDataEntry.COLUMN_SETTIME + " INTEGER NOT NULL, " + //스페이스바 확인
                NoteContract.NoteDataEntry.COLUMN_WEIGHT + " REAL NOT NULL, " + //스페이스바 확인
                NoteContract.NoteDataEntry.COLUMN_KEY + " INTEGER, " +
                NoteContract.NoteDataEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                " FOREIGN KEY ("+NoteContract.NoteDataEntry.COLUMN_KEY+") REFERENCES "+ DateContract.DateContractEntry.TABLE_NAME +" ("+DateContract.DateContractEntry._ID+"));";
        //");";

        //ON DELETE CASCADE
        //PRAGMA foreign_keys = ON;
        // 쿼리 실행
        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        sqLiteDatabase.execSQL(SQL_CREATE_EXERCISENOTE_TABLE);
    }

    //DB 스키마가 최근 것을 반영하게함
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 버전이 바뀌면 예전 버전의 테이블을 삭제 (나중에 ALTER 문으로 대체)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NoteContract.NoteDataEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
