package com.health.myapplication.ui.one_rm.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import com.health.myapplication.R
import com.health.myapplication.entity.etc.DialogType
import com.health.myapplication.listener.OneRmDialogListener
import kotlinx.android.synthetic.main.dialog_one_rm.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class OneRmDialog(
        context: Context,
        private val dialogType: DialogType
) : Dialog(context), View.OnClickListener {

    private lateinit var listener : OneRmDialogListener

    private var selectedExercise : String = ""
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_one_rm)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.4f
        }
        window.attributes = layoutParams

        if(dialogType == DialogType.UPDATE){
            exercise_name_layout.visibility = View.GONE
        }else{
            spinner.adapter = ArrayAdapter.createFromResource(context, R.array.the_three, android.R.layout.simple_spinner_item)

            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    selectedExercise = spinner.adapter.getItem(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }


        saveBtn.setOnClickListener(this)
        quitBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.saveBtn -> onSaveBtnClicked()
            R.id.quitBtn -> onQuitBtnClicked()
        }
    }

    fun setDialogListener(dialogListener: OneRmDialogListener) {
        listener = dialogListener
    }

    private fun onSaveBtnClicked(){
        try {
            val time = Date()
            val strTime: String = dateFormat.format(time)
            var oneRm: Double
            val weight = weightEditText.text.toString().toFloat().toDouble()
            val rep = repEditText.text.toString().toInt()
            if (rep == 1) oneRm = weight else {
                oneRm = weight + weight * 0.025 * rep
                oneRm = (oneRm * 100).roundToInt() / 100.0
            }
            listener.onPositiveClicked(strTime, selectedExercise, oneRm)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            Toast.makeText(context, "올바른 값을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        dismiss()
    }

    fun onQuitBtnClicked(){
        cancel()
    }
}
