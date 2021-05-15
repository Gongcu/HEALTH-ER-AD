package com.health.myapplication.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(private val itemMoveListener: ItemMoveListener) : ItemTouchHelper.Callback() {
    var dragFrom = -1
    var dragTo = -1
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlag, swipeFlags)
        //   return makeFlag( ItemTouchHelper.ACTION_STATE_DRAG , dragFlag);
    }

    override fun onMove(recyclerView: RecyclerView, fromViewHolder: RecyclerView.ViewHolder, toViewHolder: RecyclerView.ViewHolder): Boolean {
        itemMoveListener.onItemMove(fromViewHolder.adapterPosition, toViewHolder.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemMoveListener.onItemSwiped(viewHolder.itemView.id)
    }
    override fun canDropOver(recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return current.itemViewType == target.itemViewType
    }
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        //if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
            itemMoveListener.onItemMoveFinished()
       // }
       // dragTo = -1
       // dragFrom = -1
    }

    interface ItemMoveListener {
        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onItemSwiped(id: Int)
        fun onItemMoveFinished()
    }
}