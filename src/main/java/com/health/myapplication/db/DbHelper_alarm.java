package com.health.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.health.myapplication.data.AlarmContract;


public class DbHelper_alarm extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "alarmTable.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper_alarm(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    //실제 DB생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_ITEM_TABLE="CREATE TABLE " +
                AlarmContract.AlarmContractEntry.TABLE_NAME + " (" +
                AlarmContract.AlarmContractEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AlarmContract.AlarmContractEntry.COLUMN_MINUTE + " INTEGER NOT NULL, "+
                AlarmContract.AlarmContractEntry.COLUMN_SECOND + " INTEGER NOT NULL, "+
                AlarmContract.AlarmContractEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
                ");";
        // 쿼리 실행
        sqLiteDatabase.execSQL(SQL_CREATE_ITEM_TABLE);
    }

    //DB 스키마가 최근 것을 반영하게함
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // 버전이 바뀌면 예전 버전의 테이블을 삭제 (나중에 ALTER 문으로 대체)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AlarmContract.AlarmContractEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void updateAlarm(AlarmContract contract){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(AlarmContract.AlarmContractEntry.COLUMN_MINUTE,contract.getMinute());
        cv.put(AlarmContract.AlarmContractEntry.COLUMN_SECOND,contract.getSecond());

        Cursor cursor = db.rawQuery("select * from "+ AlarmContract.AlarmContractEntry.TABLE_NAME,null);
        if(cursor.getCount()==0){
            db.insert(AlarmContract.AlarmContractEntry.TABLE_NAME,null,cv);
        }else{
            cursor.moveToLast();
            long id = cursor.getLong(cursor.getColumnIndex(AlarmContract.AlarmContractEntry._ID));
            db.update(AlarmContract.AlarmContractEntry.TABLE_NAME,cv, AlarmContract.AlarmContractEntry._ID + "=?",new String[] {String.valueOf(id)});
        }
    }

    public String getAlarm(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ AlarmContract.AlarmContractEntry.TABLE_NAME,null);
        if(cursor.getCount()==0){
            return "0";
        }else{
            cursor.moveToFirst();
            String alarmTime = cursor.getInt(cursor.getColumnIndex(AlarmContract.AlarmContractEntry.COLUMN_MINUTE))+
                    ":"+ cursor.getInt(cursor.getColumnIndex(AlarmContract.AlarmContractEntry.COLUMN_SECOND));
            return alarmTime;
        }
    }
}
