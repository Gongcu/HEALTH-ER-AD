package com.health.myapplication.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.health.myapplication.DbHelper.DbHelper_program;
import com.health.myapplication.R;
import com.health.myapplication.activity.InformationActivity;
import com.health.myapplication.data.ProgramContract;
import com.health.myapplication.dialog.ProgramDialog_Edit;
import com.health.myapplication.listener.AdapterListener;
import com.health.myapplication.listener.DialogListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter_part_sub extends RecyclerView.Adapter<RecyclerAdapter_part_sub.ItemViewHolder>{
    private int PARENT_ACTIVITY;
    private int PARENT_DATE;
    private List<Long> id;
    private String part;
    private Context mContext;
    private Cursor nCursor;
    private SQLiteDatabase mDb;
    private DbHelper_program DbHelper;
    private int date;
    private AdapterListener listener;
    private ProgramDialog_Edit dialog;
    private Intent intent;


    public RecyclerAdapter_part_sub(Context context, Cursor cursor, int activity, int date) {
        mContext=context;
        nCursor=cursor;
        this.PARENT_ACTIVITY=activity;
        this.PARENT_DATE=date;

        DbHelper = new DbHelper_program(context);
        mDb=DbHelper.getWritableDatabase();

        id= new ArrayList<>();

    }

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerAdapter_part_sub.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater를 이용하여 xml을 inflate 함
        // 리사이클러뷰 업데이트
        intent = new Intent(mContext, InformationActivity.class);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_program_exercise,parent,false);
        return new RecyclerAdapter_part_sub.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter_part_sub.ItemViewHolder holder, int position) {
        if (nCursor != null && !nCursor.moveToPosition(position))
            return;
        int mode=0;
        nCursor.moveToFirst(); //커서 맨 처음으로 이동
        do{
            for(int i=0; i<id.size(); i++){ //저장된 리스트와 커서를 비교하여 기존에 있는 값인지 확인
                if((nCursor.getLong(nCursor.getColumnIndex(ProgramContract.ProgramDataEntry._ID))==id.get(i))){
                    mode=1; //기존에 있는 값이면 밑의 if문이 실행이 안되게 처리하기 위한 수
                    break;
                }
            }
            if((nCursor.getString(nCursor.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_PART)).equals(part)) && mode==0){
                final String name_value = nCursor.getString(nCursor.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_EXERCISE));
                int set = nCursor.getInt(nCursor.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_SET));
                int rep = nCursor.getInt(nCursor.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_REP));
                long value_id=nCursor.getLong(nCursor.getColumnIndex(ProgramContract.ProgramDataEntry._ID));
                holder.itemView.setTag(value_id);
                holder.exerciseName.setText(name_value);
                holder.setTextView.setText(String.valueOf(set));
                holder.repTextView.setText(String.valueOf(rep));
                id.add(value_id); //기존에 추가되었던건지 확인하기위해 추가
                holder.note_id=value_id;
                holder.data=new ProgramContract(part, name_value, date,set,rep);
                holder.getJson(name_value);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder.ITEM == false){
                            Toast.makeText(mContext,"해당 운동의 정보가 없습니다.",Toast.LENGTH_LONG).show();
                        }else {
                            intent.putExtra("name", name_value);
                            intent.putExtra("desc", holder.desc);
                            intent.putExtra("tip", holder.tip);
                            intent.putExtra("imageR", holder.image1);
                            intent.putExtra("imageF", holder.image2);
                            mContext.startActivity(intent);
                        }
                    }
                });
                break;
            }
            mode=0;
        } while(nCursor.moveToNext());
    }

    //데이터 업데이트 시 특정 뷰홀더만 다시 재생되게 override함
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_part_sub.ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }else {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    String type = (String) payload;
                    if (TextUtils.equals(type, "update") && holder instanceof RecyclerAdapter_part_sub.ItemViewHolder) {
                        //액티비티 맞는지 확인 안해도 될듯 id는 고유값이라
                        Cursor c =mDb.rawQuery("SELECT * FROM " + ProgramContract.ProgramDataEntry.TABLE_NAME + " WHERE " + ProgramContract.ProgramDataEntry._ID +
                                "=" + "'"+holder.note_id+"'", null);
                        c.moveToFirst();
                        Log.d("Update","updateddd");
                        String name_value = c.getString(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_EXERCISE));
                        int set = c.getInt(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_SET));
                        int rep = c.getInt(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_REP));

                        holder.itemView.setTag(c.getColumnIndex(ProgramContract.ProgramDataEntry._ID));
                        holder.exerciseName.setText(name_value);
                        holder.setTextView.setText(String.valueOf(set));
                        holder.repTextView.setText(String.valueOf(rep));
                        holder.data.setExercise(name_value);
                        holder.data.setSet(set);
                        holder.data.setRep(rep);
                        holder.note_id=nCursor.getLong(nCursor.getColumnIndex(ProgramContract.ProgramDataEntry._ID));
                        c.close();
                    }
                }
            }
        }
    }



    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
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
        // private TextView date;

        public ItemViewHolder(View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.partTextView);
            setTextView = itemView.findViewById(R.id.setTextView);
            repTextView = itemView.findViewById(R.id.repTextView);
            imageView = itemView.findViewById(R.id.partImageView);
            cardView = itemView.findViewById(R.id.cardview);
            cardView.setOnCreateContextMenuListener(this); //2. OnCreateContextMenuListener 리스너를 현재 클래스에서 구현한다고 설정해둡니다.
        }
        private void getJson(String exercise) {
            String json = null;
            boolean ITEM_CHECKER= false;
            //Context context;
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
                    case 1001:
                        dialog = new ProgramDialog_Edit(mContext);
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
                        dialog.getNameTextView().setText(data.getExercise());
                        dialog.getSetEditText().setText(String.valueOf(data.getSet()));
                        dialog.getRepEditText().setText(String.valueOf(data.getRep()));
                        break;
                    case 1002:  // 5. 삭제 항목을 선택시
                        boolean result=deleteDB();
                        if(result==true){
                            Toast.makeText(mContext,"삭제 성공",Toast.LENGTH_SHORT).show();
                            listener.deleteSuccess();}
                        else{
                            Toast.makeText(mContext, "삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
                return true;
            }
        };

        public void editDB(String name, int set, int rep){
            ContentValues cv=new ContentValues();
            cv.put(ProgramContract.ProgramDataEntry.COLUMN_EXERCISE,name);
            cv.put(ProgramContract.ProgramDataEntry.COLUMN_SET,set);
            cv.put(ProgramContract.ProgramDataEntry.COLUMN_REP,rep);
            if(mDb.update(ProgramContract.ProgramDataEntry.TABLE_NAME,cv,ProgramContract.ProgramDataEntry._ID + "=?",new String[] {String.valueOf(note_id)})>0)
                Toast.makeText(mContext,"편집 성공",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mContext,"편집 실패",Toast.LENGTH_SHORT).show();
        }
        public boolean deleteDB(){
            boolean result=false;
            if(id.contains(note_id))
                id.remove(note_id);

            try {
                Cursor c1 = mDb.rawQuery("select * from " + ProgramContract.ProgramDataEntry.TABLE_NAME + " where " + ProgramContract.ProgramDataEntry._ID + "=" + note_id, null);
                c1.moveToFirst();
                String part = c1.getString(c1.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_PART));
                c1.close();
                result=mDb.delete(ProgramContract.ProgramDataEntry.TABLE_NAME, ProgramContract.ProgramDataEntry._ID + "=" + note_id, null)>0;
                Cursor c = mDb.rawQuery("select * from " + ProgramContract.ProgramDataEntry.TABLE_NAME + " where " +
                        ProgramContract.ProgramDataEntry.COLUMN_DATE + "=" + PARENT_DATE + " and "+ProgramContract.ProgramDataEntry.COLUMN_PART +"='"+part+"' and "+
                        ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY + "=" + PARENT_ACTIVITY, null);
                if(c.getCount()==0){
                    //본문에 part 없애라
                    listener.dateDeleteSuccess(part);
                    c.close();
                }
            }catch (CursorIndexOutOfBoundsException e){e.printStackTrace(); return  result;}

            return result;
        }
    }

    // 데이터 셋의 크기를 리턴해줍니다. 액티비티도 맞는지 확인
    @Override
    public int getItemCount() {
        int ItemCount;
        Cursor c =mDb.rawQuery("SELECT "+ ProgramContract.ProgramDataEntry.COLUMN_PART+","+"COUNT(*)"+
                " FROM " + ProgramContract.ProgramDataEntry.TABLE_NAME + " where "+ ProgramContract.ProgramDataEntry.COLUMN_ACTIVITY+"="+PARENT_ACTIVITY+" and "+
                ProgramContract.ProgramDataEntry.COLUMN_DATE+"="+date +" GROUP BY " + ProgramContract.ProgramDataEntry.COLUMN_PART, null);

        if(c.moveToFirst()) {
            Log.d("count", String.valueOf(c.getInt(1))+"  "+part);
            do {
                String childPart = c.getString(c.getColumnIndex(ProgramContract.ProgramDataEntry.COLUMN_PART));
                if(part == null)
                    return 0;
                else if (part.equals(childPart)) {
                    ItemCount=c.getInt(1);
                    c.close();
                    return ItemCount; //해당 부위 개수 반환
                }
            } while (c.moveToNext());
        }
        c.close();
        return 0;
    }

    public void getParentPart(String parentPart){
        this.part=parentPart;
    }

    public void getParentDate(int date){this.date=date;}

    public void swapCursor(Cursor newCursor) {
        // 항상 이전 커서를 닫는다.
        if (nCursor != null)
            nCursor.close();
        // 새 커서로 업데이트
        nCursor = newCursor;
        // 리사이클러뷰 업데이트
        if (newCursor != null) {
            this.notifyDataSetChanged();
            id.clear();
            id=new ArrayList<>(); //저장된 id를 지우고 기록을 새로 추가하기 위함
        }
    }
}
