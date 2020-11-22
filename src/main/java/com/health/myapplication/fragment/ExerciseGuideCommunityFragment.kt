package com.health.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.health.myapplication.R
import com.health.myapplication.adapter.GuideItemAdapter
import kotlinx.android.synthetic.main.fragment_exercise_guide_community.*


class ExerciseGuideCommunityFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_guide_community, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //recycler_view.adapter = GuideItemAdapter()
        swipe_refresh_layout.setOnRefreshListener {
            //(recycler_view.adapter as GuideItemAdapter).list = ~~

            swipe_refresh_layout.isRefreshing=false
        }
    }

}