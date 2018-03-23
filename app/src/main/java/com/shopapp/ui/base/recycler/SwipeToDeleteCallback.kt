package com.shopapp.ui.base.recycler

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.shopapp.R


class SwipeToDeleteCallback(private val onItemSwipeListener: OnItemSwipeListener) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        val foregroundView: View? = viewHolder?.itemView?.findViewById(R.id.foregroundView)
        foregroundView?.let {
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                 viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float,
                                 actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView: View? = viewHolder?.itemView?.findViewById(R.id.foregroundView)
        foregroundView?.let {
            ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        val foregroundView: View? = viewHolder?.itemView?.findViewById(R.id.foregroundView)
        foregroundView?.let {
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        val foregroundView: View? = viewHolder?.itemView?.findViewById(R.id.foregroundView)
        foregroundView?.let {
            ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        onItemSwipeListener.onItemSwiped(viewHolder, direction)
    }

    interface OnItemSwipeListener {

        fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int)
    }
}