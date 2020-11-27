package com.health.myapplication.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.R
import com.health.myapplication.adapter.CommentAdapter
import com.health.myapplication.application.GlobalApplication
import com.health.myapplication.listener.CommentItemClickListener
import com.health.myapplication.model.Comment
import com.health.myapplication.model.GuidePost
import com.health.myapplication.view_model.CommunityViewModel
import kotlinx.android.synthetic.main.activity_community_post_detail.*
import kotlinx.android.synthetic.main.item_comment_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommunityPostDetailActivity : AppCompatActivity() {
    private val viewModel :CommunityViewModel by viewModels()
    private var postId = -1
    private var userId = -1
    private var REPLY_COMMENT_ID = -1
    val imm: InputMethodManager by lazy{
        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_post_detail)
        postId = intent.extras.getInt("ID")
        userId = (application as GlobalApplication).userId.toInt()

        //게시글 상세 조회 API
        viewModel.getPost(postId).enqueue(object : Callback<GuidePost> {
            override fun onResponse(call: Call<GuidePost>, response: Response<GuidePost>) {
                if (response.isSuccessful) {
                    val item = response.body()!!
                    title_text_view.text = item.title
                    content_text_view.text = item.content
                    datetime_text_view.text = item.createdAt
                    like_count_text_view.text = item.likeCount.toString()
                    comment_count_text_view.text = item.commentCount.toString()
                }
                Log.d("RESPONSE", response.body().toString())

            }

            override fun onFailure(call: Call<GuidePost>, t: Throwable) {
                Log.d("FAILURE", t.message)
            }
        })

        viewModel.getComment(postId,userId).enqueue(commentCallback)

        //댓글 작성 API
        comment_write_btn.setOnClickListener {
            if(REPLY_COMMENT_ID==-1){
                viewModel.writeComment(
                        postId,
                        Comment(userId, comment_edit_text.text.toString())
                ).enqueue(commentCallback)
            }else{
                viewModel.replyComment(
                        postId,
                        REPLY_COMMENT_ID,
                        Comment(userId, comment_edit_text.text.toString())
                ).enqueue(commentCallback)
            }
            comment_edit_text.setText("")
            comment_edit_text.hint = "댓글을 입력하세요."
            imm.hideSoftInputFromWindow(comment_edit_text.windowToken,0)
            REPLY_COMMENT_ID = -1
        }

        //게시글 좋아요 상태
        viewModel.getLikeState(postId,userId).enqueue(likeCallback)

        //게시글 좋아요 클릭 시 API 요청
        like_btn.setOnClickListener {viewModel.likePost(postId,userId).enqueue(likeCallback)}

        back_btn.setOnClickListener {finish()}
        recycler_view.setOnTouchListener(touchListener)
    }

    private val commentCallback = object: Callback<List<Comment>>{
        override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
            if(response.isSuccessful){
                recycler_view.adapter = CommentAdapter(response.body()!!, applicationContext,listener,viewModel)

            }
            Log.d("COMMENT_RESPONSE", response.body().toString())
        }

        override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
            recycler_view.adapter = CommentAdapter(ArrayList(), applicationContext,listener,viewModel)
            Log.d("COMMENT_FAILURE", t.message)
        }
    }

    private val likeCallback = object: Callback<Boolean>{
        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
            if(response.isSuccessful){
                if(response.body()==true){
                    ImageViewCompat.setImageTintList(like_btn, ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.colorSub)))
                }else{
                    ImageViewCompat.setImageTintList(like_btn, ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.colorDeepGray)))
                }
            }
            Log.d("RESPONSE", response.body().toString())
        }

        override fun onFailure(call: Call<Boolean>, t: Throwable) {
            Log.d("FAILURE", t.message)
        }
    }

    private val listener = object: CommentItemClickListener{
        override fun onReplyClick(holder: RecyclerView.ViewHolder, commentId: Int) {
            comment_edit_text.requestFocus()
            comment_edit_text.hint = "답글을 입력하세요."
            imm.showSoftInput(comment_edit_text,InputMethodManager.SHOW_IMPLICIT)
            REPLY_COMMENT_ID = commentId
        }
    }

    //답글 작성 요청 후 안할 경우 감지 위함.
    private val touchListener = object: View.OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            if(event!!.action == MotionEvent.ACTION_UP){
                REPLY_COMMENT_ID = -1
                comment_edit_text.hint = "댓글을 입력하세요."
                imm.hideSoftInputFromWindow(comment_edit_text.windowToken,0)
            }
            return false
        }
    }
}