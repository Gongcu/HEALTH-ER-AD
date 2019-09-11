package com.health.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import com.health.myapplication.R;
import com.health.myapplication.listener.DialogListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProgramDialog_Edit extends Dialog implements View.OnClickListener {
    private Context mContext;

    private DialogListener listener;
    private Button saveBtn;
    private Button quitBtn;

    private TextView nameTextView;
    private EditText setEditText;
    private EditText repEditText;

    private String date="";
    private String name="";
    private int set=0;
    private int rep=0;

    private boolean edit; //이건 Recycler_note에서 데이터 편집시 운동정보입력을 운동정보편집으로 바꾸기 위한 변수

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_program_data_edit); //calculator edit 재활용

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.4f;
        getWindow().setAttributes(layoutParams);

        nameTextView = findViewById(R.id.nameTextView);
        setEditText = findViewById(R.id.setEditText);
        repEditText = findViewById(R.id.repEditText);

        //셋팅
        saveBtn=(Button)findViewById(R.id.saveBtn);
        quitBtn=(Button)findViewById(R.id.quitBtn);

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        saveBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
    }
    public ProgramDialog_Edit(@NonNull Context context) {
        super(context);
        mContext=context;
        this.edit=false;
    }

    public void setDialogListener(DialogListener dialogListener){
        this.listener = dialogListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.saveBtn:
                try{
                    Date time = new Date();
                    String strTime = dateFormat.format(time);
                    name=nameTextView.getText().toString();
                    set = Integer.parseInt(setEditText.getText().toString());
                    rep = Integer.parseInt(repEditText.getText().toString());
                    //listener.onPositiveClicked(strTime, name,set,rep);
                    listener.onPositiveClicked(strTime, name,set,rep);

                }catch (NumberFormatException e){e.printStackTrace(); Toast.makeText(getContext(),"값을 입력해주세요", Toast.LENGTH_SHORT).show();}
                dismiss();
                break;
            case R.id.quitBtn:
                cancel();
        }
    }


    public TextView getNameTextView() {
        return nameTextView;
    }

    public EditText getSetEditText() {
        return setEditText;
    }

    public EditText getRepEditText() {
        return repEditText;
    }

}
