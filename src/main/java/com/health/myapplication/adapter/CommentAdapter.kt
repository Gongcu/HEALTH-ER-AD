package com.health.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.R
import com.health.myapplication.application.GlobalApplication
import com.health.myapplication.databinding.ItemCommentViewBinding
import com.health.myapplication.databinding.ItemReplyCommentViewBinding
import com.health.myapplication.listener.CommentItemClickListener
import com.health.myapplication.model.Comment
import com.health.myapplication.view_model.CommunityViewModel
import kotlinx.android.synthetic.main.item_comment_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentAdapter(var list: List<Comment>,
                     val context: Context,
                     var commentItemClickListener: CommentItemClickListener?,
                     var viewModel: CommunityViewModel?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CommentItemClickListener {

    constructor(list: List<Comment>,
                context: Context):this(list,context,null,null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            REPLY -> {
                val binding = ItemReplyCommentViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
                return ReplyViewHolder(binding)
            }
            GENERAL ->{
                val binding = ItemCommentViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
                return ViewHolder(binding,this)
            }
            else ->{//cannot reach this
                val binding = ItemCommentViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
                return ViewHolder(binding,this)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder){
            holder.bind(list[position])
        }else if(holder is ReplyViewHolder){
            holder.bind(list[position])
        }
    }
    override fun getItemViewType(position: Int): Int {
        if(list.isNotEmpty()){
            return if(list[position].parent!=null)
                1
            else
                0
        }
        else
            return super.getItemViewType(position)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list :ArrayList<Comment>) {
        this.list=list
        notifyDataSetChanged()
    }

    override fun onReplyClick(holder: RecyclerView.ViewHolder,commentId: Int) {
        if(commentItemClickListener!=null)
            commentItemClickListener!!.onReplyClick(holder,commentId)
    }


    inner class ViewHolder(private val binding: ItemCommentViewBinding, val listener: CommentItemClickListener): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("ResourceAsColor")
        fun bind(model: Comment){
            binding.comment = model
            if(model.like==true)
                ImageViewCompat.setImageTintList(binding.root.like_btn, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSub)))
            else
                ImageViewCompat.setImageTintList(binding.root.like_btn, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGray)))
            binding.root.like_btn.setOnClickListener {
                viewModel!!.likeComment(model.id!!,(context.applicationContext as GlobalApplication).userId.toInt())
                        .enqueue(object: Callback<Boolean>{
                            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                                if(response.isSuccessful){
                                    if(response.body()==true){
                                        ImageViewCompat.setImageTintList(binding.root.like_btn, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSub)))
                                        model.likeCount = model.likeCount?.plus(1)
                                    }else{
                                        ImageViewCompat.setImageTintList(binding.root.like_btn, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGray)))
                                        model.likeCount = model.likeCount?.minus(1)
                                    }
                                    binding.comment = model
                                }
                                Log.d("RESPONSE", response.body().toString())
                            }

                            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                Log.d("Failure", t.message)
                            }
                        })
            }
            binding.root.reply_comment_btn.setOnClickListener {
                listener.onReplyClick(this,model.id!!)
            }
        }
    }

    inner class ReplyViewHolder(private val binding: ItemReplyCommentViewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(model: Comment){
            binding.comment = model
            if(model.like==true)
                ImageViewCompat.setImageTintList(binding.root.like_btn, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSub)))
            else
                ImageViewCompat.setImageTintList(binding.root.like_btn, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGray)))
            binding.root.like_btn.setOnClickListener {
                viewModel!!.likeComment(model.id!!,(context.applicationContext as GlobalApplication).userId.toInt())
                        .enqueue(object: Callback<Boolean>{
                            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                                if(response.isSuccessful){
                                    if(response.body()==true){
                                        ImageViewCompat.setImageTintList(binding.root.like_btn, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorSub)))
                                        model.likeCount = model.likeCount?.plus(1)
                                    }else{
                                        ImageViewCompat.setImageTintList(binding.root.like_btn, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGray)))
                                        model.likeCount = model.likeCount?.minus(1)
                                    }
                                }
                                binding.comment = model
                                Log.d("RESPONSE", response.body().toString())
                            }

                            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                                Log.d("Failure", t.message)
                            }
                        })
            }
        }
    }

    companion object{
        const val GENERAL = 0
        const val REPLY = 1
    }

}