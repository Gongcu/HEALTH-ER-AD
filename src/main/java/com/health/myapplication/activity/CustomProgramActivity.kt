package com.health.myapplication.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.CustomProgramViewModel
import com.health.myapplication.R
import com.health.myapplication.adapter.BasicDataAdapter
import com.health.myapplication.dialog.DeleteDialog
import com.health.myapplication.dialog.DivisionSelectDialog
import com.health.myapplication.listener.DeleteDialogListener
import com.health.myapplication.listener.DivisionSelectListener
import com.health.myapplication.model.BasicDataModel
import com.health.myapplication.model.CustomProgram
import kotlinx.android.synthetic.main.activity_custom_program.ad_view
import kotlinx.android.synthetic.main.activity_custom_program.add_btn
import kotlinx.android.synthetic.main.activity_custom_program.recycler_view
import java.util.*

class CustomProgramActivity : AppCompatActivity() {
    private val context: Context by lazy{this}
    private val viewModel :CustomProgramViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_program)
        ad_view.loadAd(AdRequest.Builder().build())

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = BasicDataAdapter {
            moveProgramInfoActivity(it)
        }

        viewModel.getCustomProgram().observe(this, androidx.lifecycle.Observer {
            (recycler_view.adapter as BasicDataAdapter).setList(listGenerator(ArrayList(it)))
        })

        add_btn.setOnClickListener(View.OnClickListener {
            val dialog = DivisionSelectDialog(this)
            dialog.setDialogListener(object: DivisionSelectListener{
                override fun onPositiveClicked(division: String) {
                    viewModel.insert(CustomProgram(null,division))
                }
            })
            dialog.show()
        })

        // 목록에서 항목을 왼쪽, 오른쪽 방향으로 스와이프 하는 항목을 처리
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 스와이프된 아이템의 아이디를 가져온다.
                val id = viewHolder.itemView.tag as Int
                val deleteDialog = DeleteDialog(context)
                deleteDialog.setDialogListener(object : DeleteDialogListener {
                    override fun onPositiveClicked() {
                        viewModel.deleteById(id)
                    }

                    override fun onNegativeClicked() {
                        (recycler_view.adapter as BasicDataAdapter).notifyDataSetChanged()
                    }
                })
                deleteDialog.setCancelable(false)
                deleteDialog.show()
            }
        }).attachToRecyclerView(recycler_view)
    }

    private fun moveProgramInfoActivity(item: BasicDataModel){
        val intent = Intent(context, ProgramInfoActivity::class.java)
        intent.putExtra("ID",item.id)
        Log.d("ID", "+"+item.id)
        startActivity(intent)
    }


    private fun listGenerator(list: ArrayList<CustomProgram>) : ArrayList<BasicDataModel> {
        val newList = ArrayList<BasicDataModel>()
        for (i in list.indices) {
            when (list[i].activity) {
                "무분할" -> newList.add(BasicDataModel(list[i].id!! ,"무분할", R.drawable.one))
                "2분할"  -> newList.add(BasicDataModel(list[i].id!! ,"2분할", R.drawable.two))
                "3분할"  -> newList.add(BasicDataModel(list[i].id!! ,"3분할", R.drawable.three))
                "4분할"  -> newList.add(BasicDataModel(list[i].id!! ,"4분할", R.drawable.four))
                "5분할"  -> newList.add(BasicDataModel(list[i].id!! ,"5분할", R.drawable.five))
                "6분할"  -> newList.add(BasicDataModel(list[i].id!! ,"6분할", R.drawable.six))
                "7분할"  -> newList.add(BasicDataModel(list[i].id!! ,"7분할", R.drawable.seven))
            }
        }
        return newList
    }
}