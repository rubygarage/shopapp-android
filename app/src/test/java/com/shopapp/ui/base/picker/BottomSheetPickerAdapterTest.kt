package com.shopapp.ui.base.picker

import android.content.Context
import android.widget.LinearLayout
import com.shopapp.TestShopApplication
import kotlinx.android.synthetic.main.item_bottom_sheet_picker.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BottomSheetPickerAdapterTest {

    private lateinit var adapter: TestBottomSheetPickerAdapter
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        adapter = TestBottomSheetPickerAdapter()
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldReturnCorrectCount() {
        val stubList = mutableListOf("", "", "")
        adapter.dataList = stubList
        assertEquals(stubList.size, adapter.itemCount)
    }

    @Test
    fun shouldAdapterSelectItem() {
        val selectedItem = "selectedItem"
        val simpleItem = "selectedItem"
        adapter.dataList = mutableListOf(selectedItem, simpleItem)
        val selectedIndex = adapter.setSelected(selectedItem)
        assertEquals(0, selectedIndex)
        assertEquals(selectedItem, adapter.selectedItemData)
    }

    @Test
    fun shouldCreateCorrectViewHolder() {
        val holder = adapter.createViewHolder(LinearLayout(context), 0)
        assertTrue(holder is BottomSheetPickerAdapter.ItemViewHolder)
    }

    @Test
    fun shouldBindItemViewData() {
        val selectedItem = "selectedItem"
        adapter.dataList = mutableListOf(selectedItem)

        val holder = adapter.createViewHolder(LinearLayout(context), 0)
        assertTrue(holder is BottomSheetPickerAdapter.ItemViewHolder)
        assertTrue(holder.itemView is BottomSheetPickerItem)
        adapter.bindViewHolder(holder, 0)
        val view = holder.itemView as BottomSheetPickerItem
        assertEquals(selectedItem, view.titleView.text.toString())
    }

    @Test
    fun shouldReturnCorrectItemId() {
        val position = 3
        assertEquals(position.toLong(), adapter.getItemId(3))
    }

    @Test
    fun shouldSelectItemOnClick() {
        val selectedItem = "selectedItem"
        val simpleItem = "selectedItem"
        adapter.dataList = mutableListOf(selectedItem, simpleItem)
        adapter.onItemClicked(1)
        assertEquals(simpleItem, adapter.selectedItemData)
    }

    private class TestBottomSheetPickerAdapter : BottomSheetPickerAdapter<String>() {
        override fun convertModel(it: String) = it
    }
}