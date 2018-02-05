package com.ui.module.address

import android.os.Bundle
import android.view.View
import com.domain.entity.State
import com.ui.base.picker.BaseBottomSheetPicker
import com.ui.base.picker.BottomSheetPickerAdapter

class StateBottomSheetPicker : BaseBottomSheetPicker<State>() {

    companion object {

        private const val STATES = "states"

        fun newInstance(states: ArrayList<State>): StateBottomSheetPicker {
            val bottomSheet = StateBottomSheetPicker()
            val args = Bundle()
            args.putParcelableArrayList(STATES, states)
            bottomSheet.arguments = args
            return bottomSheet
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getParcelableArrayList<State>(STATES)
        data?.let { setData(data) }
    }

    override fun getPickerAdapter(): BottomSheetPickerAdapter<State> {
        return object : BottomSheetPickerAdapter<State>() {
            override fun convertModel(it: State): String = it.name
        }
    }

}