package com.health.myapplication.ui.body_weight.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.databinding.FragmentBodyweightDataBinding
import com.health.myapplication.ui.body_weight.BodyWeightViewModel
import kotlinx.android.synthetic.main.fragment_bodyweight_data.view.*
import kotlin.collections.ArrayList


class BodyWeightRecordFragment : Fragment() {
    private val viewModel: BodyWeightViewModel by viewModels()
    private val adapter : BodyWeightAdapter by lazy {
        BodyWeightAdapter(requireContext(),viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentBodyweightDataBinding>(layoutInflater,R.layout.fragment_bodyweight_data,null,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val view = binding.root
        view.apply {
            ad_view.loadAd(AdRequest.Builder().build())
            recycler_view.adapter = adapter
            viewModel.weights.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                adapter.setList(ArrayList(it))
            })
        }
        return view
    }

    companion object{
        private var INSTANCE : BodyWeightRecordFragment? = null

        fun getInstance(): BodyWeightRecordFragment {
            if(INSTANCE ==null)
                INSTANCE = BodyWeightRecordFragment()
            return INSTANCE!!
        }
    }
}