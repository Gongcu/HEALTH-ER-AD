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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.health.myapplication.DbHelper.DbHelper_date;
import com.health.myapplication.DbHelper.DbHelper_date_sub;
import com.health.myapplication.R;
import com.health.myapplication.data.DateContract;
import com.health.myapplication.data.NoteContract;
import com.health.myapplication.dialog.TrainingDataDialog_edit;
import com.health.myapplication.listener.AdapterListener;
import com.health.myapplication.listener.DialogListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class RecyclerAdapter_date_sub extends RecyclerView.Adapter<RecyclerAdapter_date_sub.ItemViewHolder>{
    private AdapterListener listener;
    private TrainingDataDialog_edit dialog;

    private List<String> dateList;
    private List<Long> id;
    private List<NoteContract> noteList;
    private DateContract data;
    private long parentViewId;
    private String date;
    private Context mContext;
    private Cursor nCursor;
    private Cursor parent_cursor;
    private SQLiteDatabase mDb,nDb;
    private DbHelper_date DbHelper;
    private DbHelper_date_sub DbHelper_note;
    private
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    public RecyclerAdapter_date_sub(Context context, Cursor cursor, Cursor parent_cursor) {
        mContext=context;
        nCursor=cursor;
        this.parent_cursor=parent_cursor;

        DbHelper = new DbHelper_date(context);
        mDb=DbHelper.getWritableDatabase();

        DbHelper_note = new DbHelper_date_sub(context);
        nDb=DbHelper_note.getWritableDatabase();

        dateList= new ArrayList<>();
        id= new ArrayList<>();
    }
    public void updateData(DateContract data) {
        this.data=data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 xml을 inflate 함
        // 리사이클러뷰 업데이트
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_training_data,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_date_sub.ItemViewHolder holder, int position) {
        nCursor.close();
        nCursor = nDb.rawQuery("select * from "+NoteContract.NoteDataEntry.TABLE_NAME,null);
        if (nCursor != null && !nCursor.moveToPosition(position))
            return;
        int mode=0;
        nCursor.moveToFirst(); //커서 맨 처음으로 이동
        do{
            for(int i=0; i<id.size(); i++){ //저장된 리스트와 커서를 비교하여 기존에 있는 값인지 확인
                if((nCursor.getLong(nCursor.getColumnIndex(NoteContract.NoteDataEntry._ID))==id.get(i))){
                    mode=1; //기존에 있는 값이면 밑의 if문이 실행이 안되게 처리하기 위한 수
                }
            }
            if((nCursor.getLong(nCursor.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_KEY)) == parentViewId) && mode==0){
                String name_value = nCursor.getString(nCursor.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME));
                int set_value = nCursor.getInt(nCursor.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_SETTIME));
                int rep_value = nCursor.getInt(nCursor.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_REP));
                holder.itemView.setTag(nCursor.getColumnIndex(NoteContract.NoteDataEntry._ID));
                holder.exerciseName.setText(name_value);
                holder.settime.setText(String.valueOf(set_value));
                holder.rep.setText(String.valueOf(rep_value));
                id.add(nCursor.getLong(nCursor.getColumnIndex(NoteContract.NoteDataEntry._ID)));
                holder.note_id=nCursor.getLong(nCursor.getColumnIndex(NoteContract.NoteDataEntry._ID));
                holder.data=new NoteContract(name_value, set_value, rep_value);
                break;
            }
            mode=0;
        } while(nCursor.moveToNext());
    }
    //데이터 업데이트 시 특정 뷰홀더만 다시 재생되게 override함
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_date_sub.ItemViewHolder holder, int position, @NonNull List<Object> payloads) {

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
                        holder.itemView.setTag(c.getColumnIndex(NoteContract.NoteDataEntry._ID));
                        holder.exerciseName.setText(name_value);
                        holder.settime.setText(String.valueOf(set_value));
                        holder.rep.setText(String.valueOf(rep_value));

                        holder.note_id = c.getLong(c.getColumnIndex(NoteContract.NoteDataEntry._ID));
                        holder.data.setExerciseName(name_value);
                        holder.data.setRep(rep_value);
                        holder.data.setSet(set_value);
                    }
                }
            }
        }
    }



    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private LinearLayout linearItem;
        private TextView exerciseName;
        private TextView settime;
        private TextView rep;
        private NoteContract data;
        private long note_id;
       // private TextView date;

        public ItemViewHolder(View itemView) {
            super(itemView);
            linearItem = itemView.findViewById(R.id.linearItem);
            exerciseName = itemView.findViewById(R.id.exerciseNameTextView);
            settime = itemView.findViewById(R.id.setTextView);
            rep = itemView.findViewById(R.id.repTextView);

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
                        dialog = new TrainingDataDialog_edit(mContext,true);
                        //기존엔 오류가 나서 mContext 전달값을  getApplicationContext()에서 ...activity.this로 변경 79줄
                        dialog.setDialogListener(new DialogListener() {
                            @Override
                            public void onPositiveClicked(int date, String part, String exercise) {}
                            @Override
                            public void onPositiveClicked() {
                            }
                            @Override
                            public void onPositiveClicked(double height, double weight) {
                            }
                            @Override
                            public void onPositiveClicked(String time, String name, int set, int rep) {
                                editDB(name,set,rep);
                                notifyItemChanged(getAdapterPosition(),"update");
                            }
                            @Override
                            public void onNegativeClicked() {
                            }
                        });
                        dialog.show();
                        dialog.getNameTextView().setText(data.getExerciseName());
                        dialog.getSetEditText().setText(String.valueOf(data.getSet()));
                        dialog.getRepEditText().setText(String.valueOf(data.getRep()));
                        break;

                    case 1002:
                        boolean result=deleteDB();
                        if(result==true) {
                            Toast.makeText(mContext, "삭제 성공", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(getAdapterPosition());
                            listener.deleteSuccess();
                        }
                        else{
                            Toast.makeText(mContext, "삭제 실패", Toast.LENGTH_SHORT).show();
                            Log.d("delete","fail");
                            }
                        break;

                }
                return true;
            }
        };
        public void editDB(String name, int set, int rep){
            ContentValues cv=new ContentValues();
            cv.put(NoteContract.NoteDataEntry.COLUMN_EXERCISE_NAME,name);
            cv.put(NoteContract.NoteDataEntry.COLUMN_SETTIME,set);
            cv.put(NoteContract.NoteDataEntry.COLUMN_REP,rep);
            if(nDb.update(NoteContract.NoteDataEntry.TABLE_NAME,cv,NoteContract.NoteDataEntry._ID + "=?",new String[] {String.valueOf(note_id)})>0)
                Toast.makeText(mContext,"편집 성공",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mContext,"편집 실패",Toast.LENGTH_SHORT).show();
        }
        public boolean deleteDB(){
            boolean result;
            for (int i=0; i<id.size(); i++){
                if(id.contains(note_id)) {
                    id.remove(i);
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
                        String date = c2.getString(c2.getColumnIndex(DateContract.DateContractEntry.COLUMN_DATE));
                        mDb.delete(DateContract.DateContractEntry.TABLE_NAME, DateContract.DateContractEntry._ID + "=?", new String[]{foreign_key + ""});
                        listener.dateDeleteSuccess(date);
                        c.close();
                        c2.close();
                    }
                }
            }
            return result;
        }
    }
    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        int count;
        Cursor c3 =nDb.rawQuery("SELECT "+NoteContract.NoteDataEntry.COLUMN_KEY+","+"COUNT(*)"+
                " FROM " + NoteContract.NoteDataEntry.TABLE_NAME + " GROUP BY " + NoteContract.NoteDataEntry.COLUMN_KEY, null);

        if(c3.moveToFirst()) {

            do {
                long id = c3.getLong(c3.getColumnIndex(NoteContract.NoteDataEntry.COLUMN_KEY));
                if (parentViewId == id) { //새로운 날짜 추가되면 여기서 오류 났음
                    count=c3.getInt(1);
                    c3.close();
                    return count;
                }
            } while (c3.moveToNext());
        }
        return 0;
    }

    public void getId(long parentViewId){
        this.parentViewId=parentViewId;
    }

    public void getDate(String date){
        this.date=date;
    }

    public void setAdpaterListener(AdapterListener listener){
        this.listener = listener;
    }
}
