package com.health.myapplication.ui.common_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.databinding.ItemDefaultViewBinding
import com.health.myapplication.model.etc.BaseModel

class CommonItemAdapter(var list: List<BaseModel>, val itemClick: (BaseModel) -> Unit) : RecyclerView.Adapter<CommonItemAdapter.ViewHolder>() {
    constructor(itemClick: (BaseModel) -> Unit):this(ArrayList(),itemClick)

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

    fun setList(list :ArrayList<BaseModel>) {
        this.list=list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemDefaultViewBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClick(binding.baseModel!!)
            }
        }
        fun bind(model: BaseModel){
            binding.root.tag = model.id
            binding.baseModel = model

        }
    }
}