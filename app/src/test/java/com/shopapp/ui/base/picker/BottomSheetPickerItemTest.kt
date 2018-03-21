package com.shopapp.ui.base.picker

import android.content.Context
import android.support.v4.content.ContextCompat
import com.shopapp.R
import com.shopapp.TestShopApplication
import kotlinx.android.synthetic.main.item_bottom_sheet_picker.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BottomSheetPickerItemTest {

    private lateinit var context: Context
    private lateinit var itemView: BottomSheetPickerItem

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        itemView = BottomSheetPickerItem(context)
    }

    @Test
    fun shouldDisplaySelectedItemData() {
        val data = "testData"
        itemView.bindData(data, data)
        assertEquals(data, itemView.titleView.text)
        assertEquals(ContextCompat.getDrawable(context, R.drawable.background_selected_picker_item), itemView.titleView.background)
        assertEquals(ContextCompat.getColor(context, R.color.textColorPrimary), itemView.titleView.currentTextColor)
    }

    @Test
    fun shouldDisplaySimpleItemData() {
        val data = "testData"
        val selectedData = null
        itemView.bindData(data, selectedData)
        assertEquals(data, itemView.titleView.text)
        assertEquals(ContextCompat.getDrawable(context, R.color.transparent), itemView.titleView.background)
        assertEquals(ContextCompat.getColor(context, R.color.textColorSecondary), itemView.titleView.currentTextColor)
    }

    @Test
    fun shouldReturnBindData() {
        val data = "testData"
        itemView.bindData(data, data)
        assertEquals(data, itemView.data)
    }
}