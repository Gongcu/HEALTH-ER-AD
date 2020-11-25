package com.health.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.activity.CommunityPostDetailActivity
import com.health.myapplication.databinding.ItemDefaultViewBinding
import com.health.myapplication.databinding.ItemGuideTitleViewBinding
import com.health.myapplication.model.GuideItem

class GuideItemAdapter(var list: List<GuideItem>, val context: Context) : RecyclerView.Adapter<GuideItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideItemAdapter.ViewHolder {
        val binding = ItemGuideTitleViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuideItemAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list :ArrayList<GuideItem>) {
        this.list=list
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemGuideTitleViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(model: GuideItem){
            binding.guide = model
            binding.root.setOnClickListener {
                val intent = Intent(context,CommunityPostDetailActivity::class.java)
                intent.putExtra("ID",model.id)
                context.startActivity(intent)
            }
        }
    }
}