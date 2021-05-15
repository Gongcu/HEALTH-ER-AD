package com.health.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.health.myapplication.R;
import com.health.myapplication.listener.ProgramSelectListener;

import java.util.ArrayList;

public class ProgramSelectDialog extends Dialog implements View.OnClickListener{
    private ProgramSelectListener listener;
    private Context mContext;
    private static int ACTIVITY_NUMBER;

    private Button saveBtn;
    private Button quitBtn;

    ArrayAdapter<String> adapter_date;

    private Spinner spinner;


    ArrayList<String> date;

    String choice="";
    //생성자 생성
    public ProgramSelectDialog(@NonNull Context context) {
        super(context);
        mContext=context;
        date=new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_program_select);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.4f;
        getWindow().setAttributes(layoutParams);


        for (int i=0; i<7; i++) {
            if(i==0)
                date.add("무분할");
            else
                date.add((i + 1) + "분할");
        }
        spinner = findViewById(R.id.spinner);

        adapter_date = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, date);
        adapter_date.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter_date);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice=adapter_date.getItem(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //셋팅
        saveBtn=(Button)findViewById(R.id.saveBtn);
        quitBtn=(Button)findViewById(R.id.quitBtn);

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        saveBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
    }

    public void setDialogListener(ProgramSelectListener dialogListener){
        this.listener = dialogListener;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.saveBtn:
                try{
                    listener.onPositiveClicked(choice);
                }catch (NumberFormatException e){e.printStackTrace(); Toast.makeText(getContext(),"값을 입력해주세요", Toast.LENGTH_SHORT).show();}
                dismiss();
                break;
            case R.id.quitBtn:
                cancel();
        }
    }

}
