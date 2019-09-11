package com.health.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.health.myapplication.R;
import com.health.myapplication.activity.CategoryDataActivity;
import com.health.myapplication.data.PartData;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    private ArrayList<PartData> listData = new ArrayList<>();
    private Context context;
    private Intent intent;


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        intent = new Intent(context, CategoryDataActivity.class);
        //intent.putExtra(“text”,String.valueOf(editText.getText()));
        //LayoutInflater를 이용하여 xml을 inflate 함
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partdata_view, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {
        //item을 하나, 하나 보여주는(bind 되는) 함수
        holder.onBind(listData.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("part",holder.textView.getText().toString());
                context.startActivity(intent);
            }
        });
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(PartData data) {
        //외부에서 item 추가
        listData.add(data);
    }


    // 커스텀 뷰홀더
// item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        private TextView textView;
        private ImageView imageView;
        private CardView cardView;
        private LinearLayout linearItem;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            textView = itemView.findViewById(R.id.partTextView);
            imageView = itemView.findViewById(R.id.partImageView);
            linearItem = itemView.findViewById(R.id.linearItem);
            cardView= itemView.findViewById(R.id.cardview);

        }

        void onBind(PartData data) {
            textView.setText(data.getPartName());
            imageView.setImageResource(data.getResId());
        }

    }
}
