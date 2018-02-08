package com.ui.module.address

import android.os.Bundle
import com.domain.entity.Country
import com.ui.base.picker.BaseBottomSheetPicker
import com.ui.base.picker.BottomSheetPickerAdapter

class CountryBottomSheetPicker : BaseBottomSheetPicker<Country>() {

    companion object {

        fun newInstance(): CountryBottomSheetPicker {
            val bottomSheet = CountryBottomSheetPicker()
            val args = Bundle()
            bottomSheet.arguments = args
            return bottomSheet
        }

    }

    override fun getPickerAdapter(): BottomSheetPickerAdapter<Country> {
        return object : BottomSheetPickerAdapter<Country>() {
            override fun convertModel(it: Country): String = it.name
        }
    }

}