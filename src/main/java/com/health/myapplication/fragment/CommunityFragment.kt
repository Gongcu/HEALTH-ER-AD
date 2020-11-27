package com.health.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.health.myapplication.R
import com.health.myapplication.activity.CommunityWriteActivity
import com.health.myapplication.adapter.CommunityAdapter
import com.health.myapplication.adapter.HotPostAdapter
import com.health.myapplication.model.GuideItem
import com.health.myapplication.view_model.CommunityViewModel
import kotlinx.android.synthetic.main.fragment_exercise_guide_community.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommunityFragment : Fragment() {
    val viewModel:CommunityViewModel by viewModels()
    val EXERCISE :String by lazy {
        requireActivity().intent.extras!!.getString("name")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise_guide_community, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getHotPostList(EXERCISE).enqueue(object: Callback<List<GuideItem>>{
            override fun onResponse(call: Call<List<GuideItem>>, response: Response<List<GuideItem>>) {
                if(response.isSuccessful)
                    recycler_view_hot_post.adapter = HotPostAdapter(response.body()!!,requireContext())
            }
            override fun onFailure(call: Call<List<GuideItem>>, t: Throwable) {
            }
        })

        viewModel.getList(EXERCISE).enqueue(object: Callback<List<GuideItem>>{
            override fun onResponse(call: Call<List<GuideItem>>, response: Response<List<GuideItem>>) {
                if(response.isSuccessful)
                    recycler_view.adapter = CommunityAdapter(response.body()!!,requireContext())
            }
            override fun onFailure(call: Call<List<GuideItem>>, t: Throwable) {
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh_layout.setOnRefreshListener {
            refresh()
            swipe_refresh_layout.isRefreshing=false
        }

        add_btn.setOnClickListener {
            val intent = Intent(requireActivity(), CommunityWriteActivity::class.java)
            intent.putExtra("EXERCISE",EXERCISE)
            startActivity(intent)
        }
    }

    private fun refresh(){
        viewModel.getList(EXERCISE).enqueue(object: Callback<List<GuideItem>>{
            override fun onResponse(call: Call<List<GuideItem>>, response: Response<List<GuideItem>>) {
                if(response.isSuccessful)
                    (recycler_view.adapter as CommunityAdapter).setList(ArrayList(response.body()))
            }
            override fun onFailure(call: Call<List<GuideItem>>, t: Throwable) {
                Log.e("LIST",t.message)
            }
        })
    }


}