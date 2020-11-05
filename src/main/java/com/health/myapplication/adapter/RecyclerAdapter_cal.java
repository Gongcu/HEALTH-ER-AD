package com.health.myapplication.adapter;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.health.myapplication.db.DbHelper_Calculator;
import com.health.myapplication.db.DbHelper_Calculator_sub;
import com.health.myapplication.R;
import com.health.myapplication.data.CalContract;
import com.health.myapplication.data.CalDateContract;
import com.health.myapplication.data.DateContract;
import com.health.myapplication.listener.AdapterListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter_cal extends RecyclerView.Adapter<RecyclerAdapter_cal.ItemViewHolder> {
    private CalContract recentData;
    private ArrayList<String> datelist;
    private boolean DATA_CHANGED;
    private int i = 0;
    private SQLiteDatabase mDb,nDb;
    String date;
    String transferDate;


    private DbHelper_Calculator dbHelper_date;
    private DbHelper_Calculator_sub DbHelper;
    private Context mContext;
    private Cursor mCursor;


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    public RecyclerAdapter_cal(Context context, Cursor cursor, ArrayList<String> datelist) {
        mContext = context;
        mCursor = cursor;
        dbHelper_date = new DbHelper_Calculator(context);
        mDb = dbHelper_date.getWritableDatabase();
        DbHelper = new DbHelper_Calculator_sub(context);
        nDb = DbHelper.getWritableDatabase();
        this.datelist=datelist;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 xml을 inflate 함
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_exercisedata_view, parent, false); //layout이 같음 date랑

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter_cal.ItemViewHolder holder, final int position) {
        try {
            date = datelist.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Date time = new Date();
            date = dateFormat.format(time);
        }

        Log.d("day", "" + date);
        holder.date.setText(date);
        holder.adapter.getDate(date);

        Cursor c = mDb.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME + " where " + CalDateContract.Entry.COLUMN_DATE + "='" + date + "'", null);
        c.moveToFirst();
        long id = c.getLong(c.getColumnIndex(CalContract.Entry._ID));
        holder.itemView.setTag(id);
        holder.adapter.getId(id);
        c.close();

        holder.button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.recyclerView.getVisibility() == View.GONE) {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    holder.button.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.up_btn_clicked));
                } else {
                    holder.recyclerView.setVisibility(View.GONE);
                    holder.button.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.up_btn));
                }
            }
        });

        holder.adapter.setAdpaterListener(new AdapterListener() {
            @Override
            public void deleteSuccess() {
                //holder.recyclerView.getRecycledViewPool().clear();

                holder.adapter = new RecyclerAdapter_cal_child(mContext);
                holder.recyclerView.swapAdapter(holder.adapter,true);
                notifyDataSetChanged();
                //notifyItemChanged(position);
            }

            @Override
            public void dateDeleteSuccess(String date) {
                if(datelist.contains(date)) {
                    datelist.remove(date);
                    notifyDataSetChanged();
                }
            }
        });
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() { //중복 날짜 표시 안되게 count반환
        int count;
        Cursor c = mDb.rawQuery("SELECT " + CalDateContract.Entry.COLUMN_DATE + "," + "COUNT(*)" +
                " FROM " + CalDateContract.Entry.TABLE_NAME + " GROUP BY " + CalDateContract.Entry.COLUMN_DATE, null);
        count=c.getCount();
        c.close();
        return count;
    }


    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private RecyclerAdapter_cal_child adapter;
        private Button button;
        private ItemTouchHelper itemTouchHelper;
        private TextView date;
        private RecyclerView recyclerView;
        private LinearLayout layout;
        private int position;
        // private TextView date;


        public ItemViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            date = itemView.findViewById(R.id.dateText);
            layout = itemView.findViewById(R.id.linearItem);
            recyclerView = itemView.findViewById(R.id.recyclerView);

            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext
                    , RecyclerView.VERTICAL
                    , false));
            adapter = new RecyclerAdapter_cal_child(mContext);
            recyclerView.setAdapter(adapter);
        }

        public void setData(DateContract data) {
            adapter.updateData(data);
        }

    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

}