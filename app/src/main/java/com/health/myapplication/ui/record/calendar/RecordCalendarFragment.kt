package com.health.myapplication.ui.record.calendar

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
import com.health.myapplication.ui.record.RecordViewModel
import com.health.myapplication.ui.record.today.RecordTodayAdapter
import com.health.myapplication.ui.record.calendar.decorator.OneDayDecorator
import com.health.myapplication.ui.record.calendar.decorator.SaturdayDecorator
import com.health.myapplication.ui.record.calendar.decorator.SundayDecorator
import com.health.myapplication.model.record.Record
import com.health.myapplication.util.Util
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_record_calendar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class RecordCalendarFragment : Fragment() {
    private val viewModel: RecordViewModel by viewModels()

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
        return inflater.inflate(R.layout.fragment_record_calendar, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarInit()
        recycler_view.adapter = RecordTodayAdapter(requireContext(), viewModel)
        viewUpdate()
    }


    //call after onViewCreated
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch {
            viewModel.getAllRecord().observe(viewLifecycleOwner, observer)
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
            OneDayDecorator()
        )

        calendar_view.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            viewModel.viewModelScope.launch(Dispatchers.Main) {
                val calendar = Calendar.getInstance()
                calendar.time = date.date
                day_text_view.text = sdfDay.format(date.date)
                str_day_text_view.text = Util.dayToString(calendar[Calendar.DAY_OF_WEEK])
                selectedDate = sdf.format(date.date)

                val list = viewModel.viewModelScope.async(Dispatchers.IO) {
                    return@async viewModel.getAllGeneralListRecordByDate(selectedDate)
                }.await()
                Log.d("date,list",selectedDate+" "+list.toString())
                viewModel.viewModelScope.launch(Dispatchers.Main) {
                    (recycler_view.adapter as RecordTodayAdapter).submitList(list)
                }
            }
        })

        add_btn.setOnClickListener(View.OnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.showAddDialog(requireContext(),selectedDate)
            }
        })
    }

    private fun viewUpdate() {
        val mDate = Date()
        val strDay = sdfDay.format(mDate)
        val calendar = Calendar.getInstance()
        calendar.time = mDate
        day_text_view.text = strDay
        str_day_text_view.text = Util.dayToString(calendar[Calendar.DAY_OF_WEEK])
    }

    private val observer = androidx.lifecycle.Observer<List<Record>>{ it->
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
    }

    override fun onDetach() {
        super.onDetach()
        lifecycleScope.launch {
            viewModel.getAllRecord().removeObserver(observer)
        }
    }

}
