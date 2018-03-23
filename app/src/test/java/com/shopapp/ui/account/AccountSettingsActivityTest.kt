package com.shopapp.ui.account

import android.content.Context
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.test.RxImmediateSchedulerRule
import kotlinx.android.synthetic.main.activity_account_settings.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class AccountSettingsActivityTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var activity: AccountSettingsActivity

    private lateinit var context: Context

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        activity = Robolectric.setupActivity(AccountSettingsActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(context.getString(R.string.settings), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldGetCustomerOnLoadData() {
        activity.loadData()
        verify(activity.presenter, times(2)).getCustomer()
    }

    @Test
    fun shouldPutCustomerSettingsInSwitcher() {
        activity.acceptPromotionSwitch.isChecked = false
        assertFalse(activity.acceptPromotionSwitch.isChecked)

        activity.showContent(true)
        assertTrue(activity.acceptPromotionSwitch.isChecked)
    }

    @Test
    fun shouldAddListenerToSwitcher() {
        activity.showContent(true)
        assertTrue(activity.acceptPromotionSwitch.isChecked)

        activity.acceptPromotionSwitch.isChecked = false
        verify(activity.presenter).updateSettings(false)

        activity.acceptPromotionSwitch.isChecked = true
        verify(activity.presenter).updateSettings(true)
    }

    @After
    fun tearDown() {
        activity.finish()
    }

}