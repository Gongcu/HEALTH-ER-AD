package com.health.myapplication.ui.body_weight;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.health.myapplication.R;
import com.health.myapplication.listener.BodyWeightDialogListener;
import com.health.myapplication.listener.DialogListener;

public class BodyWeightDialog extends Dialog implements View.OnClickListener{
    private BodyWeightDialogListener listener;
    private Button saveBtn;
    private Button quitBtn;
    private EditText weightEditText;
    private TextView title;
    private boolean edit;
    private double weight=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bodyweight);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.4f;
        getWindow().setAttributes(layoutParams);

        weightEditText = findViewById(R.id.weightEditText);
        title = findViewById(R.id.textView3);
        if(edit)
            title.setText("몸무게 기록 편집");
        //셋팅
        saveBtn=(Button)findViewById(R.id.saveBtn);
        quitBtn=(Button)findViewById(R.id.quitBtn);

        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        saveBtn.setOnClickListener(this);
        quitBtn.setOnClickListener(this);
    }

    //생성자 생성
    public BodyWeightDialog(@NonNull Context context, boolean edit) {
        super(context);
        this.edit = edit;
    }

    public void setDialogListener(BodyWeightDialogListener dialogListener){
        this.listener = dialogListener;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.saveBtn:
                try{
                    weight = Double.parseDouble(weightEditText.getText().toString());
                    listener.onPositiveClicked(weight);
                }catch (NumberFormatException e){e.printStackTrace(); Toast.makeText(getContext(),"값을 입력해주세요", Toast.LENGTH_SHORT).show();}
                dismiss();
                break;
            case R.id.quitBtn:
                cancel();
        }
    }


    public void setListener(BodyWeightDialogListener listener) {
        this.listener = listener;
    }

    public EditText getWeightEditText() {
        return weightEditText;
    }

    public void setWeightEditText(EditText weightEditText) {
        this.weightEditText = weightEditText;
    }

}
