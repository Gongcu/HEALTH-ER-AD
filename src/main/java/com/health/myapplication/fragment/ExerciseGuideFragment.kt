package com.health.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.adapter.HotPostAdapter
import com.health.myapplication.model.GuideItem
import com.health.myapplication.view_model.CommunityViewModel
import com.health.myapplication.view_pager.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_exercise_guide.*
import kotlinx.android.synthetic.main.fragment_exercise_guide.recycler_view_hot_post
import kotlinx.android.synthetic.main.fragment_exercise_guide_community.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class ExerciseGuideFragment : Fragment() {
    var currentPage:Int = 0
    val viewModel : CommunityViewModel by viewModels()
    val EXERCISE :String by lazy {
        requireActivity().intent.extras!!.getString("name")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_exercise_guide, container, false)
    }

    override fun onResume() {
        super.onResume()
        /*
        viewModel.getHotPostList(EXERCISE).enqueue(object: Callback<List<GuideItem>> {
            override fun onResponse(call: Call<List<GuideItem>>, response: Response<List<GuideItem>>) {
                if(response.isSuccessful)
                    recycler_view_hot_post.adapter = HotPostAdapter(response.body()!!,requireContext())
            }
            override fun onFailure(call: Call<List<GuideItem>>, t: Throwable) {
            }
        })*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ad_view.loadAd(AdRequest.Builder().build())
        exercise_name_text_view.text = "${requireActivity().intent.extras!!.getString("name")} 자세"
        how_to_text_view.text = requireActivity().intent.extras!!.getString("desc")
        tip_text_view.text = "Tip. ${requireActivity().intent.extras!!.getString("tip")}"

        val drawable = arrayOf(
                ContextCompat.getDrawable(requireContext(), requireActivity().intent.extras!!.getInt("imageR")),
                ContextCompat.getDrawable(requireContext(), requireActivity().intent.extras!!.getInt("imageF"))
        )
        view_pager.adapter = ViewPagerAdapter(requireContext(),drawable)

        tab_layout.setupWithViewPager(view_pager, true)

        timer(period=2500) {
            lifecycleScope.launch(Dispatchers.Main) {
                if(view_pager!=null) {
                    currentPage = view_pager.currentItem
                    if (currentPage == 0) {
                        view_pager.setCurrentItem(1, true)
                    } else {
                        view_pager.setCurrentItem(0, true)
                    }
                }
            }
        }
    }

}