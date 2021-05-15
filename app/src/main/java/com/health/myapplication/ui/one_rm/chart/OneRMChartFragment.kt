package com.health.myapplication.ui.one_rm.chart


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.health.myapplication.R
import com.health.myapplication.databinding.FragmentBodyweightChartBinding
import com.health.myapplication.databinding.FragmentOneRmChartBinding
import com.health.myapplication.dialog.CalculatorDialog
import com.health.myapplication.listener.CalculatorDialogListener
import com.health.myapplication.ui.body_weight.chart.BodyWeightChartFragment
import com.health.myapplication.ui.one_rm.OneRMViewModel
import kotlinx.android.synthetic.main.fragment_one_rm_chart.*
import java.util.*

private const val TAG = "OneRMChartFragment"

class OneRMChartFragment : Fragment() {
    private val viewModel: OneRMViewModel by viewModels()
    private var dialog: CalculatorDialog? = null

    private val mContext: Context by lazy {
        requireContext()
    }
    private val colorMap: Map<String, Int> by lazy {
        mapOf(
                "blue" to ContextCompat.getColor(mContext, R.color.colorSub4),
                "red" to ContextCompat.getColor(mContext, R.color.colorSub),
                "black" to ContextCompat.getColor(mContext, R.color.colorPrimary)
        )
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentOneRmChartBinding>(layoutInflater,R.layout.fragment_one_rm_chart,null,false)
        val view = binding.root
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        view.apply {

        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lineChartAttributesInit()
        // x축 설정
        viewModel.dateList.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            if (it.isNotEmpty()) {
                line_chart.xAxis.valueFormatter = AxisValueFormatter(ArrayList(it))
            } else
                line_chart.xAxis.valueFormatter = AxisValueFormatter(ArrayList())
            line_chart.xAxis.setLabelCount(it.size, true)
        })

        viewModel.entries.observe(viewLifecycleOwner) {
            Log.d(TAG, "onViewCreated: ${it}")
            val squatSet = setLineDataAttributes(LineDataSet(it["squat"], "스쿼트"), colorMap["blue"]!!)
            val benchSet = setLineDataAttributes(LineDataSet(it["benchPress"], "벤치프레스"), colorMap["red"]!!)
            val deadSet = setLineDataAttributes(LineDataSet(it["deadLift"], "데드리프트"), colorMap["black"]!!)
            val lineData = LineData(listOf(
                squatSet,
                benchSet,
                deadSet
            ))
            line_chart.apply {
                data = lineData
                invalidate()
            }
        }

        barChartAttributesInit()

        viewModel.barEntries.observe(viewLifecycleOwner) {
            val barDataSet = BarDataSet(it, "3대운동")
            barDataSet.setColors(*intArrayOf(colorMap["blue"]!!, colorMap["red"]!!, colorMap["black"]!!))
            barDataSet.setDrawValues(true)

            val barData = BarData(barDataSet)
            barData.setDrawValues(true)

            horizontal_bar_chart.data = barData
            horizontal_bar_chart.invalidate()
        }

        viewModel.totalOneRM.observe(viewLifecycleOwner){

        }

        add_btn.setOnClickListener {
            dialog = CalculatorDialog(requireContext())
            dialog!!.setDialogListener(object : CalculatorDialogListener {
                // DialogListener 를 구현 추상 클래스이므로 구현 필수 -> dialog의 값을 전달 받음
                override fun onPositiveClicked() {}
                override fun onPositiveClicked(date: String, exercise: String, one_rm: Double) {
                    viewModel.insert(date, exercise, one_rm)
                    // if (addData(date, name, one_rm)) update()
                }

                override fun onNegativeClicked() {}
            })
            dialog!!.show()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    inner class AxisValueFormatter  // 생성자 초기화
    internal constructor(private val mDate: ArrayList<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return if (mDate.size == 1) mDate[0] else if (mDate.size == 0) "0" else if (mDate.size <= value.toInt()) mDate[mDate.size - 1] else if (value.toInt() == -1) mDate[0] else mDate[value.toInt()]
        }
    }

    private fun barChartAttributesInit() {
        val exerciseName = arrayOf("스퀏", "벤치", "데드")
        val formatter = IndexAxisValueFormatter(exerciseName)

        horizontal_bar_chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawLabels(true)
            valueFormatter = formatter
        }

        horizontal_bar_chart.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(true)
            setDrawAxisLine(false)
        }

        horizontal_bar_chart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawLabels(true)
            setDrawAxisLine(false)
        }


        horizontal_bar_chart.legend.isEnabled = false

        horizontal_bar_chart.apply {
            setDrawValueAboveBar(true)
            data = barData
            minimumHeight = 400
            setFitBars(true)
            setDrawGridBackground(false)
            description.isEnabled = false
            animateXY(1000, 1000)
            invalidate()
        }
    }

    private fun lineChartAttributesInit() {
        line_chart.apply {
            minimumHeight = 1500
            description.isEnabled = false
        }

        line_chart.xAxis.apply {
            textColor = Color.BLACK
            textSize = 10.0f
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            axisMinimum = 0f
        }

        line_chart.axisLeft.apply {
            textColor = Color.BLACK
            textSize = 10.0f
            setDrawGridLines(false)
            axisMaximum = 400f
            axisMinimum = 0f
            labelCount = 11
        }

        line_chart.axisRight.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
            textColor = Color.BLACK
            textSize = 10.0f
            axisMaximum = 400f
            axisMinimum = 0f
        }
    }

    private fun setLineDataAttributes(lineDataSet: LineDataSet, color : Int): LineDataSet {
        lineDataSet.apply {
            setCircleColor(color)
            setColor(color)
            circleHoleColor = color
            circleRadius = 3f
            valueTextSize = 10f
            lineWidth = 1.5f
        }
        return lineDataSet
    }

    companion object{
        private var INSTANCE : OneRMChartFragment? = null

        fun getInstance(): OneRMChartFragment {
            if(INSTANCE ==null)
                INSTANCE = OneRMChartFragment()
            return INSTANCE!!
        }
    }

}