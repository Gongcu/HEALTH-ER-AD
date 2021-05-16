package com.health.myapplication.ui.recommend_program.program_info;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.health.myapplication.R;
import com.health.myapplication.entity.etc.ExerciseVo;
import com.health.myapplication.util.Util;
import java.util.List;

public class RecommendExerciseAdapter extends RecyclerView.Adapter<RecommendExerciseAdapter.ItemViewHolder> {
    private List<ExerciseVo> list;
    private Context context;

    public RecommendExerciseAdapter(List<ExerciseVo> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
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
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        private ImageView imageView;
        private CardView cardView;
        private TextView setTextView;
        private TextView repTextView;
        private ExerciseVo exercise;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.partTextView);
            setTextView = itemView.findViewById(R.id.setTextView);
            repTextView = itemView.findViewById(R.id.repTextView);
            imageView = itemView.findViewById(R.id.partImageView);
            cardView = itemView.findViewById(R.id.cardview);

            cardView.setOnClickListener(this);
        }

        void onBind(ExerciseVo exercise) {
            this.exercise = exercise;
            textView.setText(exercise.getName());
            setTextView.setText(exercise.getSet()+"");
            repTextView.setText(exercise.getRep()+"");
            Drawable drawable = context.getResources().getDrawable(exercise.getImageR());
            imageView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View v) {
            Util.INSTANCE.startExerciseGuideActivity(context,exercise);
        }
    }
}
