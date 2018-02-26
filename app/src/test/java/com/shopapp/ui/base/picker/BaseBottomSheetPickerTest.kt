package com.shopapp.ui.base.picker

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.TestShopApplication
import kotlinx.android.synthetic.main.bottom_sheet_picker.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BaseBottomSheetPickerTest {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragment: TestBaseBottomSheetPicker
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        val activity = Robolectric.setupActivity(FragmentActivity::class.java)
        fragmentManager = activity.supportFragmentManager
        fragment = TestBaseBottomSheetPicker()
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldShowCorrect() {
        val title = "title"
        fragment.setData(listOf())
        fragment.show(fragmentManager, "tag", title)
        SupportFragmentTestUtil.startFragment(fragment)
        val adapter = fragment.recyclerView.adapter as BottomSheetPickerAdapter<*>
        verify(adapter).notifyDataSetChanged()
        verify(adapter).setSelected(title)
    }

    @Test
    fun shouldReturnResultWhenDoneClicked() {
        fragment.setData(listOf())
        fragment.show(fragmentManager, "tag", "")
        val testObject = "TEST"
        @Suppress("UNCHECKED_CAST")
        val adapter = fragment.recyclerView.adapter as BottomSheetPickerAdapter<Any>
        val mockListener: BaseBottomSheetPicker.OnDoneButtonClickedListener<Any> = mock()
        given(adapter.selectedItemData).willReturn(testObject)
        fragment.onDoneButtonClickedListener = mockListener
        fragment.done.performClick()
        verify(mockListener).onDoneButtonClicked(testObject)
    }

    class TestBaseBottomSheetPicker : BaseBottomSheetPicker<Any>() {

        override fun getPickerAdapter(): BottomSheetPickerAdapter<Any> = mock()
    }
}