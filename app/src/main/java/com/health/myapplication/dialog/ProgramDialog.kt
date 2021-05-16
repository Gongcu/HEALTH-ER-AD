package com.health.myapplication.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import com.health.myapplication.R
import com.health.myapplication.listener.ProgramDialogListener
import kotlinx.android.synthetic.main.dialog_record.*

class ProgramDialog(
        context: Context,
        private val dialogType: DialogType
) : Dialog(context), View.OnClickListener{
    private var selectedPart = ""
    private var selectedExercise = ""
    private var DIRECT_INPUT = false
    private lateinit var listener: ProgramDialogListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_program)

        val layoutParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.4f


        }
        window.attributes = layoutParams

        val adapter = ArrayAdapter.createFromResource(context, R.array.spinner_part, android.R.layout.simple_spinner_item);
        part_spinner.adapter = adapter
        part_spinner.onItemSelectedListener = object: OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPart = part_spinner.adapter.getItem(position).toString()
                setSpinnerAdapterListener(spinnerArrayMap[position]!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        saveBtn.setOnClickListener(this)
        quitBtn.setOnClickListener(this)
    }

    fun setSpinnerAdapterListener(exerciseArray: Int){
        val exerciseAdapter =  ArrayAdapter.createFromResource(context, exerciseArray, android.R.layout.simple_spinner_item)
        exercise_spinner.adapter = exerciseAdapter
        exercise_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                selectedExercise = exerciseAdapter.getItem(position).toString()
                val rootLayout = findViewById<LinearLayout>(R.id.linearItem)
                if (selectedExercise == "직접입력") {
                    if (rootLayout.visibility == View.GONE)
                        rootLayout.visibility = View.VISIBLE
                    DIRECT_INPUT = true
                } else {
                    if (rootLayout.visibility == View.VISIBLE)
                        rootLayout.visibility = View.GONE
                    DIRECT_INPUT = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.saveBtn -> onSaveBtnClicked()
            R.id.quitBtn -> cancel()
        }
    }

    private fun onSaveBtnClicked(){
        try{
            val date = 0;
            val set = Integer.parseInt(setEditText.getText().toString());
            val rep = Integer.parseInt(repEditText.getText().toString());
            if(DIRECT_INPUT)
                selectedExercise = directEditText.getText().toString();//입력받은 값을 변수에 저장
            if(selectedExercise == "")
                Toast.makeText(context, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
            else {
                listener.onPositiveClicked(date, selectedPart, selectedExercise, set, rep);
            }
        }catch (e: NumberFormatException){
            e.printStackTrace()
            Toast.makeText(context, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }

    fun setDialogListener(listener: ProgramDialogListener) {
        this.listener = listener
    }

    private val spinnerArrayMap = mapOf(
            0 to R.array.spinner_part_chest,
            1 to R.array.spinner_part_back,
            2 to R.array.spinner_part_leg,
            3 to R.array.spinner_part_shoulder,
            4 to R.array.spinner_part_biceps,
            5 to R.array.spinner_part_triceps,
            6 to R.array.spinner_part_abdominal
    )
}