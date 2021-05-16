package com.health.myapplication.ui.recommend_program

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.ui.recommend_program.program_info.ProgramInfoActivity
import kotlinx.android.synthetic.main.activity_program_recommend.*

class ProgramRecommendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_recommend)
        ad_view.loadAd(AdRequest.Builder().build())

        card_view1.setOnClickListener(onClickListener)
        card_view2.setOnClickListener(onClickListener)
        card_view3.setOnClickListener(onClickListener)
        card_view4.setOnClickListener(onClickListener)
    }
    private val onClickListener = View.OnClickListener {
        val intent = Intent(this, ProgramInfoActivity::class.java)
        when(it.id){
            R.id.card_view1 -> intent.putExtra("PROGRAM", RECOMMEND_PROGRAM_1)
            R.id.card_view2 -> intent.putExtra("PROGRAM", RECOMMEND_PROGRAM_2)
            R.id.card_view3 -> intent.putExtra("PROGRAM", RECOMMEND_PROGRAM_3)
            R.id.card_view4 -> intent.putExtra("PROGRAM", RECOMMEND_PROGRAM_4)
        }
        startActivity(intent)
    }

    companion object{
        const val RECOMMEND_PROGRAM_1 =1
        const val RECOMMEND_PROGRAM_2 =2
        const val RECOMMEND_PROGRAM_3 =3
        const val RECOMMEND_PROGRAM_4 =4
    }
}