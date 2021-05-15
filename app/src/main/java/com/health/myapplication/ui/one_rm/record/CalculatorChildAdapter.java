package com.health.myapplication.ui.one_rm.record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.health.myapplication.db.DbHelper_Calculator;
import com.health.myapplication.db.DbHelper_Calculator_sub;
import com.health.myapplication.R;
import com.health.myapplication.model.calculator.CalContract;
import com.health.myapplication.model.calculator.CalDateContract;
import com.health.myapplication.dialog.CalculatorEditDialog;
import com.health.myapplication.listener.AdapterListener;
import com.health.myapplication.listener.CalculatorDialogListener;

import java.util.ArrayList;
import java.util.List;


public class CalculatorChildAdapter extends RecyclerView.Adapter<CalculatorChildAdapter.ItemViewHolder>{
    private AdapterListener listener;
    private CalculatorEditDialog dialog;
    private long parent_id;
    private List<Long> id;
    private String date;
    private Context mContext;

    private SQLiteDatabase mDb,nDb;
    private DbHelper_Calculator DbHelper_date;
    private DbHelper_Calculator_sub DbHelper;

    public CalculatorChildAdapter(Context context) {
        mContext=context;
        DbHelper_date = new DbHelper_Calculator(context);
        mDb=DbHelper_date.getWritableDatabase();
        DbHelper = new DbHelper_Calculator_sub(context);
        nDb=DbHelper.getWritableDatabase();
        id= new ArrayList<>();
    }


