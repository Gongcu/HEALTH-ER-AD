package com.health.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.health.myapplication.R;
import com.health.myapplication.etc.HintSpinnerAdapter;
import com.health.myapplication.listener.DataListener;
import com.health.myapplication.listener.DateRecordCopyListener;
import com.health.myapplication.model.record.RecordDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingDataDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private DateRecordCopyListener dateRecordCopyListener;
    private DataListener listener;
    private Button saveBtn;
    private Button quitBtn;

    private TextView titleText;

    private Spinner dateSpinner;
    private Spinner spinner1;
    private Spinner spinner2;

    ArrayAdapter<String> adapter_date;
    ArrayAdapter<CharSequence> adapter_main, adapter_sub;

   //private EditText nameEditText;
    private EditText setEditText;
    private EditText repEditText;
    private EditText weightEditText;

    private List<RecordDate> recordDateList;
    private ArrayList<String> dateList = new ArrayList<>() ;
    String choice_main="";
    String choice_sub="";

    private String date="";
    private String name="";
    private int set=0;
    private int rep=0;
    private float weight =0;

    private boolean edit; //이건 Recycler_note에서 데이터 편집시 운동정보입력을 운동정보편집으로 바꾸기 위한 변수

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    private EditText directEditText;

    private static boolean DIRECT_EDIT=false;
    private static boolean INIT=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_training_data);

        //다이얼로그 밖의 화면은 흐리게 및 사이즈 설정
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.4f;
        getWindow().setAttributes(layoutParams);

        dateSpinner = findViewById(R.id.date_spinner);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        if(edit) {
            titleText = findViewById(R.id.textView3);
            titleText.setText("운동 정보 편집");
        }{
            setDateSpinner();
        }

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

        setEditText = findViewById(R.id.setEditText);
        repEditText = findViewById(R.id.repEditText);
        weightEditText = findViewById(R.id.weightEditText);


        //셋팅
        saveBtn=(Button)findViewById(R.id.saveBtn);
        quitBtn=(Button)findViewById(R.id.quitBtn);

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        saveBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
    }
    public TrainingDataDialog(@NonNull Context context) {
        super(context);
        mContext=context;
        this.edit=false;
    }

    public TrainingDataDialog(@NonNull Context context, List<RecordDate> list) {
        super(context);
        mContext=context;
        recordDateList = list;
        for(int i=0; i<list.size(); i++)
            dateList.add(recordDateList.get(i).getDate());
        dateList.add("최근 기록 복사");
        this.edit=false;
    }

    public void setDialogListener(DataListener dialogListener){
        this.listener = dialogListener;
    }
    public void setDateRecordCopyListener(DateRecordCopyListener listener){
        this.dateRecordCopyListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.saveBtn:
                try{
                    Date time = new Date();
                    String strTime = dateFormat.format(time);
                    set = Integer.parseInt(setEditText.getText().toString());
                    rep = Integer.parseInt(repEditText.getText().toString());
                    weight = Float.parseFloat(weightEditText.getText().toString());
                    if(DIRECT_EDIT)
                        choice_sub = directEditText.getText().toString();//입력받은 값을 변수에 저장
                    if(choice_sub.equals("")||choice_sub==null)
                        Toast.makeText(mContext, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    else {
                        listener.onPositiveClicked(strTime, choice_sub,set,rep,weight);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choice_sub = adapter_sub.getItem(position).toString();
                LinearLayout rootlayout = findViewById(R.id.linearItem);
                if(choice_sub.equals("직접입력")){
                    if(rootlayout.getVisibility()==View.GONE) {
                        rootlayout.setVisibility(View.VISIBLE);
                    }
                    directEditText = (EditText) rootlayout.findViewById(R.id.directEditText);
                    DIRECT_EDIT = true;
                }else{
                    if(rootlayout.getVisibility()==View.VISIBLE)
                        rootlayout.setVisibility(View.GONE);
                    DIRECT_EDIT = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setDateSpinner() {
        adapter_date = new HintSpinnerAdapter(mContext, android.R.layout.simple_spinner_item,dateList);
        dateSpinner.setAdapter(adapter_date);
        dateSpinner.setSelection(adapter_date.getCount());
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (INIT && position!=dateList.size()-1 ) {
                    dateRecordCopyListener.onDateSelected(recordDateList.get(position).getId());
                    cancel();
                }
                INIT=true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public EditText getSetEditText() {
        return setEditText;
    }

    public void setSetEditText(EditText setEditText) {
        this.setEditText = setEditText;
    }

    public EditText getRepEditText() {
        return repEditText;
    }

    public void setRepEditText(EditText repEditText) {
        this.repEditText = repEditText;
    }

    public EditText getWeightEditText() {
        return weightEditText;
    }

    public void setWeightEditText(EditText repEditText) {
        this.weightEditText = repEditText;
    }

}
