package com.health.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.health.myapplication.R;
import com.health.myapplication.listener.ProgramDialogListener;

import java.util.ArrayList;

public class ProgramDialog extends Dialog implements View.OnClickListener{
    private ProgramDialogListener listener;
    private Context mContext;
    private static int ACTIVITY_NUMBER;

    private Button saveBtn;
    private Button quitBtn;

    private Spinner spinner1;
    private Spinner spinner2;

    private EditText setEditText;
    private EditText repEditText;

    private static boolean DIRECT_EDIT=false;

    ArrayAdapter<CharSequence> adapter_main, adapter_sub;
    ArrayList<String> date;

    String choice_date="";
    String choice_main="";
    String choice_sub="";

    EditText directEditText;
    ArrayList<String> item;

    //생성자 생성
    public ProgramDialog(@NonNull Context context, int activity_number) {
        super(context);
        mContext=context;
        this.ACTIVITY_NUMBER = activity_number;
        date=new ArrayList<>();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_program);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.4f;
        getWindow().setAttributes(layoutParams);



        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);


        setEditText = findViewById(R.id.setEditText);
        repEditText = findViewById(R.id.repEditText);



        adapter_main = ArrayAdapter.createFromResource(mContext, R.array.spinner_part, android.R.layout.simple_spinner_item);
        adapter_main.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter_main);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice_main=adapter_main.getItem(position).toString();
                switch (position){
                    case 0:
                        adapter_sub = ArrayAdapter.createFromResource(mContext, R.array.spinner_part_chest, android.R.layout.simple_spinner_item);
                        setListener();
                        break;
                    case 1:
                        adapter_sub = ArrayAdapter.createFromResource(mContext, R.array.spinner_part_back, android.R.layout.simple_spinner_item);
                        setListener();
                        break;
                    case 2:
                        adapter_sub = ArrayAdapter.createFromResource(mContext, R.array.spinner_part_leg, android.R.layout.simple_spinner_item);
                        setListener();
                        break;
                    case 3:
                        adapter_sub = ArrayAdapter.createFromResource(mContext, R.array.spinner_part_shoulder, android.R.layout.simple_spinner_item);
                        setListener();
                        break;
                    case 4:
                        adapter_sub = ArrayAdapter.createFromResource(mContext, R.array.spinner_part_biceps, android.R.layout.simple_spinner_item);
                        setListener();
                        break;
                    case 5:
                        adapter_sub = ArrayAdapter.createFromResource(mContext, R.array.spinner_part_triceps, android.R.layout.simple_spinner_item);
                        setListener();
                        break;
                    case 6:
                        adapter_sub = ArrayAdapter.createFromResource(mContext, R.array.spinner_part_abdominal, android.R.layout.simple_spinner_item);
                        setListener();
                        break;
                }
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

    public void setDialogListener(ProgramDialogListener dialogListener){
        this.listener = dialogListener;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.saveBtn:
                try{
                    int date = 0;
                    int set = Integer.parseInt(setEditText.getText().toString());
                    int rep = Integer.parseInt(repEditText.getText().toString());
                    if(DIRECT_EDIT)
                        try {
                            choice_sub = directEditText.getText().toString();//입력받은 값을 변수에 저장
                        }catch (NullPointerException e){e.printStackTrace();}

                    if(choice_sub.equals("")||choice_sub==null)
                        Toast.makeText(mContext, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    else {
                        listener.onPositiveClicked(date, choice_main, choice_sub, set, rep);
                    }
                }catch (NumberFormatException e){e.printStackTrace(); Toast.makeText(getContext(),"값을 입력해주세요", Toast.LENGTH_SHORT).show();}
                dismiss();
                break;
            case R.id.quitBtn:
                cancel();
        }
    }

    public void setListener(){
        adapter_sub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter_sub);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                choice_sub = adapter_sub.getItem(position).toString();
                if(choice_sub.equals("직접입력")){
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout rootlayout = findViewById(R.id.linearItem);
                    LinearLayout linear = (LinearLayout) View.inflate(mContext,
                            R.layout.spinner_directinput, rootlayout);
                    directEditText = (EditText) linear.findViewById(R.id.directEditText);
                    DIRECT_EDIT = true;
                }else{
                    DIRECT_EDIT = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
