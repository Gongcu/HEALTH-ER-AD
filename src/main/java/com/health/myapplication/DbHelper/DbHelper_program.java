package com.health.myapplication.DbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.health.myapplication.data.ProgramContract;

public class DbHelper_program extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "programTable.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper_program(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //실제 DB생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_DATE_TABLE="CREATE TABLE IF NOT EXISTS " +
                ProgramContract.ProgramDataEntry.TABLE_NAME + " (" +
                ProgramContract.ProgramDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY + " INTEGER NOT NULL, " +
                ProgramContract.ProgramDataEntry.COLUMN_DATE + " INTEGER NOT NULL, " + //한국 시간
                ProgramContract.ProgramDataEntry.COLUMN_PART + " TEXT NOT NULL, " + //한국 시간
                ProgramContract.ProgramDataEntry.COLUMN_EXERCISE + " TEXT NOT NULL, " + //한국 시간
                ProgramContract.ProgramDataEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                ProgramContract.ProgramDataEntry.COLUMN_SET + " INTEGER DEFAULT 0 , " +
                ProgramContract.ProgramDataEntry.COLUMN_REP + " INTEGER DEFAULT 0 "+
                ");";
        // 쿼리 실행
        sqLiteDatabase.execSQL(SQL_CREATE_DATE_TABLE);
    }
    //DB 스키마가 최근 것을 반영하게함
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 버전이 바뀌면 예전 버전의 테이블을 삭제 (나중에 ALTER 문으로 대체)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProgramContract.ProgramDataEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
        // If you need to add a column
    }
}
