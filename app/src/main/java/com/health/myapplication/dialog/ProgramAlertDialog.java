package com.health.myapplication.dialog;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.health.myapplication.db.DbHelper_dialog;
import com.health.myapplication.R;
import com.health.myapplication.model.etc.DialogContract;

public class ProgramAlertDialog extends Dialog implements View.OnClickListener {
    private CheckBox checkBox;
    private Button quitBtn;
    private SQLiteDatabase mDb;
    private DbHelper_dialog dbHelper;
    private Context mContext;

    public ProgramAlertDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_program_alert);
        dbHelper=new DbHelper_dialog(mContext);
        mDb=dbHelper.getWritableDatabase();

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.4f;
        getWindow().setAttributes(layoutParams);

        //셋팅
        checkBox= findViewById(R.id.checkBox);
        quitBtn=(Button)findViewById(R.id.quitBtn);

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)

        quitBtn.setOnClickListener(this);

        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(DialogContract.Entry.COLUMN_CLICK,1);
                mDb.insert(DialogContract.Entry.TABLE_NAME,null,cv);
                cancel();
            }
        }) ;
    }



    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.quitBtn:
                cancel();
        }
    }

}
