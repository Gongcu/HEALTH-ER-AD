package com.health.myapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.health.myapplication.db.DbHelper_date;
import com.health.myapplication.db.DbHelper_date_sub;
import com.health.myapplication.R;
import com.health.myapplication.data.DateContract;
import com.health.myapplication.listener.AdapterListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter_date extends RecyclerView.Adapter<RecyclerAdapter_date.ItemViewHolder> {
    private ArrayList<String> date_list;
    private int i=0;
    private RecyclerAdapter_date_sub adapter;
    SQLiteDatabase mDb,nDb;
    // Item의 클릭 상태를 저장할 array 객체
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;
    private DbHelper_date DbHelper;
    private DbHelper_date_sub DbHelper_note;
    private Context mContext;
    private Cursor mCursor;
    private Cursor nCursor;
    private String date;
    String today;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    public RecyclerAdapter_date(Context context, Cursor cursor, Cursor cursor2,ArrayList<String> list) {
        mContext = context;
        mCursor = cursor;
        nCursor = cursor2;

        DbHelper = new DbHelper_date(context);
        mDb=DbHelper.getWritableDatabase();

        DbHelper_note = new DbHelper_date_sub(context);
        nDb=DbHelper_note.getWritableDatabase();
        date_list=list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 xml을 inflate 함
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_exercisedata_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter_date.ItemViewHolder holder, final int position) {
        long id = 0;
        try {
            date = date_list.get(position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Date time = new Date();
            date = dateFormat.format(time);
        }
        Log.d("adtefrag",""+date);
        holder.date.setText(date);
        holder.adapter.getDate(date);
        Cursor c = mDb.rawQuery("select * from "+ DateContract.DateContractEntry.TABLE_NAME+" where "+ DateContract.DateContractEntry.COLUMN_DATE+"='"+date+"'",null);
        c.moveToFirst();
        if (c.getCount()>0) {
            id = c.getLong(c.getColumnIndex(DateContract.DateContractEntry._ID));
            holder.itemView.setTag(id);
            holder.adapter.getId(id);
            c.close();
        }

        holder.adapter.getDate(date);

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
                holder.adapter = new RecyclerAdapter_date_sub(mContext, nCursor, mCursor);
                holder.recyclerView.swapAdapter(holder.adapter,true);
                notifyDataSetChanged();
                //
                // holder.adapter.no
            }

            @Override
            public void dateDeleteSuccess(String date) {
                if(date_list.contains(date)){
                    date_list.remove(date);
                    notifyDataSetChanged();
                }
            }
        });
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() { //중복 날짜 표시 안되게 count반환
        int count;
        Cursor c =mDb.rawQuery("SELECT "+DateContract.DateContractEntry.COLUMN_DATE+","+"COUNT(*)"+
                " FROM " + DateContract.DateContractEntry.TABLE_NAME + " GROUP BY " + DateContract.DateContractEntry.COLUMN_DATE, null);
        count=c.getCount();
        c.close();
        return count;
    }



    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        RecyclerAdapter_date_sub adapter;
        private Button button;
        private TextView date;
        private RecyclerView recyclerView;
        private LinearLayout layout;
        // private TextView date;


        public ItemViewHolder(View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.button);
            date = itemView.findViewById(R.id.dateText);
            layout = itemView.findViewById(R.id.linearItem);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext
                    , RecyclerView.VERTICAL
                    ,false));
            adapter= new RecyclerAdapter_date_sub(mContext, nCursor, mCursor);
            recyclerView.setAdapter(adapter);
        }

        public void setData(DateContract data) {
            adapter.updateData(data);
        }


        public RecyclerAdapter_date_sub getAdapter(){
            return adapter;
        }
    }


    public void updateList(ArrayList<String> list){
        if(date_list!=null)
            date_list.clear();
        date_list=list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}