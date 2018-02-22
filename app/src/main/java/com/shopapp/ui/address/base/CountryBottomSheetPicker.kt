package com.shopapp.ui.address.base

import android.os.Bundle
import com.shopapp.gateway.entity.Country
import com.shopapp.ui.base.picker.BaseBottomSheetPicker
import com.shopapp.ui.base.picker.BottomSheetPickerAdapter

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