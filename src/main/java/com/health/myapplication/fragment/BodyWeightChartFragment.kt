package com.health.myapplication.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.health.myapplication.R
import com.health.myapplication.BodyWeightViewModel
import com.health.myapplication.dialog.BodyWeightDialog
import com.health.myapplication.listener.BodyWeightDialogListener
import com.health.myapplication.model.BodyWeight
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlin.collections.ArrayList


class BodyWeightChartFragment : Fragment() {
    private val viewModel: BodyWeightViewModel by viewModels()
    private var xLabelCount = 0
    private val entryList: ArrayList<Entry> = ArrayList()
    private val lineData: LineData = LineData()
    private lateinit var lineDataSet: LineDataSet
    private val xAxis: XAxis by lazy { line_chart.xAxis }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chart, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getWeight().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            line_chart.clear()
            xLabelCount = it.size
            xAxis.valueFormatter = AxisValueFormatter(it)
            entryList.clear()
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
            chartInit()
            line_chart.invalidate()

            if (it.size <= 1) change_text_view.text = "0.0"
            else change_text_view.text = String.format("%.1f", it[it.size - 1].bodyweight - it[0].bodyweight)

        })

        add_btn.setOnClickListener(View.OnClickListener {
            val dialog = BodyWeightDialog(requireContext(), false)
            dialog.setDialogListener (object: BodyWeightDialogListener {
                override fun onPositiveClicked(weight: Double) {
                    viewModel.findAndInsert(weight)
                }
            })
            dialog.show()
        })
        super.onViewCreated(view, savedInstanceState)
    }


    private fun chartInit(){
        //x축 설정
        xAxis.textColor=Color.BLACK
        xAxis.position=XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.axisMinimum=0f
        xAxis.setLabelCount(xLabelCount, true)


        // y축 설정
        line_chart.axisLeft.textColor = Color.BLACK
        line_chart.axisLeft.setDrawGridLines(false)
        line_chart.axisLeft.axisMaximum = 130f
        line_chart.axisLeft.axisMinimum = 30f
        line_chart.axisLeft.labelCount = 11

        line_chart.axisRight.setDrawLabels(false)
        line_chart.axisRight.setDrawAxisLine(false)
        line_chart.axisRight.setDrawGridLines(false)

        line_chart.minimumHeight=1500
        line_chart.description.isEnabled=false
        line_chart.data=lineData
        line_chart.invalidate()
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

        fun getInstance():BodyWeightChartFragment{
            if(INSTANCE==null)
                INSTANCE = BodyWeightChartFragment()
            return INSTANCE!!
        }
    }
}