package com.health.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.health.myapplication.R;
import com.health.myapplication.listener.CalculatorDialogListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalculatorDialog extends Dialog implements View.OnClickListener{
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    private CalculatorDialogListener listener;
    private Context mContext;

    private EditText weightEditText;
    private EditText repEditText;
    private Button saveBtn;
    private Button quitBtn;

    private Spinner spinner;


    ArrayAdapter<CharSequence> adapter;

    String choice="";
    double weight=0.0;
    int rep=0;

    //생성자 생성
    public CalculatorDialog(@NonNull Context context) {
        super(context);
        mContext=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_calculator);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.4f;
        getWindow().setAttributes(layoutParams);

        spinner = findViewById(R.id.spinner);
        weightEditText = findViewById(R.id.weightEditText);
        repEditText = findViewById(R.id.repEditText);


        adapter = ArrayAdapter.createFromResource(mContext, R.array.the_three, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice=adapter.getItem(position).toString();
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

    public void setDialogListener(CalculatorDialogListener dialogListener){
        this.listener = dialogListener;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.saveBtn:
                try{
                    Date time = new Date();
                    String strTime = dateFormat.format(time);
                    double one_rm;
                    weight= Float.parseFloat(weightEditText.getText().toString());
                    rep= Integer.parseInt(repEditText.getText().toString());
                    if(rep==1)
                        one_rm=weight;
                    else{
                        one_rm=weight+(weight*0.025*rep);
                        one_rm=Math.round(one_rm*100)/100.0;
                    }
                    listener.onPositiveClicked(strTime,choice,one_rm);
                }catch (NumberFormatException e){e.printStackTrace(); Toast.makeText(getContext(),"올바른 값을 입력해주세요", Toast.LENGTH_SHORT).show();}
                dismiss();
                break;
            case R.id.quitBtn:
                cancel();
        }
    }
}
