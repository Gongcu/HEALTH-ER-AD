package com.health.myapplication.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemMoveListener itemMoveListener;
    public ItemTouchHelperCallback(ItemMoveListener listener){
        itemMoveListener=listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlag, swipeFlags);
     //   return makeFlag( ItemTouchHelper.ACTION_STATE_DRAG , dragFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        itemMoveListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        itemMoveListener.onItemSwiped(viewHolder.itemView.getId());
    }

    public interface ItemMoveListener{
        void onItemMove(int fromPosition, int toPosition);
        void onItemSwiped(int id);
    }

}
