package com.health.myapplication.ui.record.today

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.ui.record.RecordViewModel
import com.health.myapplication.databinding.ItemRecordTodayBinding
import com.health.myapplication.entity.etc.DialogType
import com.health.myapplication.ui.record.dialog.RecordDialog
import com.health.myapplication.listener.RecordDialogListener
import com.health.myapplication.entity.record.Record

class RecordTodayAdapter(val context: Context, val viewModel: RecordViewModel) :ListAdapter<Record, RecordTodayAdapter.ViewHolder>(
    RecordDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecordTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    inner class ViewHolder(private val binding: ItemRecordTodayBinding): RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener{
        fun bind(model: Record){
            binding.record = model
            binding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            val edit = menu!!.add(Menu.NONE, 1001, 1, "편집")
            val delete = menu.add(Menu.NONE, 1002, 2, "삭제")
            edit.setOnMenuItemClickListener(onEditMenu)
            delete.setOnMenuItemClickListener(onEditMenu)
        }
        private val onEditMenu = MenuItem.OnMenuItemClickListener { item ->
            when (item.itemId) {
                1001 -> {
                    RecordDialog(context, DialogType.UPDATE).apply {
                        setRecordDialogListener(object : RecordDialogListener {
                            override fun onPositiveClicked(time: String, name: String, set: Int, rep: Int, weight: Double) {
                                viewModel.updateRecord(binding.record!!.id!!, set, rep, weight)
                            }
                        })
                        show()
                    }
                }
                1002 -> {
                    viewModel.deleteRecordById(binding.record!!.id!!)
                }
            }
            true
        }
    }

    companion object RecordDiffUtil: DiffUtil.ItemCallback<Record>(){
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return oldItem==newItem
        }
    }
}