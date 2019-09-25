package com.health.myapplication.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import com.health.myapplication.DbHelper.DbHelper_date;
import com.health.myapplication.DbHelper.DbHelper_date_sub;
import com.health.myapplication.R;
import com.health.myapplication.data.DateContract;
import com.health.myapplication.data.NoteContract;
import com.health.myapplication.dialog.TrainingDataDialog_edit;
import com.health.myapplication.listener.DataListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter_day extends RecyclerView.Adapter<RecyclerAdapter_day.ItemViewHolder> {
    private Context mContext;
    private SQLiteDatabase mDb, nDb;
    private DbHelper_date dbHelperDate;
    private DbHelper_date_sub dbHelperNote;
    private long PARENT_ID;
    private ArrayList<NoteContract> list;
    private TrainingDataDialog_edit dialog;

    public RecyclerAdapter_day(Context mContext, ArrayList<NoteContract> list) {
        this.mContext = mContext;
        this.list = list;
        dbHelperDate = new DbHelper_date(mContext);
        dbHelperNote = new DbHelper_date_sub(mContext);
        mDb=dbHelperDate.getWritableDatabase();
        nDb=dbHelperNote.getWritableDatabase();
    }

    @NonNull
    @Override
    public RecyclerAdapter_day.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_training_data2,viewGroup,false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_day.ItemViewHolder itemViewHolder, int position) {
        long cursor_id=list.get(position).getId();
        itemViewHolder.itemView.setTag(cursor_id);
        itemViewHolder.note_id=cursor_id;
        itemViewHolder.onBind(position);
    }

    public void onBindViewHolder(@NonNull RecyclerAdapter_day.ItemViewHolder holder, int position, @NonNull List<Object> payloads) {

        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }else {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    String type = (String) payload;
                    if (TextUtils.equals(type, "update") && holder instanceof ItemViewHolder) {
                        Cursor c =nDb.rawQuery("SELECT * FROM " + NoteContract.NoteDataEntry.TABLE_NAME + " WHERE " + NoteContract.NoteDataEntry._ID +
                                "=" + "'"+holder.note_id+"'", null);
                        c.moveToFirst();
                        String name_value = c.getString(c.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME));
                        int set_value = c.getInt(c.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_SETTIME));
                        int rep_value = c.getInt(c.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_REP));
                        float weight_value = c.getFloat(c.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_WEIGHT));
                        holder.itemView.setTag(c.getColumnIndex(NoteContract.NoteDataEntry._ID));
                        holder.name.setText(name_value);
                        holder.set.setText(String.valueOf(set_value));
                        holder.rep.setText(String.valueOf(rep_value));
                        holder.weight.setText(String.valueOf(weight_value));

                        holder.note_id = c.getLong(c.getColumnIndex(NoteContract.NoteDataEntry._ID));
                        holder.data.setId(c.getLong(c.getColumnIndex(NoteContract.NoteDataEntry._ID)));
                        holder.data.setExerciseName(name_value);
                        holder.data.setRep(rep_value);
                        holder.data.setSet(set_value);
                        holder.data.setWeight(weight_value);
                        c.close();
                    }
                }
            }
        }
    }

    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private TextView name;
        private TextView set;
        private TextView rep;
        private TextView weight;
        private NoteContract data;
        private long note_id;

        public ItemViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exerciseNameTextView);
            set = itemView.findViewById(R.id.setTextView);
            rep = itemView.findViewById(R.id.repTextView);
            weight = itemView.findViewById(R.id.weightTextView);

            itemView.setOnCreateContextMenuListener(this); //2. OnCreateContextMenuListener 리스너를 현재 클래스에서 구현한다고 설정해둡니다.

        }

        public void onBind(int position){
            name.setText(list.get(position).getExerciseName());
            set.setText(String.valueOf(list.get(position).getSet()));
            rep.setText(String.valueOf(list.get(position).getRep()));
            weight.setText(String.valueOf(list.get(position).getWeight()));
            data = new NoteContract(list.get(position).getExerciseName(),list.get(position).getSet(),list.get(position).getRep(),list.get(position).getWeight());
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
                        dialog = new TrainingDataDialog_edit(mContext);
                        //기존엔 오류가 나서 mContext 전달값을  getApplicationContext()에서 ...activity.this로 변경 79줄
                        dialog.setDialogListener(new DataListener() {
                            @Override
                            public void onPositiveClicked(String time, String name, int set, int rep, float weight) {
                                editDB(name,set,rep, weight);
                                notifyItemChanged(getAdapterPosition(),"update");
                            }
                        });
                        dialog.show();
                        dialog.getNameTextView().setText(data.getExerciseName());
                        dialog.getSetEditText().setText(String.valueOf(data.getSet()));
                        dialog.getRepEditText().setText(String.valueOf(data.getRep()));
                        dialog.getWeightEditText().setText(String.valueOf(data.getWeight()));
                        break;

                    case 1002:
                        boolean result=deleteDB();
                        if(result==true){
                            Toast.makeText(mContext,"삭제 성공",Toast.LENGTH_SHORT).show();
                            Log.d("delete","success");
                            notifyItemRemoved(getAdapterPosition());}
                        else{
                            Toast.makeText(mContext, "삭제 실패", Toast.LENGTH_SHORT).show();
                            Log.d("delete","fail");
                        }
                        break;

                }
                return true;
            }
        };
        public void editDB(String name, int set, int rep, float weight){
            ContentValues cv=new ContentValues();
            cv.put(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME,name);
            cv.put(NoteContract.NoteDataEntry.COLUMN_SETTIME,set);
            cv.put(NoteContract.NoteDataEntry.COLUMN_REP,rep);
            cv.put(NoteContract.NoteDataEntry.COLUMN_WEIGHT,weight);
            if(nDb.update(NoteContract.NoteDataEntry.TABLE_NAME,cv,NoteContract.NoteDataEntry._ID + "=?",new String[] {String.valueOf(note_id)})>0)
                Toast.makeText(mContext,"편집 성공",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mContext,"편집 실패",Toast.LENGTH_SHORT).show();
        }

        public boolean deleteDB(){
            boolean result = false;
            for (int i=0; i<list.size(); i++){
                if(list.get(i).getId()==note_id) {
                    list.remove(i);
                    break;
                }
            }
            Cursor c = nDb.rawQuery("select * from " + NoteContract.NoteDataEntry.TABLE_NAME+ " where "+ NoteContract.NoteDataEntry._ID+"="+note_id,null);
            c.moveToFirst();
            long foreign_key = c.getLong(c.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_KEY));
            result =  nDb.delete(NoteContract.NoteDataEntry.TABLE_NAME, NoteContract.NoteDataEntry._ID + "=" + note_id, null)>0;

            if(result){
                c = nDb.rawQuery("select * from "+ NoteContract.NoteDataEntry.TABLE_NAME+" where "+ NoteContract.NoteDataEntry.COLUMN_KEY+"="+foreign_key,null);
                if(c.getCount()==0){
                    Cursor c2 = mDb.rawQuery("select * from "+ DateContract.DateContractEntry.TABLE_NAME +" where "+ DateContract.DateContractEntry._ID + "="+foreign_key,null);
                    if(c2.getCount()>0&&c2!=null) {
                        c2.moveToFirst();
                        mDb.delete(DateContract.DateContractEntry.TABLE_NAME, DateContract.DateContractEntry._ID + "=?", new String[]{foreign_key + ""});
                        c.close();
                        c2.close();
                    }
                }
            }
            return result;
        }
    }
    // 어댑터에 현재 보관되고 있는 커서를 새로운 것으로 바꿔 UI를 갱신한다.


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(NoteContract data) {
        // 외부에서 item을 추가시킬 함수입니다.
        list.add(data);
    }
}
