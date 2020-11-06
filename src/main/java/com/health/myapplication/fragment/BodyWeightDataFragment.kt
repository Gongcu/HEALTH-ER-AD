package com.health.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.view_model.BodyWeightViewModel
import com.health.myapplication.adapter.BodyWeightListAdapter
import com.health.myapplication.dialog.BodyWeightDialog
import com.health.myapplication.listener.BodyWeightDialogListener
import kotlinx.android.synthetic.main.fragment_bodyweight_data.*
import kotlin.collections.ArrayList


class BodyWeightDataFragment : Fragment() {
    private val viewModel: BodyWeightViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bodyweight_data, container, false)
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
        private var INSTANCE : BodyWeightDataFragment? = null

        fun getInstance():BodyWeightDataFragment{
            if(INSTANCE==null)
                INSTANCE = BodyWeightDataFragment()
            return INSTANCE!!
        }
    }
}