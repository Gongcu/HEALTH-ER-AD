package com.health.myapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.health.myapplication.R
import com.health.myapplication.RecordViewModel
import com.health.myapplication.adapter.RecordTodayAdapter
import com.health.myapplication.calendar.OneDayDecorator
import com.health.myapplication.calendar.SaturdayDecorator
import com.health.myapplication.calendar.SundayDecorator
import com.health.myapplication.dialog.TrainingDataDialog
import com.health.myapplication.listener.DataListener
import com.health.myapplication.model.Record
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.add_btn
import kotlinx.android.synthetic.main.fragment_calendar.recycler_view
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class RecordCalendarFragment : Fragment() {
    val viewModel: RecordViewModel by viewModels()

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    @SuppressLint("SimpleDateFormat")
    private val sdfDay = SimpleDateFormat("dd")

    private var selectedDate: String = sdf.format(Date())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            calendarInit()
            recycler_view.adapter = RecordTodayAdapter(requireContext(), viewModel)
            update()
        }
    }

    private fun calendarInit() {
        calendar_view.state().edit().setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2030, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()

        calendar_view.addDecorators(
                SundayDecorator(),
                SaturdayDecorator(),
                OneDayDecorator())

        calendar_view.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
                val calendar = Calendar.getInstance()
                calendar.time = date.date
                day_text_view.text = sdfDay.format(date.date)
                str_day_text_view.text = dayToString(calendar[Calendar.DAY_OF_WEEK])
                selectedDate=sdf.format(date.date)
                (recycler_view.adapter as RecordTodayAdapter).submitList(ArrayList())

                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    val list = viewModel.getAllGeneralListRecordByDate(selectedDate)
                    viewModel.viewModelScope.launch(Dispatchers.Main) {
                        (recycler_view.adapter as RecordTodayAdapter).submitList(list)
                    }
                }
        })

        add_btn.setOnClickListener(View.OnClickListener {
            val dialog = TrainingDataDialog(requireContext())
            dialog.setDialogListener(DataListener { date, name, set, rep, weight ->
                viewModel.insert(selectedDate, name, set, rep, weight)
            })
            dialog.show()
        })
    }

    private fun dayToString(dayNum: Int): String? {
        var day: String? = null
        when (dayNum) {
            1 -> day = "Sun"
            2 -> day = "Mon"
            3 -> day = "Tue"
            4 -> day = "Wed"
            5 -> day = "Thu"
            6 -> day = "Fri"
            7 -> day = "Sat"
        }
        return day
    }

    suspend fun update() {
        val mDate = Date()
        val strDay = sdfDay.format(mDate)
        val calendar = Calendar.getInstance()
        calendar.time = mDate
        day_text_view.text = strDay
        str_day_text_view.text = dayToString(calendar[Calendar.DAY_OF_WEEK])
        //전체 데이터 옵저빙하고 변화 시 리스트 얻어와짐, 그 중에서 dateId로 필터링하여 리스트 제출
        viewModel.getAllRecord().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            var list: List<Record>
            viewModel.viewModelScope.launch(Dispatchers.IO) {
                val dateId = viewModel.getIdByDate(selectedDate)
                list = it.filter {
                    it.dateid == dateId
                }
                viewModel.viewModelScope.launch {
                    (recycler_view.adapter as RecordTodayAdapter).submitList(ArrayList(list))
                }
            }
        })
    }
}
