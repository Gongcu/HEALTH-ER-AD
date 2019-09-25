package com.health.myapplication.activity_program;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.health.myapplication.DbHelper.DbHelper_dialog;
import com.health.myapplication.data.DialogContract;
import com.health.myapplication.dialog.ProgramAlertDialog;

public class ShowAlert {
    private Context mContext;
    private SQLiteDatabase mDb;
    private DbHelper_dialog dbHelper;
    private ProgramAlertDialog dialog;

    public ShowAlert(Context context) {
        mContext=context;
        dbHelper = new DbHelper_dialog(mContext);
        mDb=dbHelper.getWritableDatabase();
        Cursor c = mDb.rawQuery("select * from "+ DialogContract.Entry.TABLE_NAME,null);
        if(c.getCount()==0)
        {
            dialog = new ProgramAlertDialog(mContext);
            dialog.show();
        }
        c.close();
    }
}
