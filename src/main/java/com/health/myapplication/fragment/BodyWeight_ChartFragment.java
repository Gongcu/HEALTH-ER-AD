package com.health.myapplication.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.health.myapplication.DbHelper.DbHelper_weight;
import com.health.myapplication.R;
import com.health.myapplication.data.WeightContract;
import com.health.myapplication.dialog.BodyWeightDialog;
import com.health.myapplication.listener.DialogListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BodyWeight_ChartFragment extends Fragment {
    private BodyWeightDialog dialog;
    private LineChart lineChart;

    private SQLiteDatabase mDb;
    private DbHelper_weight dbHelper;
    private int xLabelCount;
    private LineData data;
    private LineDataSet dataSet1;
    private XAxis xAxis;

    private Button button;
    private TextView changeTextView;
    private double weight = 0.0;

    public BodyWeight_ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dbHelper = new DbHelper_weight(getActivity());
        mDb = dbHelper.getWritableDatabase(); //데이터에 db 채우기 위함
        return inflater.inflate(R.layout.fragment_bodyweight_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        lineChart = view.findViewById(R.id.line_chart);
        changeTextView = view.findViewById(R.id.chageTextView);

        setChangeTextView();
        dataSet1 = new LineDataSet(getData(),"몸무게");
        drawLineData();
        data = new LineData();
        data.addDataSet(dataSet1);

        // x축 설정
        xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new AxisValueFormatter(getDate()));
        xAxis.setTextColor(Color.BLACK);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(xLabelCount,true);

        // y축 설정
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setDrawGridLines(false);
        yLAxis.setAxisMaximum(130);
        yLAxis.setAxisMinimum(30);
        yLAxis.setLabelCount (11);

        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        lineChart.setMinimumHeight(1500);
        lineChart.getDescription().setEnabled(false);
        lineChart.setData(data);
        lineChart.invalidate();




       button = view.findViewById(R.id.AddButton);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new BodyWeightDialog(getActivity(),false);
                dialog.setDialogListener(new DialogListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked(int date, String part, String exercise) {}
                    @Override
                    public void onPositiveClicked(String time, String name, int set, int rep) {
                    }

                    @Override
                    public void onPositiveClicked() {
                    }

                    @Override
                    public void onPositiveClicked(double height, double weight) {
                        setResult(height, weight);
                        addToNote();
                        addDataEntry((float)weight);
                    }

                    //이거 수정 가능성 높음
                    @Override
                    public void onNegativeClicked() {
                        Log.d("MyDialogListener", "onNegativeClicked");
                    }
                });
                dialog.show();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }



    private ArrayList<Entry> getData() {
        ArrayList<Entry> data_value = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("select * from " + WeightContract.WeightEntry.TABLE_NAME, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            int i = 0;
            do {
                data_value.add(new Entry((float)i,cursor.getFloat(cursor.getColumnIndex(WeightContract.WeightEntry.COLUMN_WEIGHT))));
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return data_value;
    }

    private String[] getDate() { //x값이 date 수만큼 들어오므로 개수의 차이가 있다.
        SimpleDateFormat old_sdf = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat new_sdf = new SimpleDateFormat("mm/dd");

        Cursor cursor = mDb.rawQuery("select * from " + WeightContract.WeightEntry.TABLE_NAME, null);

        String[] dateArray = new String[cursor.getCount()];
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            int i = 0;
            do {
                String date = cursor.getString(cursor.getColumnIndex(WeightContract.WeightEntry.COLUMN_DATE));
                try {
                    Date original_date = old_sdf.parse(date);
                    dateArray[i] =  new_sdf.format(original_date);
                }catch (ParseException e){e.printStackTrace();}
                i++;
            } while (cursor.moveToNext());
        }
        xLabelCount = dateArray.length;
        cursor.close();
        return dateArray;
    }

    public class AxisValueFormatter extends ValueFormatter {
        private String[] mDate;
        // 생성자 초기화
        AxisValueFormatter(String[] values) {
            this.mDate = values;
        }

        @Override
        public String getFormattedValue(float value) {
            if(mDate.length==1)
                return mDate[0];
            else if(mDate.length==0)
                return "";
            else if(mDate.length<=(int)value) {
                return mDate[mDate.length - 1];
            }
            else if((int)value==-1) {
                return mDate[0];
            }
            else {
                return mDate[(int) value];
            }
        }

    }

    public void changeDataSet(){
        if(dataSet1!=null){
            setChangeTextView();
            dataSet1.clear();
            dataSet1 = new LineDataSet(getData(),"몸무게");
            drawLineData();
            data.removeDataSet(0);
            data.addDataSet(dataSet1);
            for(int i=0; i<getDate().length; i++)
                Log.d("data"+i,getDate()[i]);
            xAxis.setValueFormatter((new AxisValueFormatter(getDate())));
            xAxis.setLabelCount(xLabelCount,true);
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }

//        setChangeTextView();
    }


    public void addDataEntry(float weight){
        dataSet1.clear();
        dataSet1 = new LineDataSet(getData(),"몸무게");
        drawLineData();
        data.removeDataSet(dataSet1);
        data.addDataSet(dataSet1);
        for(int i=0; i<getDate().length; i++)
            Log.d("data"+i,getDate()[i]);
        xAxis.setValueFormatter((new AxisValueFormatter(getDate())));
        xAxis.setLabelCount(xLabelCount,true);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    public void addToNote() {
        ContentValues cv = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String strDate = sdf.format(date);
        Cursor c = mDb.rawQuery("select * from "+ WeightContract.WeightEntry.TABLE_NAME+" where "+ WeightContract.WeightEntry.COLUMN_DATE+
                "='"+strDate+"'",null);
        if(c.getCount()>0) {
            Toast.makeText(getActivity(), "이미 오늘 입력한 몸무게가 있습니다. 꾹 눌러 편집 하세요.", Toast.LENGTH_SHORT).show();
            c.close();
            return;
        }else {
            cv.put(WeightContract.WeightEntry.COLUMN_DATE, strDate);
            cv.put(WeightContract.WeightEntry.COLUMN_HEIGHT, 0);
            cv.put(WeightContract.WeightEntry.COLUMN_WEIGHT, weight);
            mDb.insert(WeightContract.WeightEntry.TABLE_NAME, null, cv);

            setChangeTextView();
        }
    }

    //for dialog listener
    private void setResult(double height, double weight) {
        this.weight = weight;
    }

    /** 값의 입력 순서가 섞이면 문제가 생기는 함수(현재 로직에선 그럴 상황 없음) -> 로직 변경시 최신. 과거 date 확인 하고 값 받게*/
    private void setChangeTextView(){
        Cursor c = mDb.rawQuery("select * from "+ WeightContract.WeightEntry.TABLE_NAME,null);
        if(c.getCount()==1){
            changeTextView.setText("0.0");
            return;
        }
        else if(c.getCount()>0){
            c.moveToFirst();
            double firstValue = c.getDouble(c.getColumnIndex(WeightContract.WeightEntry.COLUMN_WEIGHT));
            c.moveToLast();
            double lastValue =c.getDouble(c.getColumnIndex(WeightContract.WeightEntry.COLUMN_WEIGHT));
            changeTextView.setText(String.format("%.1f",lastValue-firstValue));
        }
        c.close();
    }

    private void drawLineData(){
        dataSet1.setCircleColor(ContextCompat.getColor(getContext(), R.color.colorSub4));
        dataSet1.setColor(ContextCompat.getColor(getContext(), R.color.colorSub4));
        dataSet1.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.colorSub4));
        dataSet1.setLineWidth(2.0f);
        dataSet1.setCircleRadius(3);
        dataSet1.setValueTextSize(10.0f);
    }

}
