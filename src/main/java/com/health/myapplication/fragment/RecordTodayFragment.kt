package com.health.myapplication.fragment

import android.content.Context
import android.os.Bundle
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
import com.health.myapplication.dialog.TrainingDataDialog
import com.health.myapplication.listener.DataListener
import com.health.myapplication.model.Record
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.android.synthetic.main.fragment_record.add_btn
import kotlinx.android.synthetic.main.fragment_record.recycler_view
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class RecordTodayFragment : Fragment() {
    private val viewModel: RecordViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch{
            val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
            recycler_view.adapter = RecordTodayAdapter(requireContext(),viewModel)
            viewModel.getAllRecord().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                var list: List<Record>
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    val dateId = viewModel.getIdByDate(date)
                    list = it.filter {
                        it.dateid == dateId
                    }
                    viewModel.viewModelScope.launch {
                        (recycler_view.adapter as RecordTodayAdapter).submitList(ArrayList(list))
                    }
                }
            })
        }

        add_btn.setOnClickListener(View.OnClickListener {
            val dialog = TrainingDataDialog(requireContext())
            dialog.setDialogListener(DataListener { date, name, set, rep, weight ->
                viewModel.insert(date, name, set, rep, weight)
            })
            dialog.show()
        })
    }

    companion object {
    }
}