package com.shopapp.ui.address.base

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
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
class StateBottomSheetPickerTest {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragment: StateBottomSheetPicker
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        val activity = Robolectric.setupActivity(FragmentActivity::class.java)
        fragmentManager = activity.supportFragmentManager
        context = RuntimeEnvironment.application.baseContext
        fragment = StateBottomSheetPicker.newInstance()
    }

    @Test
    fun shouldCorrectConvertModel() {
        val state = MockInstantiator.newState()
        assertEquals(state.name, fragment.getPickerAdapter().convertModel(state))
    }
}