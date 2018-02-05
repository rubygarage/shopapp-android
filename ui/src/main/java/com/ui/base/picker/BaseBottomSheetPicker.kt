package com.ui.base.picker

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ui.R
import kotlinx.android.synthetic.main.bottom_sheet_picker.*

abstract class BaseBottomSheetPicker<T> : BottomSheetDialogFragment() {

    lateinit var adapter: BottomSheetPickerAdapter<T>
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
        adapter = getPickerAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    abstract fun getPickerAdapter(): BottomSheetPickerAdapter<T>

    fun setData(dataList: List<T>) {
        if (this::adapter.isInitialized) {
            adapter.dataList.clear()
            adapter.dataList.addAll(dataList)
            adapter.notifyDataSetChanged()
        }
    }

    interface OnDoneButtonClickedListener<in T> {

        fun onDoneButtonClicked(selectedData: T)
    }
}