package com.health.myapplication.ui.one_rm.chart


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.health.myapplication.R
import com.health.myapplication.databinding.FragmentOneRmChartBinding
import com.health.myapplication.ui.one_rm.OneRmViewModel
import kotlinx.android.synthetic.main.fragment_one_rm_chart.theThree
import kotlinx.android.synthetic.main.fragment_one_rm_chart.view.*
import kotlinx.coroutines.launch


class OneRmChartFragment : Fragment() {
    private val viewModel: OneRmViewModel by viewModels()
    private val exerciseName = arrayOf("스퀏", "벤치", "데드")
    private val formatter = IndexAxisValueFormatter(exerciseName)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //SET DATA BINDING
        val binding = DataBindingUtil.inflate<FragmentOneRmChartBinding>(layoutInflater,R.layout.fragment_one_rm_chart,null,false)
        val view = binding.root
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        //INIT CHART
        initLineChartAttributes(view.line_chart)
        initBarChartAttributes(view.horizontal_bar_chart)

        //OBSERVING DATA
        viewModel.oneRmDateList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            val strList: List<String> = it.map { OneRMDate -> OneRMDate.date!! }
            if (it.isNotEmpty()) {
                view.line_chart.xAxis.valueFormatter = AxisValueFormatter(strList)
            } else
                view.line_chart.xAxis.valueFormatter = AxisValueFormatter(strList)
            view.line_chart.xAxis.setLabelCount(it.size, true)
        })


        viewModel.oneRmRecordList.observe(viewLifecycleOwner) {
            theThree.text = viewModel.getTotalOneRm(it).toString()

            lifecycleScope.launch {
                view.line_chart.clear()
                view.line_chart.data = viewModel.getChartLineData(it)
                view.line_chart.invalidate()
            }

            lifecycleScope.launch {
                view.horizontal_bar_chart.clear()
                view.horizontal_bar_chart.data = viewModel.getBarData(it)
                view.horizontal_bar_chart.invalidate()
            }
        }

        viewModel.crudAlert.observe(viewLifecycleOwner) {
            val toastText: String  = when (it) {
                OneRmViewModel.CRUD_ALERT.EXIST -> "오늘 해당 운동이 이미 기록되었습니다. 수정이 필요합니다."
                OneRmViewModel.CRUD_ALERT.INSERT -> "기록 완료"
                else -> ""
            }
            Toast.makeText(activity,toastText,Toast.LENGTH_SHORT).show()
        }

        view.add_btn.setOnClickListener {
            viewModel.showAddDialog(requireActivity())
        }
        return view
    }



    inner class AxisValueFormatter  // 생성자 초기화
    internal constructor(private val mDate: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return if (mDate.size == 1) mDate[0] else if (mDate.size == 0) "0" else if (mDate.size <= value.toInt()) mDate[mDate.size - 1] else if (value.toInt() == -1) mDate[0] else mDate[value.toInt()]
        }
    }

    private fun initBarChartAttributes(barChart: BarChart) {
        barChart.apply {
            legend.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawLabels(true)
                valueFormatter = formatter
            }

            axisRight.apply {
                setDrawGridLines(false)
                setDrawLabels(true)
                setDrawAxisLine(false)
            }

            axisLeft.apply {
                setDrawGridLines(false)
                setDrawLabels(true)
                setDrawAxisLine(false)
            }

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

    private fun initLineChartAttributes(lineChart: LineChart) {
        lineChart.apply {
            minimumHeight = 1500
            description.isEnabled = false

            xAxis.apply {
                textColor = Color.BLACK
                textSize = 10.0f
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                axisMinimum = 0f
            }

            axisLeft.apply {
                textColor = Color.BLACK
                textSize = 10.0f
                setDrawGridLines(false)
                axisMaximum = 400f
                axisMinimum = 0f
                labelCount = 11
            }

            axisRight.apply {
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
                textColor = Color.BLACK
                textSize = 10.0f
                axisMaximum = 400f
                axisMinimum = 0f
            }
        }
    }

    companion object{
        private var INSTANCE : OneRmChartFragment? = null

        fun getInstance(): OneRmChartFragment {
            if(INSTANCE ==null)
                INSTANCE = OneRmChartFragment()
            return INSTANCE!!
        }
    }

}