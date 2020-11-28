package com.health.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.activity.CommunityPostDetailActivity
import com.health.myapplication.databinding.ItemPostViewBinding
import com.health.myapplication.model.GuideItem

class CommunityAdapter(var list: ArrayList<GuideItem>, var context: Context) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>() {
    constructor(context: Context):this(ArrayList(), context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapter.ViewHolder {
        val binding = ItemPostViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommunityAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @JvmName("setList1")
    fun setList(list: ArrayList<GuideItem>) {
        this.list=list
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun addItems(list: ArrayList<GuideItem>) {
        this.list.addAll(list)
        notifyItemRangeInserted(this.list.size, list.size)
    }

    inner class ViewHolder(private val binding: ItemPostViewBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        fun bind(model: GuideItem){
            binding.guide = model
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d("TOUCH","AFA")
            val intent = Intent(context, CommunityPostDetailActivity::class.java)
            intent.putExtra("ID", binding.guide!!.id)
            context.startActivity(intent)
        }
    }
}