package com.health.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.health.myapplication.DbHelper.DbHelper_proItem;
import com.health.myapplication.DbHelper.DbHelper_program;
import com.health.myapplication.R;
import com.health.myapplication.activity_program.*;
import com.health.myapplication.data.PartData;
import com.health.myapplication.data.ProgramContract;

import java.util.ArrayList;
import java.util.Collections;

public class RecyclerAdapter_program  extends RecyclerView.Adapter<RecyclerAdapter_program.ItemViewHolder> {
    private ArrayList<PartData> listData;
    private Context context;
    private SQLiteDatabase mDb;
    private DbHelper_program dbHelper;


    public RecyclerAdapter_program(Context context, ArrayList<PartData> list) {
        listData= list;
        Collections.sort(listData);
        this.context = context;
        dbHelper = new DbHelper_program(context);
        mDb=dbHelper.getWritableDatabase();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_program_data_view, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        //item을 하나, 하나 보여주는(bind 되는) 함수
        holder.onBind(listData.get(position));
        switch(listData.get(position).getPartName()) {
            case "무분할":
                holder.ACTIVITY_NUMBER=1;
                holder.itemView.setTag(1);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, OneDayProgram.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "2분할":
                holder.ACTIVITY_NUMBER=2;
                holder.itemView.setTag(2);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TwoDayProgram.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "3분할":
                holder.ACTIVITY_NUMBER=3;
                holder.itemView.setTag(3);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ThreeDayProgram.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "4분할":
                holder.ACTIVITY_NUMBER=4;
                holder.itemView.setTag(4);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FourDayProgram.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "5분할":
                holder.ACTIVITY_NUMBER=5;
                holder.itemView.setTag(5);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, FiveDayProgram.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "6분할":
                holder.ACTIVITY_NUMBER=6;
                holder.itemView.setTag(6);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SixDayProgram.class);
                        context.startActivity(intent);
                    }
                });
                break;
            case "7분할":
                holder.ACTIVITY_NUMBER=7;
                holder.itemView.setTag(7);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SevenDayProgram.class);
                        context.startActivity(intent);
                    }
                });
                break;
        }

    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(PartData data) {
        //외부에서 item 추가
        listData.add(data);
        Collections.sort(listData);
        notifyDataSetChanged();
    }

    public void removeItem(int activity) {
        if (activity == 1) {
            for (int i = 0; i < listData.size(); i++) {
                if (listData.get(i).getPartName().equals("무분할")) {
                    listData.remove(listData.get(i));
                    Collections.sort(listData);
                    deleteDB(activity);
                    return;
                }
            }
        } else {
            for (int i = 0; i < listData.size(); i++) {
                if (listData.get(i).getPartName().equals(activity + "분할")) {
                    listData.remove(listData.get(i));
                    Collections.sort(listData);
                    deleteDB(activity);
                    return;
                }
            }
        }
    }


    // 커스텀 뷰홀더
// item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private TextView textView;
        private ImageView imageView;
        private CardView cardView;
        private int ACTIVITY_NUMBER;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            textView = itemView.findViewById(R.id.partTextView);
            imageView = itemView.findViewById(R.id.partImageView);
            cardView= itemView.findViewById(R.id.cardview);

        }

        void onBind(PartData data) {
            textView.setText(data.getPartName());
            imageView.setImageResource(data.getResId());
        }
    }
    public boolean deleteDB(int ACTIVITY_NUMBER){
        return mDb.delete(ProgramContract.ProgramDataEntry.TABLE_NAME, ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY + "=" + ACTIVITY_NUMBER, null)>0;
    }
}
