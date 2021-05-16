package com.health.myapplication.ui.one_rm.record

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
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.databinding.FragmentOneRmRecordBinding
import com.health.myapplication.entity.one_rm.OneRmDate
import com.health.myapplication.ui.one_rm.OneRmViewModel
import kotlinx.android.synthetic.main.fragment_one_rm_record.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OneRmRecordFragment : Fragment() {

    private val viewModel : OneRmViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentOneRmRecordBinding>(layoutInflater,R.layout.fragment_one_rm_record,container,false)
        val view = binding.root

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        view.adView.loadAd(AdRequest.Builder().build())
        view.add_btn.setOnClickListener {
            viewModel.showAddDialog(requireActivity())
        }

        viewModel.oneRmRecordList.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                var dates : List<OneRmDate> = listOf()
                val job = lifecycleScope.launch(Dispatchers.IO) {
                    dates = viewModel.getAllDate()
                }
                job.join()
                dates = dates.sortedBy {
                    it.date
                }.reversed()
                view.recycler_view.adapter = OneRmDateAdapter(requireActivity(), dates, it,viewModel)
            }
        }

        viewModel.crudAlert.observe(viewLifecycleOwner) {
            val toastText: String  = when (it) {
                OneRmViewModel.CRUD_ALERT.EXIST -> "오늘 해당 운동이 이미 기록되었습니다. 수정이 필요합니다."
                OneRmViewModel.CRUD_ALERT.INSERT -> "기록 완료"
                OneRmViewModel.CRUD_ALERT.DELETE -> "삭제 완료"
                OneRmViewModel.CRUD_ALERT.UPDATE -> "수정 완료"
            }
            Toast.makeText(activity,toastText, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }


    companion object{
        private var INSTANCE : OneRmRecordFragment? = null

        fun getInstance(): OneRmRecordFragment {
            if(INSTANCE ==null)
                INSTANCE = OneRmRecordFragment()
            return INSTANCE!!
        }
    }
}