package com.health.myapplication.DbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.health.myapplication.data.ProgramItem;

public class DbHelper_proItem extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "programItem.db";
    private static final int DATABASE_VERSION = 2;

    public DbHelper_proItem(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //실제 DB생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_ITEM_TABLE="CREATE TABLE " +
                ProgramItem.Entry.TABLE_NAME + " (" +
                ProgramItem.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProgramItem.Entry.COLUMN_ACTIVITY + " INTEGER NOT NULL, "+
                ProgramItem.Entry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
                ");";
        // 쿼리 실행
        sqLiteDatabase.execSQL(SQL_CREATE_ITEM_TABLE);
    }

    //DB 스키마가 최근 것을 반영하게함
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 버전이 바뀌면 예전 버전의 테이블을 삭제 (나중에 ALTER 문으로 대체)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProgramItem.Entry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
