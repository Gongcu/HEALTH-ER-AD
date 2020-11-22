package com.health.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.databinding.ItemDefaultViewBinding
import com.health.myapplication.databinding.ItemGuideTitleViewBinding
import com.health.myapplication.model.GuideItem

class GuideItemAdapter(var list: List<GuideItem>) : RecyclerView.Adapter<GuideItemAdapter.ViewHolder>() {
    init {
        list = ArrayList<GuideItem>();
    }
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
                //itemclick
            }
        }
    }
}