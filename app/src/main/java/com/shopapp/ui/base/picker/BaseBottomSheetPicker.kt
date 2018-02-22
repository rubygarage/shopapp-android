package com.shopapp.ui.base.picker

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shopapp.R
import kotlinx.android.synthetic.main.bottom_sheet_picker.*

abstract class BaseBottomSheetPicker<T> : BottomSheetDialogFragment() {

    protected val adapter: BottomSheetPickerAdapter<T> by lazy {
        getPickerAdapter()
    }

    private lateinit var manager: RecyclerView.LayoutManager

    var onDoneButtonClickedListener: OnDoneButtonClickedListener<T>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.setOnClickListener {
            val selectedData = adapter.selectedItemData
            if (selectedData != null) {
                onDoneButtonClickedListener?.onDoneButtonClicked(selectedData)
            }
            dismiss()
        }
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
    }

    abstract fun getPickerAdapter(): BottomSheetPickerAdapter<T>

    fun setData(dataList: List<T>) {
        adapter.dataList.clear()
        adapter.dataList.addAll(dataList)
        adapter.notifyDataSetChanged()
    }

    fun show(fragmentManager: FragmentManager?, tag: String?, selectedTitle: String) {
        val index = adapter.setSelected(selectedTitle)
        manager = LinearLayoutManager(context)
        manager.scrollToPosition(index)
        super.show(fragmentManager, tag)
    }

    interface OnDoneButtonClickedListener<in T> {

        fun onDoneButtonClicked(selectedData: T)
    }
}