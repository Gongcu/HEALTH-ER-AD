package com.health.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemAnimator
import androidx.recyclerview.widget.SimpleItemAnimator
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
    var OFFSET = 0
    val list = ArrayList<GuideItem>()
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
        viewModel.getHotPostList(EXERCISE).enqueue(object : Callback<List<GuideItem>> {
            override fun onResponse(call: Call<List<GuideItem>>, response: Response<List<GuideItem>>) {
                if (response.isSuccessful)
                    recycler_view_hot_post.adapter = HotPostAdapter(response.body()!!, requireContext())
            }

            override fun onFailure(call: Call<List<GuideItem>>, t: Throwable) {
            }
        })

        if(recycler_view.adapter==null) {
            recycler_view.adapter = CommunityAdapter(requireContext())
            recycler_view.addOnScrollListener(onScrollListener)
        }
        viewModel.getList(EXERCISE, OFFSET).enqueue(getListCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh_layout.setOnRefreshListener { refresh() }

        add_btn.setOnClickListener {
            val intent = Intent(requireActivity(), CommunityWriteActivity::class.java)
            intent.putExtra("EXERCISE", EXERCISE)
            startActivity(intent)
        }
    }

    private fun refresh(){
        OFFSET=0
        viewModel.getList(EXERCISE, OFFSET).enqueue(getListCallback)
    }

    private val onScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            //item 수가 적어서 화면에 아이템을 다 표시하여 스크롤이 첨 부터 불가능할때도 !canScrollVertically는 호출됨
            if(!recycler_view.canScrollVertically(1) && dy>0){
                if(OFFSET != 0 && OFFSET%10==0){
                    viewModel.getList(EXERCISE, OFFSET).enqueue(getListCallback)
                }
            }

            if(!recycler_view.canScrollVertically(1) && dy<0){
                //refresh()
            }
        }
    }

    private val getListCallback = object: Callback<List<GuideItem>>{
        override fun onResponse(call: Call<List<GuideItem>>, response: Response<List<GuideItem>>) {
            if(response.isSuccessful) {
                val list = response.body()
                if(OFFSET==0) {
                    (recycler_view.adapter as CommunityAdapter).setList(ArrayList(list!!))
                }
                else {
                    (recycler_view.adapter as CommunityAdapter).addItems(ArrayList(list!!))
                    Log.d(TAG, "OFFSET:0 - SET LIST")
                }
                OFFSET+=list!!.size
            }
            swipe_refresh_layout.isRefreshing=false
            Log.d(TAG, response.body().toString())
        }
        override fun onFailure(call: Call<List<GuideItem>>, t: Throwable) {
            Log.w(TAG, t.message)
            swipe_refresh_layout.isRefreshing=false
        }
    }

    companion object{
        val TAG = "CommunityFragment"
    }
}