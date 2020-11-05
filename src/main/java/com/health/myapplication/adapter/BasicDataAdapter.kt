package com.health.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.databinding.ItemDefaultViewBinding
import com.health.myapplication.model.BasicDataModel

class BasicDataAdapter(var list: List<BasicDataModel>, val itemClick: (BasicDataModel) -> Unit) : RecyclerView.Adapter<BasicDataAdapter.ViewHolder>() {
    constructor(itemClick: (BasicDataModel) -> Unit):this(ArrayList(),itemClick)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicDataAdapter.ViewHolder {
        val binding = ItemDefaultViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasicDataAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list :ArrayList<BasicDataModel>) {
        this.list=list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemDefaultViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(model: BasicDataModel){
            binding.root.tag = model.id
            binding.basicModel = model
            binding.root.setOnClickListener {
                itemClick(model)
            }
        }
    }
}