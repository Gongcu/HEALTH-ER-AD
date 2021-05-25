package com.health.myapplication.ui.record.dialog

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
import com.health.myapplication.entity.etc.DialogType
import com.health.myapplication.listener.DateRecordCopyListener
import com.health.myapplication.listener.RecordDialogListener
import com.health.myapplication.entity.record.RecordDate
import kotlinx.android.synthetic.main.dialog_record.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecordDialog(
        context: Context,
        private val dialogType: DialogType,
        private val dateList: ArrayList<RecordDate> = ArrayList()
) : Dialog(context), View.OnClickListener{
    private var selectedPart = ""
    private var selectedExercise = ""
    private var DIRECT_INPUT = false
    private var INIT = false
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private lateinit var dateRecordCopyListener: DateRecordCopyListener
    private lateinit var recordDialogListener: RecordDialogListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_record)

        val layoutParams = WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.4f
        }
        window.attributes = layoutParams

        if(dialogType == DialogType.INSERT){
            setDateSpinner()
            val adapter = ArrayAdapter.createFromResource(context, R.array.spinner_part, android.R.layout.simple_spinner_item);
            part_spinner.adapter = adapter
            part_spinner.onItemSelectedListener = object: OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedPart = part_spinner.adapter.getItem(position).toString()
                    setSpinnerAdapterListener(spinnerArrayMap[position]!!)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }else{
            title_text_view.text = "운동 정보 수정"
            date_spinner.visibility = View.GONE
            part_layout.visibility= View.GONE
            exercise_layout.visibility= View.GONE
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

    private fun setDateSpinner() {
        val strDateList = ArrayList(dateList.map {
            it.date!!
        })
        strDateList.add("최근 기록 복사")
        date_spinner.apply {
            adapter = HintSpinnerAdapter(context, android.R.layout.simple_spinner_item, strDateList)
            setSelection(adapter.count)
            onItemSelectedListener = object: OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (INIT && position != strDateList.size - 1) {
                        dateRecordCopyListener.onDateSelected(dateList[position].id!!)
                        cancel()
                    }
                    INIT = true
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
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


    private fun onSaveBtnClicked(){
        try {
            val time = Date()
            val strTime: String = dateFormat.format(time)
            val set = setEditText.text.toString().toInt()
            val rep = repEditText.text.toString().toInt()
            val weight = weightEditText.text.toString().toFloat()
            if (DIRECT_INPUT) selectedExercise = directEditText.text.toString() //입력받은 값을 변수에 저장
            if (selectedExercise == "" && dialogType == DialogType.INSERT)
                Toast.makeText(context, "값을 입력해주세요", Toast.LENGTH_SHORT).show()
            else
                recordDialogListener.onPositiveClicked(strTime, selectedExercise, set, rep, weight.toDouble())
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            Toast.makeText(context, "값을 입력해주세요", Toast.LENGTH_SHORT).show()
        }
        dismiss()
    }
    private fun onQuitBtnClicked(){
        cancel()
    }

    fun setRecordDialogListener(recordDialogListener: RecordDialogListener) {
        this.recordDialogListener = recordDialogListener
    }

    fun setDateRecordCopyListener(listener: DateRecordCopyListener?) {
        dateRecordCopyListener = listener!!
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