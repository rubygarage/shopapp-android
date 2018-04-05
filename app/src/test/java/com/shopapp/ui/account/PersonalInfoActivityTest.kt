package com.shopapp.ui.account

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Customer
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_personal_info.*
import kotlinx.android.synthetic.main.layout_lce.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class PersonalInfoActivityTest {

    private lateinit var activity: PersonalInfoActivity
    private lateinit var context: Context

    private val oldCustomer: Customer = mock {
        on { email } doReturn "mail@mail.com"
        on { firstName } doReturn "firstName"
        on { lastName } doReturn "lastName"
        on { phone } doReturn "06332916"
    }

    @Before
    fun setUpTest() {
        activity = Robolectric.setupActivity(PersonalInfoActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
        activity.loadingView.minShowTime = 0
        doNothing().`when`(activity.presenter).getCustomer()
    }

    @Test
    fun shouldGetCustomerOnLoadData() {
        activity.loadData()
        verify(activity.presenter, times(2)).getCustomer()
    }

    @Test
    fun shouldShowCustomerEmailWhenCustomerLoaded() {
        activity.setupCustomerEmail("mail@mail.com")
        assertEquals("mail@mail.com", activity.emailInput.text.toString())
    }

    @Test
    fun shouldShowCustomerNameWhenCustomerLoaded() {
        activity.showContent(oldCustomer)
        assertEquals("firstName", activity.firstNameInput.text.toString())
    }

    @Test
    fun shouldShowCustomerLastNameWhenCustomerLoaded() {
        activity.showContent(oldCustomer)
        assertEquals("lastName", activity.lastNameInput.text.toString())
    }

    @Test
    fun shouldShowCustomerPhoneWhenCustomerLoaded() {
        activity.showContent(oldCustomer)
        assertEquals("lastName", activity.lastNameInput.text.toString())
    }

    @Test
    fun shouldSetEmailWhenFieldIsDisable() {
        assertEquals("", activity.emailInput.text.toString())
        assertFalse(activity.emailInput.isEnabled)
        activity.setupCustomerEmail("email@test.com")
        assertEquals("email@test.com", activity.emailInput.text.toString())
        assertFalse(activity.emailInput.isEnabled)
    }

    @Test
    fun clearFocusOnPhoneFieldClick() {
        activity.phoneInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
        assertFalse(activity.phoneInput.isFocused)
    }

    @Test
    fun shouldDisableButtonOnCustomerFieldsNotChange() {
        activity.showContent(oldCustomer)
        assertFalse(activity.saveButton.isEnabled)
        activity.firstNameInput.setText("changedText")
        assertTrue(activity.saveButton.isEnabled)
    }

    @Test
    fun shouldEnableButtonOnCustomerFirstNameChange() {
        activity.showContent(oldCustomer)
        assertFalse(activity.saveButton.isEnabled)
        activity.firstNameInput.setText("changedText")
        assertTrue(activity.saveButton.isEnabled)
    }

    @Test
    fun shouldEnableButtonOnCustomerLastNameChange() {
        activity.showContent(oldCustomer)
        assertFalse(activity.saveButton.isEnabled)
        activity.lastNameInput.setText("changedText")
        assertTrue(activity.saveButton.isEnabled)
    }

    @Test
    fun shouldEnableButtonOnCustomerPhoneChange() {
        activity.showContent(oldCustomer)
        assertFalse(activity.saveButton.isEnabled)
        activity.phoneInput.setText("07")
        assertTrue(activity.saveButton.isEnabled)
    }

    @Test
    fun shouldAddTextWatcherWhenOnResume() {
        assertNotNull(Shadows.shadowOf(activity.firstNameInput).watchers.find { it is SimpleTextWatcher })
        assertNotNull(Shadows.shadowOf(activity.lastNameInput).watchers.find { it is SimpleTextWatcher })
        assertNotNull(Shadows.shadowOf(activity.phoneInput).watchers.find { it is SimpleTextWatcher })
    }

    @Test
    fun shouldRemoveTextWatcherWhenOnPause() {
        context = RuntimeEnvironment.application.baseContext
        val activity = Robolectric.buildActivity(PersonalInfoActivity::class.java, PersonalInfoActivity.getStartIntent(context))
            .create()
            .resume()
            .pause()
            .get()

        assertTrue(Shadows.shadowOf(activity.firstNameInput).watchers.size == 1)
        assertTrue(Shadows.shadowOf(activity.firstNameInput).watchers.first() !is SimpleTextWatcher)
        assertTrue(Shadows.shadowOf(activity.lastNameInput).watchers.size == 1)
        assertTrue(Shadows.shadowOf(activity.lastNameInput).watchers.first() !is SimpleTextWatcher)
        assertTrue(Shadows.shadowOf(activity.phoneInput).watchers.size == 1)
        assertTrue(Shadows.shadowOf(activity.phoneInput).watchers.first() !is SimpleTextWatcher)
    }

    @Test
    fun shouldEditCustomerWhenButtonClicked() {
        activity.saveButton.performClick()

        assertEquals(View.VISIBLE, activity.loadingView.visibility)
        assertTrue(activity.containerConstraintLayout.hasFocus())
        verify(activity.presenter).editCustomer(
            activity.firstNameInput.text.toString(),
            activity.lastNameInput.text.toString(),
            activity.phoneInput.text.toString()
        )
    }

    @Test
    fun shouldShowChangePassword() {
        activity.changePassword.performClick()
        verify(activity.router).showChangePassword(activity)
    }

    @Test
    fun shouldShowToastAndSetResultWhenOnCustomerChanged() {
        val customer: Customer = mock()
        activity.onCustomerChanged(customer)
        val looper = Shadows.shadowOf(activity.mainLooper)
        looper.idle()

        assertEquals(ShadowToast.getTextOfLatestToast(), context.getString(R.string.customer_changed))
        assertEquals(Activity.RESULT_OK, Shadows.shadowOf(activity).resultCode)
    }

    @Test
    fun shouldLoadingViewBeGoneWhenHideProgressCalled() {
        activity.loadData()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)
        activity.hideProgress()
        assertEquals(View.GONE, activity.loadingView.visibility)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}