    public void setAdapterListener(AdapterListener adapterListener){
        this.listener = adapterListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_onerm_data,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalculatorChildAdapter.ItemViewHolder holder, int position) {
        Cursor c = nDb.rawQuery("select * from "+ CalContract.Entry.TABLE_NAME,null);
        if (c != null && !c.moveToPosition(position))
            return;
        int mode=0;
        c.moveToFirst(); //커서 맨 처음으로 이동
        do{
            for(int i=0; i<id.size(); i++){ //저장된 리스트와 커서를 비교하여 기존에 있는 값인지 확인
                if((c.getLong(c.getColumnIndex(CalContract.Entry._ID))==id.get(i))){
                    mode=1; //기존에 있는 값이면 밑의 if문이 실행이 안되게 처리하기 위한 수
                    break;
                }
            }
            if((c.getLong(c.getColumnIndex(CalContract.Entry.COLUMN_KEY))==parent_id) && mode==0){
                String name_value = c.getString(c.getColumnIndex(CalContract.Entry.COLUMN_EXERCISE));
                double onerm_value = c.getDouble(c.getColumnIndex(CalContract.Entry.COLUMN_ONERM));
                holder.itemView.setTag(c.getColumnIndex(CalContract.Entry._ID));
                holder.exerciseName.setText(name_value);
                holder.one_rm.setText(String.valueOf(onerm_value));
                id.add(c.getLong(c.getColumnIndex(CalContract.Entry._ID)));
                holder.note_id=c.getLong(c.getColumnIndex(CalContract.Entry._ID));
                holder.data=new CalContract(name_value,onerm_value);
                break;
            }
            mode=0;
        } while(c.moveToNext());
        c.close();
    }
    //데이터 업데이트 시 특정 뷰홀더만 다시 재생되게 override함
    @Override
    public void onBindViewHolder(@NonNull CalculatorChildAdapter.ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }else {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    String type = (String) payload;
                    if (TextUtils.equals(type, "update") && holder instanceof ItemViewHolder) {
                        Cursor c =nDb.rawQuery("SELECT * FROM " + CalContract.Entry.TABLE_NAME + " WHERE " + CalContract.Entry._ID +
                                "=" + "'"+holder.note_id+"'", null);
                        if(c.getCount()>0) {
                            c.moveToFirst();
                            double onerm_value = c.getDouble(c.getColumnIndex(CalContract.Entry.COLUMN_ONERM));
                            holder.itemView.setTag(c.getColumnIndex(CalContract.Entry._ID));
                            holder.one_rm.setText(String.valueOf(onerm_value));

                            holder.note_id = c.getLong(c.getColumnIndex(CalContract.Entry._ID));
                            holder.data.setOne_rm(onerm_value);
                        }
                    }
                }
            }
        }
    }



    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private LinearLayout linearItem;
        private TextView exerciseName;
        private TextView one_rm;
        private CalContract data;
        private long note_id;
        // private TextView date;

        public ItemViewHolder(View itemView) {
            super(itemView);
            linearItem = itemView.findViewById(R.id.linearItem);
            exerciseName = itemView.findViewById(R.id.exerciseNameTextView);
            one_rm = itemView.findViewById(R.id.onermTextView);
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
                        String name;
                        dialog = new CalculatorEditDialog(mContext);
                        //기존엔 오류가 나서 mContext 전달값을  getApplicationContext()에서 ...activity.this로 변경 79줄
                        dialog.setDialogListener(new CalculatorDialogListener() {
                            @Override
                            public void onPositiveClicked() {
                            }
                            @Override
                            public void onPositiveClicked(String date,String name, double one_rm) {
                                editDB(one_rm);
                                notifyItemChanged(getAdapterPosition(),"update");
                            }
                            @Override
                            public void onNegativeClicked() {
                            }
                        });
                        dialog.show();
                        dialog.setTextForEditDb(data.getExerciseName());
                        break;

                    case 1002:
                        if(deleteDB()){
                            Toast.makeText(mContext,"삭제 성공",Toast.LENGTH_SHORT).show();
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
        public void editDB(double one_rm){
            ContentValues cv=new ContentValues();
            cv.put(CalContract.Entry.COLUMN_ONERM,one_rm);
            if(nDb.update(CalContract.Entry.TABLE_NAME,cv, CalContract.Entry._ID + "=?",new String[] {String.valueOf(note_id)})>0) {
                Toast.makeText(mContext, "편집 성공", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(mContext,"편집 실패",Toast.LENGTH_SHORT).show();
        }
        public boolean deleteDB(){
            boolean result;
            if(id.contains(note_id))
                id.remove(note_id);
            try {
                Cursor c = nDb.rawQuery("select * from " + CalContract.Entry.TABLE_NAME + " where " + CalContract.Entry._ID + "=" + note_id, null);
                c.moveToFirst();
                //삭제 후 삭제된 컬럼키에 해당하는 키가 noteTable에 없으면 dateTable의 id 삭제
                long column_key = c.getLong(c.getColumnIndex(CalContract.Entry.COLUMN_KEY));
                result = nDb.delete(CalContract.Entry.TABLE_NAME, CalContract.Entry._ID + "=" + note_id, null) > 0;
                if (result) { //삭제가 성공적으로 되면 note에 해당하는 column key의 개수가 0개(없으면) 해당 date도 삭제    최상위 어답터에 리스너 달아야될듯
                    c = nDb.rawQuery("select * from " + CalContract.Entry.TABLE_NAME + " where " + CalContract.Entry.COLUMN_KEY + "=" + column_key, null);
                    if (c.getCount() == 0) {
                        c = mDb.rawQuery("select * from " + CalDateContract.Entry.TABLE_NAME + " where " + CalDateContract.Entry._ID + "=" + column_key, null);
                        if (c.getCount() > 0) {
                            c.moveToFirst();
                            String date = c.getString(c.getColumnIndex(CalDateContract.Entry.COLUMN_DATE));
                            mDb.delete(CalDateContract.Entry.TABLE_NAME, CalDateContract.Entry._ID + "=?", new String[]{"" + column_key});
                            listener.dateDeleteSuccess(date);
                        }
                    }
                }
                c.close();
            }catch (CursorIndexOutOfBoundsException e){e.printStackTrace();return  false;}
            return result;
        }
    }
    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        int count;
        Cursor c =nDb.rawQuery("SELECT "+ CalContract.Entry.COLUMN_KEY+","+"COUNT(*)"+
                " FROM " + CalContract.Entry.TABLE_NAME + " GROUP BY " + CalContract.Entry.COLUMN_KEY, null);
        if(c.moveToFirst()) {
            do {
                long id= c.getLong(c.getColumnIndex(CalContract.Entry.COLUMN_KEY));
                try {
                    if (parent_id == id) { //새로운 날짜 추가되면 여기서 오류 났음
                        count = c.getInt(1);
                        c.close();
                        return count;
                    }
                }catch (NullPointerException e){c.moveToLast(); count=c.getInt(1); c.close(); return count;}
             } while (c.moveToNext());

        }
        c.close();
        return 0;
    }


    public void getDate(String date){
        this.date=date;
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    public void getId(long parent_id){
        this.parent_id=parent_id;
    }

}
