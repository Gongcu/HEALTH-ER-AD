package com.health.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.health.myapplication.R;
import com.health.myapplication.activity.ExerciseGuideActivity;
import com.health.myapplication.model.ExerciseData;

import java.util.ArrayList;

public class GuideListAdapter extends RecyclerView.Adapter<GuideListAdapter.ItemViewHolder> {
    private ArrayList<ExerciseData> listData;
    private Context context;
    private Intent intent;
    private ArrayList<ArrayList<String>> list_image;

    public GuideListAdapter(ArrayList<ExerciseData> listData, ArrayList<ArrayList<String>> list_image ) {
        this.listData = listData;
        this.list_image=list_image;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        intent = new Intent(context, ExerciseGuideActivity.class);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        //item을 하나, 하나 보여주는(bind 되는) 함수
        holder.onBind(listData.get(position), list_image.get(position).get(0));
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent.putExtra("name",holder.textView.getText().toString());
                intent.putExtra("desc",listData.get(position).getDesc());
                intent.putExtra("tip",listData.get(position).getTip());
                intent.putExtra("imageR", context.getResources().getIdentifier(list_image.get(position).get(0), "drawable", context.getPackageName()));
                intent.putExtra("imageF", context.getResources().getIdentifier(list_image.get(position).get(1), "drawable", context.getPackageName()));
                context.startActivity(intent);
            }
        });
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return listData.size();
    }



    // 커스텀 뷰홀더
// item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{
        public final View mView;

        private TextView textView;
        private ImageView imageView;
        private LinearLayout linearItem;
        private CardView cardView;
        private TextView setTextView;
        private TextView repTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            textView = itemView.findViewById(R.id.partTextView);
            setTextView = itemView.findViewById(R.id.setTextView);
            repTextView = itemView.findViewById(R.id.repTextView);
            imageView = itemView.findViewById(R.id.partImageView);
            linearItem = itemView.findViewById(R.id.linearItem);
            cardView = itemView.findViewById(R.id.cardview);

        }

        void onBind(ExerciseData data, String strImage) {
            textView.setText(data.getName());
            setTextView.setText(data.getSet()+"");
            repTextView.setText(data.getRep()+"");
            int id = context.getResources().getIdentifier(strImage, "drawable", context.getPackageName());
            Drawable drawable1 = context.getResources().getDrawable(id);
            imageView.setImageDrawable(drawable1);
        }

    }
}
