package com.health.myapplication.adapter

import android.content.Context
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.ViewModel
import com.health.myapplication.databinding.ItemDataViewBinding
import com.health.myapplication.dialog.BodyWeightDialog
import com.health.myapplication.listener.BodyWeightDialogListener
import com.health.myapplication.model.BodyWeight


class BodyWeightListAdapter(val context:Context,val viewModel: ViewModel) : RecyclerView.Adapter<BodyWeightListAdapter.ViewHolder>() {
    private var list = ArrayList<BodyWeight>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyWeightListAdapter.ViewHolder {
        val binding = ItemDataViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BodyWeightListAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }
    fun setList(list: ArrayList<BodyWeight>) {
        this.list = list
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemDataViewBinding): RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener{
        fun bind(model: BodyWeight){
            binding.bodyWeight = model
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
                    var dialog: BodyWeightDialog = BodyWeightDialog(context, true)
                    dialog.setDialogListener (object: BodyWeightDialogListener{
                        override fun onPositiveClicked(weight: Double) {
                            viewModel.update(weight,binding.bodyWeight!!.date!!)
                        }
                    })
                    dialog.show()
                    dialog.weightEditText.setText(binding.bodyWeight!!.bodyweight.toString())
                }
                1002 -> {
                    viewModel.delete(binding.bodyWeight!!)
                }
            }
            true
        }
    }
}