package com.client.shop.ui.address.base

import android.os.Bundle
import com.client.shop.gateway.entity.State
import com.client.shop.ui.base.picker.BaseBottomSheetPicker
import com.client.shop.ui.base.picker.BottomSheetPickerAdapter

class StateBottomSheetPicker : BaseBottomSheetPicker<State>() {

    companion object {

        fun newInstance(): StateBottomSheetPicker {
            val bottomSheet = StateBottomSheetPicker()
            val args = Bundle()
            bottomSheet.arguments = args
            return bottomSheet
        }
    }

    override fun getPickerAdapter(): BottomSheetPickerAdapter<State> {
        return object : BottomSheetPickerAdapter<State>() {
            override fun convertModel(it: State): String = it.name
        }
    }

}