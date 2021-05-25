package com.health.myapplication.ui.custom_program.program_info

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.R
import com.health.myapplication.entity.custom_program.CustomProgramExercise
import com.health.myapplication.entity.etc.ExerciseVo
import com.health.myapplication.ui.exercise_info.ExerciseInfoActivity
import com.health.myapplication.util.JsonParser

class CustomProgramExerciseAdapter(
        private val context: Context
) : RecyclerView.Adapter<CustomProgramExerciseAdapter.ItemViewHolder>() {
    private var list: List<CustomProgramExercise>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_program_exercise, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list!![position])
    }

    // 커스텀 뷰홀더 item layout 에 존재하는 위젯들을 바인딩합니다.
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val imageView: ImageView
        private val cardView: CardView
        private val exerciseName: TextView
        private val setTextView: TextView
        private val repTextView: TextView
        private var exerciseVo: ExerciseVo? = null
        private var order = -1


        override fun onClick(v: View) {
            if (exerciseVo==null) {
                Toast.makeText(context, "해당 운동의 정보가 없습니다.", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(context, ExerciseInfoActivity::class.java)
                intent.putExtra("exercise", exerciseVo)
                context.startActivity(intent)
            }
        }

        fun bind(customProgramExercise: CustomProgramExercise){
            itemView.id = customProgramExercise.id!!
            exerciseName.text = customProgramExercise.exercise
            setTextView.text = customProgramExercise.settime.toString()
            repTextView.text = customProgramExercise.rep.toString()
            order = customProgramExercise.itemorder
            exerciseVo = JsonParser.getExerciseFromJson(context,customProgramExercise.exercise)
            if(exerciseVo==null)
                imageView.setImageDrawable(context.getDrawable(R.drawable.default_image))
            else
                imageView.setImageDrawable(context.getDrawable(exerciseVo!!.imageR))
        }

        init {
            exerciseName = itemView.findViewById(R.id.partTextView)
            setTextView = itemView.findViewById(R.id.setTextView)
            repTextView = itemView.findViewById(R.id.repTextView)
            imageView = itemView.findViewById(R.id.partImageView)
            cardView = itemView.findViewById(R.id.cardview)
            cardView.setOnClickListener(this)
        }
    }

    fun setList(list : List<CustomProgramExercise>){
        this.list = list
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

}