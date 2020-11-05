package com.health.myapplication.fragment;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
import com.health.myapplication.db.DbHelper_Calculator;
import com.health.myapplication.db.DbHelper_Calculator_sub;
import com.health.myapplication.R;
import com.health.myapplication.data.CalContract;
import com.health.myapplication.data.CalDateContract;
import com.health.myapplication.dialog.CalculatorDialog;
import com.health.myapplication.listener.CalculatorDialogListener;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class Calculator_ChartFragment extends Fragment {
    private LineChart lineChart;
    private int entrySize;
    private int xLabelCount;
    private LineData data;
    private LineDataSet dataSet1, dataSet2, dataSet3;
    private XAxis xAxis;

    private SQLiteDatabase mDb, nDb;
    private DbHelper_Calculator_sub dbHelper;
    private DbHelper_Calculator dbHelper_date;

    private CalculatorDialog dialog;

    private Button addBtn;
    private TextView textView;
    public static Context context;
    public static Activity activity;
    private ArrayList<String> date_list;

    private HorizontalBarChart barChart;
    private BarDataSet barDataSet,barDataSet2,barDataSet3;
    private BarData barData;




    private final String[] exerciseName = new String[]{"스퀏","벤치","데드"};
    private IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(exerciseName);

    public Calculator_ChartFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        date_list = new ArrayList<>();

        dbHelper_date=new DbHelper_Calculator(getActivity());
        mDb=dbHelper_date.getWritableDatabase();

        dbHelper=new DbHelper_Calculator_sub(getActivity());
        nDb=dbHelper.getWritableDatabase();

        setList(); //db의 date 할당
        context=getActivity();
        activity=getActivity();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textView = view.findViewById(R.id.theThree);
        barChart = view.findViewById(R.id.horizontal_bar_chart);
        addBtn = view.findViewById(R.id.AddButton);
        lineChart = view.findViewById(R.id.line_chart);


        dataSet1 = new LineDataSet(getData("스쿼트"),"스쿼트");

        dataSet2 = new LineDataSet(getData("벤치프레스"),"벤치프레스");

        dataSet3 = new LineDataSet(getData("데드리프트"),"데드리프트");

        drawLineData();

        data = new LineData();
        data.addDataSet(dataSet1);
        data.addDataSet(dataSet2);
        data.addDataSet(dataSet3);

        // x축 설정
        xAxis = lineChart.getXAxis();
        Cursor cursor = mDb.rawQuery("select * from "+CalDateContract.Entry.TABLE_NAME, null);
        if(cursor.getCount()>0)
            xAxis.setValueFormatter(new AxisValueFormatter(getDate()));
        else
            xAxis.setValueFormatter(new AxisValueFormatter(new String[] {""}));
        cursor.close();
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10.0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(date_list.size(),true);


        // y축 설정
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setTextSize(10.0f);
        yLAxis.setDrawGridLines(false);
        yLAxis.setAxisMaximum(400);
        yLAxis.setAxisMinimum(0);
        yLAxis.setLabelCount (11);

        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);

        lineChart.setMinimumHeight(1500);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setData(data);
        lineChart.invalidate();

        /**Bar Initialize*/

        barDataSet = new BarDataSet(getBarData(),"3대운동");
        final int color[] ={ContextCompat.getColor(getContext(), R.color.colorSub4),
                ContextCompat.getColor(getContext(), R.color.colorSub),
                ContextCompat.getColor(getContext(), R.color.colorPrimary)};

         final String[] exerciseName = new String[]{"스퀏","벤치","데드"};
         IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(exerciseName);
        barDataSet.setColors(color);
        barDataSet.setDrawValues(true);
        barData = new BarData();
        barData.addDataSet(barDataSet);
        barData.setDrawValues(true);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setGranularity(1f);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(formatter);

        YAxis yAxisRight_bar = barChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight_bar.setDrawGridLines(false);
        yAxisRight_bar.setDrawLabels(true);
        yAxisRight_bar.setDrawAxisLine(false);
        YAxis yAxisLeft_bar = barChart.getAxisLeft(); //Y축의 오른쪽면 설정
        yAxisLeft_bar.setDrawLabels(false);
        yAxisLeft_bar.setDrawAxisLine(false);
        yAxisLeft_bar.setDrawGridLines(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setData(barData);
        barChart.setMinimumHeight(400);
        barChart.setFitBars(true);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barChart.animateXY(1000, 1000);
        barChart.invalidate();
        /**                ********                 ***/

        addBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new CalculatorDialog(getActivity());
                dialog.setDialogListener(new CalculatorDialogListener() {  // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                    @Override
                    public void onPositiveClicked() { }
                    @Override
                    public void onPositiveClicked(String date,String name, double one_rm) {
                        //adapter.notifyDataChange();
                        if(addData(date, name, one_rm))
                            update();
                            //addDataEntry(name);
                    }
                    @Override
                    public void onNegativeClicked() {
                    }
                });
                dialog.show();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }


    private void setList(){
        Cursor cursor = mDb.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME+" group by "+ CalDateContract.Entry.COLUMN_DATE, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                date_list.add(cursor.getString(cursor.getColumnIndex(CalContract.Entry.COLUMN_DATE)));
            } while (cursor.moveToNext());
        }
        Collections.sort(date_list);
        cursor.close();

    }

    public ArrayList<Entry> getData(String name) {
        ArrayList<Entry> data_value = new ArrayList<>();
        Cursor cursor = nDb.rawQuery("select * from " + CalContract.Entry.TABLE_NAME+" where "+ CalContract.Entry.COLUMN_EXERCISE+"='"+name+"'", null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < date_list.size(); i++) {
                long column_key = cursor.getLong(cursor.getColumnIndex(CalContract.Entry.COLUMN_KEY));
                //dateTable에서 컬럼키와 같은 날의 데이트를 저장 하는 코드
                Cursor c = mDb.rawQuery("select * from "+CalDateContract.Entry.TABLE_NAME+" where "+CalDateContract.Entry._ID+"="+column_key,null);
                if(c.getCount()>0) {
                    c.moveToFirst();
                    Cursor c2= mDb.rawQuery("select * from "+CalDateContract.Entry.TABLE_NAME+" where "+CalDateContract.Entry._ID+"="+column_key,null);
                    c2.moveToFirst();
                    String date = c2.getString(c2.getColumnIndex(CalDateContract.Entry.COLUMN_DATE));
                    if(date_list.get(i).equals(date)){
                        Log.d("entry"+i,cursor.getFloat(cursor.getColumnIndex(CalContract.Entry.COLUMN_ONERM))+"");
                        data_value.add(new Entry((float) i, cursor.getFloat(cursor.getColumnIndex(CalContract.Entry.COLUMN_ONERM))));
                        if(!cursor.moveToNext()) {
                            cursor.moveToPrevious();
                            continue;
                        }
                    }else{
                        Log.d("entry"+i,i+"");
                        data_value.add(new Entry((float) i, 0));
                    }
                }
            }
        }
        cursor.close();
        return data_value;
    }





    public boolean addData(String date, String name, double one_rm) {
        ContentValues cv = new ContentValues();
        Cursor c2 = mDb.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME + " where " + CalDateContract.Entry.COLUMN_DATE + "='" + date+"'", null);
        /*같은 날이 있을경우 note만 추가*/
        if(c2.getCount()>0){
            c2.moveToFirst();
            long column_key = c2.getLong(c2.getColumnIndex(CalDateContract.Entry._ID));
            c2.close();
            Cursor c = nDb.rawQuery("select * from " + CalContract.Entry.TABLE_NAME + " where " + CalContract.Entry.COLUMN_KEY + "='" + column_key+"'" +
                    " and " + CalContract.Entry.COLUMN_EXERCISE + "='" + name+"'", null);
            if(c.getCount()>0){
                Toast.makeText(getActivity(),"이미 같은 운동이 존재 합니다.", Toast.LENGTH_LONG).show();
                c.close();
                return false;
            }
            cv.put(CalContract.Entry.COLUMN_DATE, date);
            cv.put(CalContract.Entry.COLUMN_EXERCISE, name);
            cv.put(CalContract.Entry.COLUMN_ONERM, one_rm);
            cv.put(CalContract.Entry.COLUMN_KEY, column_key);
            nDb.insert(CalContract.Entry.TABLE_NAME, null, cv);
            return true;
        }
        else {
            if(!date_list.contains(date))
                date_list.add(date);
            cv.put(CalDateContract.Entry.COLUMN_DATE, date);
            mDb.insert(CalDateContract.Entry.TABLE_NAME, null, cv);
            cv.clear();

            Cursor c3= mDb.rawQuery("select * from "+CalDateContract.Entry.TABLE_NAME,null);
            c3.moveToLast();
            long recentId = c3.getLong(c3.getColumnIndex(CalDateContract.Entry._ID));//가장 최신에 삽입된 날짜의 ID를 얻어옴
            cv.put(CalContract.Entry.COLUMN_DATE, date);
            cv.put(CalContract.Entry.COLUMN_EXERCISE, name);
            cv.put(CalContract.Entry.COLUMN_ONERM, one_rm);
            cv.put(CalContract.Entry.COLUMN_KEY, recentId);
            nDb.insert(CalContract.Entry.TABLE_NAME, null, cv);
            return true;
        }
    }




    private String[] getDate() {
        SimpleDateFormat old_sdf = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat new_sdf = new SimpleDateFormat("mm/dd");

        Cursor c = mDb.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME + " group by "+ CalDateContract.Entry.COLUMN_DATE, null);
        String[] dateArray = new String[c.getCount()];
        if (c != null && c.getCount() != 0) {
            c.moveToFirst();
            int i = 0;
            do {
                String date = c.getString(c.getColumnIndex(CalContract.Entry.COLUMN_DATE));
                try {
                    Date original_date = old_sdf.parse(date);
                    dateArray[i] =  new_sdf.format(original_date);
                }catch (ParseException e){e.printStackTrace();}
                i++;
            } while (c.moveToNext());
            xLabelCount=i-1;
        }
        c.close();
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
                return "0";
            else if(mDate.length<=(int)value)
                return mDate[mDate.length-1];
            else if((int)value==-1)
                return mDate[0];
            else
                return mDate[(int) value];
        }
    }


    public void update(){
        if(dataSet1!=null) {
            date_list.clear();
            setList();
            dataSet1.clear();
            dataSet2.clear();
            dataSet3.clear();
            data.removeDataSet(dataSet1);
            data.removeDataSet(dataSet2);
            data.removeDataSet(dataSet3);

            dataSet1 = new LineDataSet(getData("스쿼트"), "스쿼트");
            dataSet2 = new LineDataSet(getData("벤치프레스"), "벤치프레스");
            dataSet3 = new LineDataSet(getData("데드리프트"), "데드리프트");

            drawLineData();

            data.addDataSet(dataSet1);
            data.addDataSet(dataSet2);
            data.addDataSet(dataSet3);

            xAxis.setValueFormatter((new AxisValueFormatter(getDate())));
            xAxis.setLabelCount(date_list.size(), true);
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }

        if(barDataSet!=null){
            barDataSet.clear();
            barDataSet = new BarDataSet(getBarData(),"3대 운동");
            final int color[]={ContextCompat.getColor(getContext(), R.color.colorSub4),
                    ContextCompat.getColor(getContext(), R.color.colorSub),
                    ContextCompat.getColor(getContext(), R.color.colorPrimary)};
            barDataSet.setColors(color);
            barData.removeDataSet(barDataSet);
            barData.addDataSet(barDataSet);

            barChart.notifyDataSetChanged();
            barChart.invalidate();

        }
    }

    private void drawLineData(){
        dataSet1.setCircleColor(ContextCompat.getColor(getContext(), R.color.colorSub4));
        dataSet1.setColor(ContextCompat.getColor(getContext(), R.color.colorSub4));
        dataSet1.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.colorSub4));
        dataSet1.setCircleRadius(3);
        dataSet1.setValueTextSize(10.0f);
        dataSet1.setLineWidth(1.5f);

        dataSet2.setCircleColor(ContextCompat.getColor(getContext(), R.color.colorSub));
        dataSet2.setColor(ContextCompat.getColor(getContext(), R.color.colorSub));
        dataSet2.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.colorSub));
        dataSet2.setCircleRadius(3);
        dataSet2.setValueTextSize(10.0f);
        dataSet2.setLineWidth(1.5f);

        dataSet3.setCircleColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        dataSet3.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        dataSet3.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        dataSet3.setCircleRadius(3);
        dataSet3.setValueTextSize(10.0f);
        dataSet3.setLineWidth(1.5f);
    }

    private ArrayList getBarData(){
        float squat_max=0, bench_max=0, dead_max=0;
        Cursor c1 = nDb.rawQuery("select * from "+ CalContract.Entry.TABLE_NAME + " where "+ CalContract.Entry.COLUMN_EXERCISE+"='스쿼트'",null);
        Cursor c2 = nDb.rawQuery("select * from "+ CalContract.Entry.TABLE_NAME + " where "+ CalContract.Entry.COLUMN_EXERCISE+"='벤치프레스'",null);
        Cursor c3 = nDb.rawQuery("select * from "+ CalContract.Entry.TABLE_NAME + " where "+ CalContract.Entry.COLUMN_EXERCISE+"='데드리프트'",null);
        if(c1.getCount()>0){
            c1.moveToFirst();
            do{
                float weight = c1.getFloat(c1.getColumnIndex(CalContract.Entry.COLUMN_ONERM));
                if(weight>squat_max)
                    squat_max=weight;
            }while (c1.moveToNext());
            c1.close();
        }
        if(c2.getCount()>0){
            c2.moveToFirst();
            do{
                float weight = c2.getFloat(c2.getColumnIndex(CalContract.Entry.COLUMN_ONERM));
                if(weight>bench_max)
                    bench_max=weight;
            }while (c2.moveToNext());
            c2.close();
        }
        if(c3.getCount()>0){
            c3.moveToFirst();
            do{
                float weight = c3.getFloat(c3.getColumnIndex(CalContract.Entry.COLUMN_ONERM));
                if(weight>dead_max)
                    dead_max=weight;
            }while (c3.moveToNext());
            c3.close();
        }
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, squat_max));
        entries.add(new BarEntry(1f, bench_max));
        entries.add(new BarEntry(2f, dead_max));
        textView.setText(String.valueOf(squat_max+bench_max+dead_max));

        return entries;
    }

}
