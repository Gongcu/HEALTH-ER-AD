package com.health.myapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.BodyWeightViewModel
import com.health.myapplication.adapter.BodyWeightListAdapter
import com.health.myapplication.dialog.BodyWeightDialog
import com.health.myapplication.listener.BodyWeightDialogListener
import com.health.myapplication.model.BodyWeight
import kotlinx.android.synthetic.main.fragment_data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DataFragment : Fragment() {
    private val viewModel: BodyWeightViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ad_view.loadAd(AdRequest.Builder().build())
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = BodyWeightListAdapter(requireContext(),viewModel)
        viewModel.getWeight().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            (recycler_view.adapter as BodyWeightListAdapter).setList(ArrayList(it))
        })
        add_btn.setOnClickListener {
            val dialog = BodyWeightDialog(requireContext(), false)
            dialog.setDialogListener (object: BodyWeightDialogListener{
                override fun onPositiveClicked(weight: Double) {
                    viewModel.findAndInsert(weight)
                }
            })
            dialog.show()
        }
        super.onViewCreated(view, savedInstanceState)
    }


    companion object{
        private var INSTANCE : DataFragment? = null

        fun getInstance():DataFragment{
            if(INSTANCE==null)
                INSTANCE = DataFragment()
            return INSTANCE!!
        }
    }
}