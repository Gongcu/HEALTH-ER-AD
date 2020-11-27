package com.health.myapplication.listener

import androidx.recyclerview.widget.RecyclerView

interface CommentItemClickListener {
    fun onReplyClick(holder: RecyclerView.ViewHolder,commentId: Int)
}
