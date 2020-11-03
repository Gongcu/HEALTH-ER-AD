package com.health.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.health.myapplication.DbHelper.DbHelper_program;
import com.health.myapplication.R;
import com.health.myapplication.activity.ExerciseGuideActivity;
import com.health.myapplication.data.ProgramContract;
import com.health.myapplication.dialog.ProgramDialog_Edit;
import com.health.myapplication.listener.AdapterListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecyclerAdapter_example extends RecyclerView.Adapter<RecyclerAdapter_example.ItemViewHolder> {
    private int PARENT_ACTIVITY;
    private int PARENT_DATE;

    private Context mContext;
    private Cursor nCursor;
    private SQLiteDatabase mDb;
    private DbHelper_program DbHelper;
    private AdapterListener listener;
    private ProgramDialog_Edit dialog;
    private Intent intent;
    private ArrayList<ProgramContract> list;


    public RecyclerAdapter_example(Context context, Cursor cursor, int activity, int date, ArrayList<ProgramContract> list) {
        mContext=context;
        nCursor=cursor;
        this.PARENT_ACTIVITY=activity;
        this.PARENT_DATE=date;
        this.list = list;
        DbHelper = new DbHelper_program(context);
        mDb=DbHelper.getWritableDatabase();

    }

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerAdapter_example.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 xml을 inflate 함
        // 리사이클러뷰 업데이트
        intent = new Intent(mContext, ExerciseGuideActivity.class);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_program_exercise,parent,false);
        return new RecyclerAdapter_example.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter_example.ItemViewHolder holder, int position) {
        Cursor c = mDb.rawQuery("select * from " + ProgramContract.ProgramDataEntry.TABLE_NAME + " where " +
                ProgramContract.ProgramDataEntry.COLUMN_DATE + "=" + PARENT_DATE + " and " +
                ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY + "=" + PARENT_ACTIVITY+" and "+ ProgramContract.ProgramDataEntry.COLUMN_EXERCISE+"='"+list.get(position).getExercise()+"'", null);
        final String name_value = list.get(position).getExercise();
        int set = list.get(position).getSet();
        int rep = list.get(position).getRep();
        int order = list.get(position).getOrder();

        if(c.getCount()>0) {
            c.moveToFirst();
            long value_id = c.getLong(c.getColumnIndex(ProgramContract.ProgramDataEntry._ID));
            holder.itemView.setTag(value_id);
            holder.note_id=value_id;
            c.close();
        }
        holder.itemView.setId(list.get(position).getId());
        holder.exerciseName.setText(name_value);
        holder.setTextView.setText(String.valueOf(set));
        holder.repTextView.setText(String.valueOf(rep));
        holder.order=order;

        holder.data=new ProgramContract(name_value, PARENT_DATE,set,rep);

        holder.getJson(name_value);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.ITEM == false){
                    Toast.makeText(mContext,"해당 운동의 정보가 없습니다.",Toast.LENGTH_LONG).show();
                }else {
                    Log.d(TAG, "true"+ holder.ITEM +"");
                    intent.putExtra("name", name_value);
                    intent.putExtra("desc", holder.desc);
                    intent.putExtra("tip", holder.tip);
                    intent.putExtra("imageR", mContext.getResources().getIdentifier(holder.image1, "drawable", mContext.getPackageName()));
                    intent.putExtra("imageF", mContext.getResources().getIdentifier(holder.image2, "drawable", mContext.getPackageName()));
                    mContext.startActivity(intent);
                }
            }
        });
    }



    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private CardView cardView;
        private TextView exerciseName;
        private TextView setTextView;
        private TextView repTextView;
        private ProgramContract data;
        private long note_id;
        private String desc, tip;
        private String image1, image2;
        private boolean ITEM;
        private int order;
        // private TextView date;

        public ItemViewHolder(View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.partTextView);
            setTextView = itemView.findViewById(R.id.setTextView);
            repTextView = itemView.findViewById(R.id.repTextView);
            imageView = itemView.findViewById(R.id.partImageView);
            cardView = itemView.findViewById(R.id.cardview);
        }
        private void getJson(String exercise) {
            String json = null;
            boolean ITEM_CHECKER= false;
            try {
                InputStream is = mContext.getAssets().open("total_exercise.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("전체운동");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.getJSONObject(i);
                    if(o.getString("name").equals(exercise)) {
                        int id = mContext.getResources().getIdentifier(o.getString("imageR"), "drawable", mContext.getPackageName());
                        Drawable drawable1 = mContext.getResources().getDrawable(id);
                        imageView.setImageDrawable(drawable1);
                        desc=o.getString("desc"); //j로 얻어야됨
                        tip=o.getString("tip");
                        image1=o.getString("imageR"); //j로 얻어야됨
                        image2=o.getString("imageF");
                        ITEM=true;
                        ITEM_CHECKER = true;
                        break;
                    }
                }
                if(ITEM_CHECKER == false){
                    Drawable drawable = mContext.getResources().getDrawable(R.drawable.default_image);
                    imageView.setImageDrawable(drawable);
                    ITEM = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 데이터 셋의 크기를 리턴해줍니다. 액티비티도 맞는지 확인
    @Override
    public int getItemCount() {
        Cursor c =mDb.rawQuery("SELECT * FROM " + ProgramContract.ProgramDataEntry.TABLE_NAME + " where "+ ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY+"="+PARENT_ACTIVITY+" and "+
                ProgramContract.ProgramDataEntry.COLUMN_DATE+"="+PARENT_DATE, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public void remove(int id){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getId()==id)
                list.remove(i);
        }
    }

}
