package me.thanel.webmark.ui.touchhelper

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class ItemTouchCallback :
    ItemTouchHelper.SimpleCallback(0, 0) {

    abstract fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int, direction: Int)

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeableViewHolder = viewHolder as? SwipeableViewHolder
        return swipeableViewHolder?.getSwipeDirs() ?: 0
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onSwiped(viewHolder, position, direction)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val swipeableViewHolder = viewHolder as? SwipeableViewHolder
        swipeableViewHolder?.onClearSwipe()

        val draggableViewHolder = viewHolder as? DraggableViewHolder
        if (draggableViewHolder != null) {
            draggableViewHolder.draggableView.isSelected = false
            recyclerView.clipChildren = true
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        val draggableViewHolder = viewHolder as? DraggableViewHolder
        draggableViewHolder?.draggableView?.isSelected =
            actionState == ItemTouchHelper.ACTION_STATE_DRAG
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val swipeableViewHolder = viewHolder as? SwipeableViewHolder
            swipeableViewHolder?.onSwipe(dX)
        }

        val draggableViewHolder = viewHolder as? DraggableViewHolder
        if (draggableViewHolder != null) {
            draggableViewHolder.draggableView.translationY = dY
            recyclerView.clipChildren = dY == 0f && !isCurrentlyActive
        }
    }

    companion object {
        fun create(onSwiped: (RecyclerView.ViewHolder, Int, Int) -> Unit) = object : ItemTouchCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int, direction: Int) =
                onSwiped(viewHolder, position, direction)
        }
    }
}
