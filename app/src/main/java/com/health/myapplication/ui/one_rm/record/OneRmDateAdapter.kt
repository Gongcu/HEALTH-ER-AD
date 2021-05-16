package com.health.myapplication.ui.one_rm.record

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.health.myapplication.R
import com.health.myapplication.listener.AdapterListener
import com.health.myapplication.entity.one_rm.OneRmDate
import com.health.myapplication.entity.one_rm.OneRmRecord
import com.health.myapplication.ui.one_rm.OneRmViewModel

class OneRmDateAdapter(
        private val mContext: Context,
        private val oneRMDates: List<OneRmDate>,
        private val oneRMRecords: List<OneRmRecord>,
        private val viewModel: OneRmViewModel
) : RecyclerView.Adapter<OneRmDateAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.item_record_date, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = oneRMDates[position]
        holder.bind(item,oneRMRecords);
    }

    override fun getItemCount(): Int {
        return oneRMDates.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val button: Button = itemView.findViewById(R.id.button)
        private val date: TextView = itemView.findViewById(R.id.date_text)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_view)
        private lateinit var adapter: OneRmRecordAdapter

        fun bind(item: OneRmDate, oneRMRecords: List<OneRmRecord>) {
            date.text = item.date
            itemView.tag = item.id

            val list = oneRMRecords.filter { it.dateId==item.id }
            adapter = OneRmRecordAdapter(mContext, list)
            adapter.setAdapterListener(object: AdapterListener{
                override fun deleteOneRMRecord(oneRMRecord: OneRmRecord) {
                    viewModel.deleteOneRMRecord(oneRMRecord)
                }

                override fun updateOneRMRecord(id: Int, weight: Double) {
                    viewModel.updateOneRMRecord(id,weight)

                }
            })
            recyclerView.adapter = adapter

        }

        override fun onClick(v: View) {
            if (recyclerView.visibility == View.GONE) {
                recyclerView.visibility = View.VISIBLE
                button.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.up_btn_clicked))
            } else {
                recyclerView.visibility = View.GONE
                button.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.up_btn))
            }
        }

        init {
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
            button.setOnClickListener(this)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}