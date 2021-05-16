package com.health.myapplication.ui.common_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.databinding.ItemDefaultViewBinding
import com.health.myapplication.entity.etc.BaseVo

class CommonItemAdapter(var list: List<BaseVo>, val itemClick: (BaseVo) -> Unit) : RecyclerView.Adapter<CommonItemAdapter.ViewHolder>() {
    constructor(itemClick: (BaseVo) -> Unit):this(ArrayList(),itemClick)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonItemAdapter.ViewHolder {
        val binding = ItemDefaultViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommonItemAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list :ArrayList<BaseVo>) {
        this.list=list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemDefaultViewBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClick(binding.baseModel!!)
            }
        }
        fun bind(model: BaseVo){
            binding.root.tag = model.id
            binding.baseModel = model

        }
    }
}