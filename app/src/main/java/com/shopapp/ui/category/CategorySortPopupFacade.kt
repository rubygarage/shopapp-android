package com.shopapp.ui.category

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.shopapp.gateway.entity.SortType
import com.shopapp.R
import com.shopapp.ext.getScreenSize

class CategorySortPopupFacade(
    private val context: Context,
    private val listener: OnSortTypeChangeListener
) : View.OnClickListener {

    private var sortType: SortType? = null
    private var popupWindow: PopupWindow? = null

    @SuppressLint("InflateParams")
    fun showSortPopup(view: View, sortType: SortType) {

        this.sortType = sortType

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_sort, null)
        setupChild(popupView)

        val horizontalSpace = context.resources.getDimensionPixelSize(R.dimen.horizontal_space)
        val width = context.getScreenSize().x - (horizontalSpace * 2)
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow?.let {
            it.showAsDropDown(view, horizontalSpace, 0, Gravity.CENTER_HORIZONTAL)
            dimBehind(it)
        }
    }

    private fun setupChild(parent: View) {
        val newestView: TextView = parent.findViewById(R.id.newestSort)
        val highToLowView: TextView = parent.findViewById(R.id.highToLowSort)
        val lowToHighView: TextView = parent.findViewById(R.id.lowToHighSort)

        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (sortType) {
            SortType.RECENT -> setChildChecked(newestView)
            SortType.PRICE_HIGH_TO_LOW -> setChildChecked(highToLowView)
            SortType.PRICE_LOW_TO_HIGH -> setChildChecked(lowToHighView)
        }

        newestView.setOnClickListener(this)
        highToLowView.setOnClickListener(this)
        lowToHighView.setOnClickListener(this)
    }

    private fun setChildChecked(child: TextView) {
        child.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_black, 0)
    }

    private fun dimBehind(popupWindow: PopupWindow) {
        val container: View = if (popupWindow.background == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupWindow.contentView.parent as View
            } else {
                popupWindow.contentView
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupWindow.contentView.parent.parent as View
            } else {
                popupWindow.contentView.parent as View
            }
        }
        val context = popupWindow.contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.5f
        wm.updateViewLayout(container, p)
    }

    override fun onClick(v: View) {
        var sortType: SortType? = null
        when (v.id) {
            R.id.newestSort -> sortType = SortType.RECENT
            R.id.highToLowSort -> sortType = SortType.PRICE_HIGH_TO_LOW
            R.id.lowToHighSort -> sortType = SortType.PRICE_LOW_TO_HIGH
        }
        sortType?.let {
            if (this.sortType != it) {
                listener.onSortTypeChanged(it)
            } else {
                listener.onSortTypeChanged(SortType.NAME)
            }
            popupWindow?.dismiss()
        }
    }

    interface OnSortTypeChangeListener {

        fun onSortTypeChanged(sortType: SortType)
    }
}