package com.shopapp.ui.checkout.payment.card

import android.os.Bundle
import android.view.View
import com.shopapp.ui.base.picker.BaseBottomSheetPicker
import com.shopapp.ui.base.picker.BottomSheetPickerAdapter
import java.util.*

class DateBottomSheetPicker : BaseBottomSheetPicker<String>() {

    companion object {
        private const val DATE_TYPE = "DATE_TYPE"
        const val MAX_AVAILABLE_YEARS_COUNT = 30
        const val DATE_TYPE_MONTH = "MONTH"
        const val DATE_TYPE_YEAR = "YEAR"

        fun newInstance(dateType: String): DateBottomSheetPicker {
            val bottomSheet = DateBottomSheetPicker()
            val args = Bundle()
            args.putString(DATE_TYPE, dateType)
            bottomSheet.arguments = args
            return bottomSheet
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateType = arguments?.getString(DATE_TYPE, "")
        when (dateType) {
            DATE_TYPE_MONTH -> setData(getAvailableMonths())
            DATE_TYPE_YEAR -> setData(getAvailableYears())
        }

    }

    override fun getPickerAdapter(): BottomSheetPickerAdapter<String> {
        return object : BottomSheetPickerAdapter<String>() {
            override fun convertModel(it: String) = it
        }
    }

    private fun getAvailableMonths(): List<String> {
        return listOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    }

    private fun getAvailableYears(): List<String> {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        return (currentYear..currentYear + MAX_AVAILABLE_YEARS_COUNT).map { it.toString() }
    }
}