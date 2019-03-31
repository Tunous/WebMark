package me.thanel.webmark.ui.touchhelper

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class ItemTouchCallback(
    private val swipeElevation: Float
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    abstract fun onSwiped(position: Int, direction: Int)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onSwiped(position, direction)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val swipeableViewHolder = viewHolder as? SwipeableViewHolder
        swipeableViewHolder?.swipeableView?.apply {
            translationX = 0f
            translationZ = 0f
        }

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
        val swipeableViewHolder = viewHolder as? SwipeableViewHolder
        swipeableViewHolder?.swipeableView?.apply {
            translationX = dX
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                translationZ = if (dX != 0f) swipeElevation else 0f
            }
        }

        val draggableViewHolder = viewHolder as? DraggableViewHolder
        if (draggableViewHolder != null) {
            draggableViewHolder.draggableView.translationY = dY
            recyclerView.clipChildren = dY == 0f && !isCurrentlyActive
        }
    }

    companion object {
        fun create(swipeElevation: Float, onSwiped: (Int, Int) -> Unit) =
            object : ItemTouchCallback(swipeElevation) {
                override fun onSwiped(position: Int, direction: Int) =
                    onSwiped(position, direction)
            }
    }
}
