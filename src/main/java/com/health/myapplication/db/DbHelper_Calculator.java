package com.health.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.health.myapplication.model.CalDateContract;

public class DbHelper_Calculator extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "caldateTable.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper_Calculator(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //실제 DB생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_DATE_TABLE="CREATE TABLE IF NOT EXISTS " +
                CalDateContract.Entry.TABLE_NAME + " (" +
                CalDateContract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CalDateContract.Entry.COLUMN_DATE + " DATE DEFAULT (date('now','localtime')), " + //한국 시간
                CalDateContract.Entry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+ //국제표준시
                ");";
        // 쿼리 실행
        sqLiteDatabase.execSQL(SQL_CREATE_DATE_TABLE);
    }
    //DB 스키마가 최근 것을 반영하게함

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 버전이 바뀌면 예전 버전의 테이블을 삭제 (나중에 ALTER 문으로 대체)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CalDateContract.Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}