package com.ui.module.address

import android.os.Bundle
import android.view.View
import com.domain.entity.Country
import com.ui.base.picker.BaseBottomSheetPicker
import com.ui.base.picker.BottomSheetPickerAdapter

class CountryBottomSheetPicker : BaseBottomSheetPicker<Country>() {

    companion object {

        private const val COUNTRIES = "countries"

        fun newInstance(countries: ArrayList<Country>): CountryBottomSheetPicker {
            val bottomSheet = CountryBottomSheetPicker()
            val args = Bundle()
            args.putParcelableArrayList(COUNTRIES, countries)
            bottomSheet.arguments = args
            return bottomSheet
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getParcelableArrayList<Country>(COUNTRIES)
        data?.let { setData(data) }
    }

    override fun getPickerAdapter(): BottomSheetPickerAdapter<Country> {
        return object : BottomSheetPickerAdapter<Country>() {
            override fun convertModel(it: Country): String = it.name
        }
    }

}