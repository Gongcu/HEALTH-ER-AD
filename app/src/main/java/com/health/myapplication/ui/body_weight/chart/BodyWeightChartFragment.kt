package com.health.myapplication.ui.body_weight.chart

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.health.myapplication.R
import com.health.myapplication.databinding.FragmentBodyweightChartBinding
import com.health.myapplication.ui.body_weight.BodyWeightViewModel
import com.health.myapplication.model.body_weight.BodyWeight
import kotlinx.android.synthetic.main.fragment_bodyweight_chart.*
import kotlinx.android.synthetic.main.fragment_bodyweight_chart.view.*
import kotlin.collections.ArrayList


class BodyWeightChartFragment : Fragment() {
    private val viewModel: BodyWeightViewModel by viewModels()
    private var xLabelCount = 0
    private val entryList: ArrayList<Entry> = ArrayList()
    private val lineData: LineData = LineData()
    private lateinit var lineDataSet: LineDataSet
    private val xAxis: XAxis by lazy { line_chart.xAxis }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentBodyweightChartBinding>(layoutInflater,R.layout.fragment_bodyweight_chart,null,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val view = binding.root
        viewModel.weights.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            //clear
            line_chart.clear()
            entryList.clear()

            //setSize
            xLabelCount = it.size
            xAxis.valueFormatter = AxisValueFormatter(it)

            //add data
            for (i in it.indices)
                entryList.add(Entry(i.toFloat(), it[i].bodyweight.toFloat()))
            lineDataSet = LineDataSet(entryList, "몸무게")
            if (lineData.dataSets.isEmpty()) {
                lineData.addDataSet(lineDataSet)
            } else {
                lineData.removeDataSet(0)
                lineData.addDataSet(lineDataSet)
            }
            drawLineData(lineDataSet)
            line_chart.data = lineData

            //draw chart
            chartInit(view.line_chart)
            line_chart.invalidate()

            if (it.size <= 1) change_text_view.text = "0.0"
            else change_text_view.text = String.format("%.1f", it[it.size - 1].bodyweight - it[0].bodyweight)

        })

        return view
    }


    private fun chartInit(lineChart: LineChart){
        //x축 설정
        lineChart.xAxis.textColor=Color.BLACK
        lineChart.xAxis.position=XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.axisMinimum=0f
        lineChart.xAxis.setLabelCount(xLabelCount, true)


        // y축 설정
        lineChart.axisLeft.textColor = Color.BLACK
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisLeft.axisMaximum = 130f
        lineChart.axisLeft.axisMinimum = 30f
        lineChart.axisLeft.labelCount = 11

        lineChart.axisRight.setDrawLabels(false)
        lineChart.axisRight.setDrawAxisLine(false)
        lineChart.axisRight.setDrawGridLines(false)

        lineChart.minimumHeight=1500
        lineChart.description.isEnabled=false
        lineChart.data=lineData
        lineChart.invalidate()
    }


    private fun drawLineData(lineDataSet: LineDataSet) {
        lineDataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.colorSub4))
        lineDataSet.color=ContextCompat.getColor(requireContext(), R.color.colorSub4)
        lineDataSet.circleHoleColor=ContextCompat.getColor(requireContext(), R.color.colorSub4)
        lineDataSet.lineWidth=2.0f
        lineDataSet.circleRadius = 3f
        lineDataSet.valueTextSize = 10.0f
    }

    class AxisValueFormatter
    internal constructor(private val list: List<BodyWeight>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String? {
            return if (list.size == 1) list[0].date else if (list.size == 0) "" else if (list.size <= value.toInt()) {
                list[list.size - 1].date
            } else if (value.toInt() == -1) {
                list[0].date
            } else {
                list[value.toInt()].date
            }
        }
    }

    companion object{
        private var INSTANCE : BodyWeightChartFragment? = null

        fun getInstance(): BodyWeightChartFragment {
            if(INSTANCE ==null)
                INSTANCE = BodyWeightChartFragment()
            return INSTANCE!!
        }
    }
}