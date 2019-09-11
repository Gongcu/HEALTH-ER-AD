package com.health.myapplication.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import com.health.myapplication.DbHelper.DbHelper_weight;
import com.health.myapplication.R;
import com.health.myapplication.data.WeightContract;
import com.health.myapplication.dialog.BodyWeightDialog;
import com.health.myapplication.listener.AdapterListener;
import com.health.myapplication.listener.DialogListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter_weight extends RecyclerView.Adapter<RecyclerAdapter_weight.ItemViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
    private BodyWeightDialog dialog;
    private SQLiteDatabase mDb;
    private DbHelper_weight dbHelper;
    private AdapterListener listener;


    public RecyclerAdapter_weight(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        dbHelper = new DbHelper_weight(context);
        mDb=dbHelper.getWritableDatabase();
    }

    public void setListener(AdapterListener listener){this.listener=listener;}
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_weight.ItemViewHolder holder, int position) {
        // 해당 포지션으로 이동한다.

        if(!mCursor.moveToPosition(position))
            return;
        // 열의 이름으로 열의 번호를 넘겨줌
        double weight = mCursor.getDouble(mCursor.getColumnIndex(WeightContract.WeightEntry.COLUMN_WEIGHT));

        String date = mCursor.getString(mCursor.getColumnIndex(WeightContract.WeightEntry.COLUMN_DATE));
        try {
            Date temp = dateFormat.parse(date);
            date=dateFormat.format(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //커서에 id 지정
        long id = mCursor.getLong(mCursor.getColumnIndex(WeightContract.WeightEntry._ID));
        holder.date.setText(date);
        holder.weight.setText(String.valueOf(weight));
        //홀더의 아이템뷰 태그로 id지정
        holder.itemView.setTag(id);
        holder.id=id;
        holder.weight_value=weight;
    }

    //데이터 업데이트 시 특정 뷰홀더만 다시 재생되게 override함
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_weight.ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }else {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    String type = (String) payload;
                    if (TextUtils.equals(type, "update") && holder instanceof ItemViewHolder) {
                        Cursor c =mDb.rawQuery("SELECT * FROM " + WeightContract.WeightEntry.TABLE_NAME + " WHERE " + WeightContract.WeightEntry._ID +
                                "=" + "'"+holder.id+"'", null);
                        c.moveToFirst();
                        double weight_value = c.getDouble(c.getColumnIndex(WeightContract.WeightEntry.COLUMN_WEIGHT));
                        holder.itemView.setTag(c.getColumnIndex(WeightContract.WeightEntry._ID));
                        holder.weight.setText(String.valueOf(weight_value));

                        holder.id=c.getLong(c.getColumnIndex(WeightContract.WeightEntry._ID));
                        holder.weight_value=weight_value;
                    }
                }
            }
        }
    }


    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private TextView weight;
        private TextView date;
        private double weight_value=0;
        private long id;

        public ItemViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateEditText);
            weight = itemView.findViewById(R.id.weightEditText);
            itemView.setOnCreateContextMenuListener(this); //2. OnCreateContextMenuListener 리스너를 현재 클래스에서 구현한다고 설정해둡니다.
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다. ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1001:  // 5. 편집 항목을 선택시
                        dialog = new BodyWeightDialog(mContext, true);
                        dialog.setDialogListener(new DialogListener() {
                            @Override
                            public void onPositiveClicked(int date, String part, String exercise) {
                            }
                            @Override
                            public void onPositiveClicked(String time, String name, int set, int rep) {
                            }
                            @Override
                            public void onPositiveClicked() {
                            }
                            @Override
                            public void onPositiveClicked(double height,double weight) {
                                editDB(weight);
                                notifyItemChanged(getAdapterPosition(),"update");
                            }
                            @Override
                            public void onNegativeClicked() {
                            }
                        });
                        dialog.show();
                        dialog.getWeightEditText().setText(String.valueOf(weight_value));
                        break;

                    case 1002:
                        if(deleteDB()){
                            Toast.makeText(mContext,"삭제 성공",Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(getAdapterPosition());
                            listener.deleteSuccess();
                        }
                        else{
                            Toast.makeText(mContext, "삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
                return true;
            }
        };

        public void editDB(double weight){
            ContentValues cv=new ContentValues();
            cv.put(WeightContract.WeightEntry.COLUMN_WEIGHT,weight);
            if(mDb.update(WeightContract.WeightEntry.TABLE_NAME,cv, WeightContract.WeightEntry._ID + "=?",new String[] {String.valueOf(id)})>0) {
                Toast.makeText(mContext, "편집 성공", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(mContext,"편집 실패",Toast.LENGTH_SHORT).show();
        }
        public boolean deleteDB(){
            return mDb.delete(WeightContract.WeightEntry.TABLE_NAME, WeightContract.WeightEntry._ID+"=?",new String[]{id+""})>0;
        }
    }
    // 어댑터에 현재 보관되고 있는 커서를 새로운 것으로 바꿔 UI를 갱신한다.

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 xml을 inflate 함
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_bodyweight_view,parent,false);
        return new ItemViewHolder(view);
    }

    public void swapCursor(Cursor newCursor) {
        // 항상 이전 커서를 닫는다.
        if (mCursor != null)
            mCursor.close();
        // 새 커서로 업데이트
        mCursor = newCursor;
        // 리사이클러뷰 업데이트
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }
}
