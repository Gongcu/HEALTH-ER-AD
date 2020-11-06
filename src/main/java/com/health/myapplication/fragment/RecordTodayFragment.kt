package com.health.myapplication.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.health.myapplication.R
import com.health.myapplication.view_model.RecordViewModel
import com.health.myapplication.adapter.RecordTodayAdapter
import com.health.myapplication.model.Record
import com.health.myapplication.util.Util
import kotlinx.android.synthetic.main.fragment_record_today.add_btn
import kotlinx.android.synthetic.main.fragment_record_today.recycler_view
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecordTodayFragment : Fragment() {
    private val viewModel: RecordViewModel by viewModels()
    private lateinit var list : List<Record>
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
        recycler_view.adapter = RecordTodayAdapter(requireContext(), viewModel)
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        add_btn.setOnClickListener(View.OnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.showAddDialog(requireContext(),Util.getDate())
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch {
            viewModel.getAllRecord().observe(viewLifecycleOwner,observer)
        }
    }

    override fun onDetach() {
        super.onDetach()
        lifecycleScope.launch {
            viewModel.getAllRecord().removeObserver(observer)
        }
    }

    private val observer = androidx.lifecycle.Observer<List<Record>>{ newList->
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            val dateId = viewModel.getIdByDate(Util.getDate())
            list = newList.filter {
                it.dateid == dateId
            }
            Log.d("list",list.toString())
            viewModel.viewModelScope.launch {
                (recycler_view.adapter as RecordTodayAdapter).submitList(list)
            }
        }
    }
}