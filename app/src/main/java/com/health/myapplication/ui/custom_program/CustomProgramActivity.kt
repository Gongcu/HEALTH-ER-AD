package com.health.myapplication.ui.custom_program

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.health.myapplication.R
import com.health.myapplication.ui.common_adapter.CommonItemAdapter
import com.health.myapplication.dialog.DeleteDialog
import com.health.myapplication.dialog.DivisionSelectDialog
import com.health.myapplication.listener.DeleteDialogListener
import com.health.myapplication.listener.DivisionSelectListener
import com.health.myapplication.entity.etc.BaseVo
import com.health.myapplication.entity.custom_program.CustomProgram
import com.health.myapplication.ui.custom_program.program_info.CustomProgramInfoActivity
import kotlinx.android.synthetic.main.activity_custom_program.ad_view
import kotlinx.android.synthetic.main.activity_custom_program.add_btn
import kotlinx.android.synthetic.main.activity_custom_program.recycler_view
import java.util.*

class CustomProgramActivity : AppCompatActivity() {
    private val context: Context by lazy{this}
    private val viewModel : CustomProgramViewModel by viewModels()
    private val dialog : DivisionSelectDialog by lazy{
        DivisionSelectDialog(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_program)
        ad_view.loadAd(AdRequest.Builder().build())

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = CommonItemAdapter {
            moveCustomProgramInfoActivity(it)
        }

        viewModel.getCustomProgram().observe(this, androidx.lifecycle.Observer {
            (recycler_view.adapter as CommonItemAdapter).setList(listGenerator(ArrayList(it)))
        })

        dialog.setDialogListener(object: DivisionSelectListener{
            override fun onPositiveClicked(division: Int) {
                viewModel.insert(CustomProgram(null,division))
            }
        })

        add_btn.setOnClickListener(View.OnClickListener {
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
                        (recycler_view.adapter as CommonItemAdapter).notifyDataSetChanged()
                    }
                })
                deleteDialog.setCancelable(false)
                deleteDialog.show()
            }
        }).attachToRecyclerView(recycler_view)
    }

    private fun moveCustomProgramInfoActivity(item: BaseVo){
        val intent = Intent(context, CustomProgramInfoActivity::class.java)
        intent.putExtra("ID",item.id)
        startActivity(intent)
    }


    private fun listGenerator(list: ArrayList<CustomProgram>) : ArrayList<BaseVo> {
        val newList = ArrayList<BaseVo>()
        for (i in list.indices) {
            when (list[i].activity) {
                1 -> newList.add(BaseVo(list[i].id!! ,"무분할", R.drawable.one))
                2  -> newList.add(BaseVo(list[i].id!! ,"2분할", R.drawable.two))
                3  -> newList.add(BaseVo(list[i].id!! ,"3분할", R.drawable.three))
                4  -> newList.add(BaseVo(list[i].id!! ,"4분할", R.drawable.four))
                5  -> newList.add(BaseVo(list[i].id!! ,"5분할", R.drawable.five))
                6  -> newList.add(BaseVo(list[i].id!! ,"6분할", R.drawable.six))
                7  -> newList.add(BaseVo(list[i].id!! ,"7분할", R.drawable.seven))
            }
        }
        return newList
    }
}