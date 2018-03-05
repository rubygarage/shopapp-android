package com.shopapp.ui.checkout.payment.card

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.shopapp.TestShopApplication
import com.shopapp.ui.checkout.payment.card.DateBottomSheetPicker.Companion.MAX_AVAILABLE_YEARS_COUNT
import kotlinx.android.synthetic.main.bottom_sheet_picker.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class DateBottomSheetPickerTest {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragment: DateBottomSheetPicker
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        val activity = Robolectric.setupActivity(FragmentActivity::class.java)
        fragmentManager = activity.supportFragmentManager
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldShowDatePickerDialogWithMonths() {
        fragment = DateBottomSheetPicker.newInstance(DateBottomSheetPicker.DATE_TYPE_MONTH)
        fragment.show(fragmentManager, "", "")

        assertEquals(12, fragment.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldShowDatePickerDialogWithYears() {
        val currentYear = 1
        fragment = DateBottomSheetPicker.newInstance(DateBottomSheetPicker.DATE_TYPE_YEAR)
        fragment.show(fragmentManager, "", "")

        assertEquals(MAX_AVAILABLE_YEARS_COUNT + currentYear, fragment.recyclerView.adapter.itemCount)
    }

}