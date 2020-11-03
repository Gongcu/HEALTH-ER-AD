package com.health.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.databinding.ItemDefaultViewBinding
import com.health.myapplication.model.BasicModel

class BasicAdapter(val list: List<BasicModel>,val itemClick: (BasicModel) -> Unit) : RecyclerView.Adapter<BasicAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicAdapter.ViewHolder {
        val binding = ItemDefaultViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BasicAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemDefaultViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(model: BasicModel){
            binding.basicModel = model
            binding.root.setOnClickListener {
                itemClick(model)
            }
        }
    }
}