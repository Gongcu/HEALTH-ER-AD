package com.health.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.health.myapplication.model.calculator.CalContract;
import com.health.myapplication.model.calculator.CalDateContract;

public class DbHelper_Calculator_sub extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "calculator.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper_Calculator_sub(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //실제 DB생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_CALCULATOR_TABLE="CREATE TABLE IF NOT EXISTS " +
                CalContract.Entry.TABLE_NAME + " (" +
                CalContract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CalContract.Entry.COLUMN_EXERCISE + " TEXT NOT NULL, " +
                CalContract.Entry.COLUMN_ONERM + " REAL NOT NULL, " +
                CalContract.Entry.COLUMN_KEY + " INTEGER NOT NULL, " +
                CalContract.Entry.COLUMN_DATE + " DATE DEFAULT (date('now','localtime')), "+
                " FOREIGN KEY ("+ CalContract.Entry.COLUMN_KEY+") REFERENCES "+ CalDateContract.Entry.TABLE_NAME +" ("+CalDateContract.Entry._ID+"));";
        //");";
        // 쿼리 실행
        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        sqLiteDatabase.execSQL(SQL_CREATE_CALCULATOR_TABLE);
    }

    //DB 스키마가 최근 것을 반영하게함
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 버전이 바뀌면 예전 버전의 테이블을 삭제 (나중에 ALTER 문으로 대체)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CalContract.Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
