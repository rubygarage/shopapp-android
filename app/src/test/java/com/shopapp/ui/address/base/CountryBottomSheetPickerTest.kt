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
class CountryBottomSheetPickerTest {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragment: CountryBottomSheetPicker
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        val activity = Robolectric.setupActivity(FragmentActivity::class.java)
        fragmentManager = activity.supportFragmentManager
        context = RuntimeEnvironment.application.baseContext
        fragment = CountryBottomSheetPicker.newInstance()
    }

    @Test
    fun shouldCorrectConvertModel() {
        val country = MockInstantiator.newCountry()
        assertEquals(country.name, fragment.getPickerAdapter().convertModel(country))
    }
}