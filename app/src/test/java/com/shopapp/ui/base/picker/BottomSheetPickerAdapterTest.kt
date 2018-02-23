package com.shopapp.ui.base.picker

import com.shopapp.TestShopApplication
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BottomSheetPickerAdapterTest {

    private lateinit var adapter: TestBottomSheetPickerAdapter

    @Before
    fun setUpTest() {
        adapter = TestBottomSheetPickerAdapter()
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

    private class TestBottomSheetPickerAdapter : BottomSheetPickerAdapter<String>() {
        override fun convertModel(it: String) = it
    }
}