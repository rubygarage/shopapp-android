package com.ui.base.picker

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ui.R
import kotlinx.android.synthetic.main.bottom_sheet_picker.*

open class BaseBottomSheetPicker : BottomSheetDialogFragment() {

    private val dataList: MutableList<String> = mutableListOf()
    private lateinit var adapter: BottomSheetPickerAdapter
    var onDoneButtonClickedListener: OnDoneButtonClickedListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.bottom_sheet_picker, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        done.setOnClickListener {
            val selectedData = adapter.selectedItemData
            if (selectedData != null) {
                onDoneButtonClickedListener?.onDoneButtonClicked(selectedData)
            }
            dismiss()
        }
        adapter = BottomSheetPickerAdapter(dataList)
        productRecyclerView.layoutManager = LinearLayoutManager(context)
        productRecyclerView.adapter = adapter
    }

    fun setData(dataList: List<String>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        if (this::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    interface OnDoneButtonClickedListener {

        fun onDoneButtonClicked(selectedData: String)
    }
}