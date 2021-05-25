package com.health.myapplication.ui.one_rm.record;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.widget.TextView;

import com.health.myapplication.R;
import com.health.myapplication.entity.etc.DialogType;
import com.health.myapplication.ui.one_rm.dialog.OneRmDialog;
import com.health.myapplication.listener.AdapterListener;
import com.health.myapplication.listener.OneRmDialogListener;
import com.health.myapplication.entity.one_rm.OneRmRecord;

import java.util.List;


public class OneRmRecordAdapter extends RecyclerView.Adapter<OneRmRecordAdapter.ItemViewHolder>{
    private AdapterListener listener;
    private OneRmDialog dialog;
    private Context mContext;
    private List<OneRmRecord> records;

    private final int UPDATE = 1001;
    private final int DELETE = 1002;

    public OneRmRecordAdapter(Context context, List<OneRmRecord> records) {
        this.mContext=context;
        this.records= records;
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
    public void onBindViewHolder(@NonNull OneRmRecordAdapter.ItemViewHolder holder, int position) {
        OneRmRecord item = records.get(position);
        holder.bind(item);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        private TextView exerciseName;
        private TextView one_rm;
        private OneRmRecord oneRMRecord;

        public ItemViewHolder(View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exerciseNameTextView);
            one_rm = itemView.findViewById(R.id.onermTextView);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(OneRmRecord oneRMRecord){
            itemView.setTag(oneRMRecord.getId());
            exerciseName.setText(oneRMRecord.getExercise());
            one_rm.setText(String.valueOf(oneRMRecord.getWeight()));
            this.oneRMRecord = oneRMRecord;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {  // 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다. ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.
            MenuItem update = menu.add(Menu.NONE, UPDATE, 1, "편집");
            MenuItem delete = menu.add(Menu.NONE, DELETE, 2, "삭제");
            update.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case UPDATE:
                        dialog = new OneRmDialog(mContext, DialogType.UPDATE);
                        dialog.setDialogListener(new OneRmDialogListener() {
                            @Override
                            public void onPositiveClicked(String date,String name, double weight) {
                                listener.updateOneRMRecord(oneRMRecord.getId(),weight);
                            }
                        });
                        dialog.show();
                        break;

                    case DELETE:
                        listener.deleteOneRMRecord(oneRMRecord);
                        break;
                }
                return true;
            }
        };
    }

    @Override
    public int getItemCount() {
        return records.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
}
