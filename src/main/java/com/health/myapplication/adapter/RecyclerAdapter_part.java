package com.health.myapplication.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.health.myapplication.DbHelper.DbHelper_program;
import com.health.myapplication.R;
import com.health.myapplication.data.ExerciseData;
import com.health.myapplication.data.ProgramContract;
import com.health.myapplication.listener.AdapterListener;

import java.util.ArrayList;

public class RecyclerAdapter_part extends RecyclerView.Adapter<RecyclerAdapter_part.ItemViewHolder>{
    private int x=0;
    SQLiteDatabase mDb;
    private ArrayList<String> saved_list;
    private ArrayList<ExerciseData> listData;
    private ArrayList<ArrayList<String>> list_image;
    private DbHelper_program DbHelper;
    private Context mContext;
    private Cursor mCursor;
    private int ACTIVITY_NUMBER;
    private boolean DATA_CHANGED;
    private int PARENT_DATE;


    public RecyclerAdapter_part(Context context, Cursor cursor,ArrayList<String> list, int date, int activity) {
        mContext = context;
        mCursor = cursor;
        this.saved_list=list;
        DbHelper = new DbHelper_program(context);
        mDb=DbHelper.getWritableDatabase();
        this.PARENT_DATE=date;
        this.ACTIVITY_NUMBER=activity;
    }

    @NonNull
    @Override
    public RecyclerAdapter_part.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_program_recyclerview, parent, false);
        return new RecyclerAdapter_part.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter_part.ItemViewHolder holder, int position) {
        if(saved_list.size()<position)
            return;
        holder.part = saved_list.get(position);
        //String part=saved_list.get(position);
        holder.partTextView.setText(saved_list.get(position));
        holder.adapter.getParentDate(PARENT_DATE);
        holder.adapter.getParentPart(saved_list.get(position));

        holder.adapter.setListener(new AdapterListener() {
            @Override
            public void deleteSuccess() {
                holder.adapter.swapCursor(getData());
            }

            @Override
            public void dateDeleteSuccess(String part) {
                if(saved_list.contains(part)) {
                    saved_list.remove(part);
                    notifyDataSetChanged();
                }
            }
        });
/*
        if(DATA_CHANGED)
            holder.getAdapter().swapCursor(getData());*/
    }

    @Override
    public int getItemCount() {
        int ItemCount;
        Cursor c =mDb.rawQuery("SELECT "+ProgramContract.ProgramDataEntry.COLUMN_PART+","+"COUNT(*)"+
                " FROM " + ProgramContract.ProgramDataEntry.TABLE_NAME + " where "+ ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY+"="+ACTIVITY_NUMBER+" and "+
                ProgramContract.ProgramDataEntry.COLUMN_DATE+"="+PARENT_DATE +" GROUP BY " + ProgramContract.ProgramDataEntry.COLUMN_PART, null);
        ItemCount = c.getCount();
        c.close();
        return ItemCount;
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private RecyclerAdapter_part_sub adapter;
        private TextView partTextView;
        private RecyclerView recyclerView;
        private LinearLayout layout;
        private long part_id;
        private String part;

        public ItemViewHolder(View itemView) {
            super(itemView);
            partTextView = itemView.findViewById(R.id.partTextView);
            layout = itemView.findViewById(R.id.linearItem);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            recyclerView.setNestedScrollingEnabled(false);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            /*
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext
                    , RecyclerView.VERTICAL
                    ,false));*/
            adapter= new RecyclerAdapter_part_sub(mContext,mCursor,ACTIVITY_NUMBER,PARENT_DATE);
            recyclerView.setAdapter(adapter);
        }

        public RecyclerAdapter_part_sub getAdapter() {
            return adapter;
        }
    }

    private Cursor getData() {
        String whereClause = ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY+"=? and "+ProgramContract.ProgramDataEntry.COLUMN_DATE+"=?";
        String[] whereArgs={String.valueOf(ACTIVITY_NUMBER),String.valueOf(PARENT_DATE)};
        return mDb.query(
                ProgramContract.ProgramDataEntry.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                ProgramContract.ProgramDataEntry.COLUMN_TIMESTAMP + " ASC"
        );
    }

    public void notifyChanged(boolean data){
        this.DATA_CHANGED=data;
    }


}
