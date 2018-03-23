package com.shopapp.ui.account

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class ForgotPasswordActivityTest {

    private lateinit var activity: ForgotPasswordActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        activity = Robolectric.buildActivity(ForgotPasswordActivity::class.java, ForgotPasswordActivity.getStartIntent(context))
            .create()
            .resume()
            .get()
        activity.loadingView.minShowTime = 0
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(context.getString(R.string.forgot_password), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldCallPresenterOnSubmitButtonClick() {
        activity.emailInput.setText("email@ggg.com")
        val button = activity.submitButton
        assertNotNull(button)
        button.performClick()
        verify(activity.presenter).forgotPassword("email@ggg.com")
    }

    @Test
    fun shouldChangeStateToProgressOnSubmitButtonClick() {
        activity.emailInput.setText("email@ggg.com")
        val button = activity.submitButton
        assertNotNull(button)
        button.performClick()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)
    }

    @Test
    fun shouldEnableButtonWhenEmailAreNotEmpty() {
        val button = activity.submitButton
        assertNotNull(button)

        activity.emailInput.setText("")
        assertFalse(button.isEnabled)

        activity.emailInput.setText("email@ggg.com")
        assertTrue(button.isEnabled)
    }

    @Test
    fun shouldDisableButtonWhenEmailIsEmpty() {
        val button = activity.submitButton
        assertNotNull(button)

        activity.emailInput.setText("email@ggg.com")
        assertTrue(button.isEnabled)

        activity.emailInput.setText("")
        assertFalse(button.isEnabled)
    }

    @Test
    fun shouldDisableErrorWhenEmailChanged() {
        val inputLayout = activity.emailInputLayout
        inputLayout.error = "error"
        assertTrue(inputLayout.isErrorEnabled)
        activity.emailInput.setText("email")
        assertFalse(inputLayout.isErrorEnabled)
    }

    @Test
    fun shouldShowResendViewsOnShowContent() {
        activity.showContent(Unit)
        assertEquals(View.VISIBLE, activity.resendGroup.visibility)
    }

    @Test
    fun shouldHideInputViewsOnShowContent() {
        activity.showContent(Unit)
        assertEquals(View.GONE, activity.inputGroup.visibility)
    }

    @Test
    fun shouldChangeStateToContentOnShowContent() {
        activity.showContent(Unit)
        assertEquals(View.VISIBLE, activity.contentView.visibility)
    }

    @Test
    fun shouldSetErrorToInputLayoutOnEmailError() {
        activity.showEmailValidError()
        assertEquals(context.getString(R.string.invalid_email_error_message), activity.emailInputLayout.error)
    }

    @Test
    fun shouldAddTextWatcherWhenOnResume() {
        assertNotNull(shadowOf(activity.emailInput).watchers.find { it is SimpleTextWatcher })
    }

    @Test
    fun shouldRemoveTextWatcherWhenOnPause() {
        context = RuntimeEnvironment.application.baseContext
        val activity = Robolectric.buildActivity(ForgotPasswordActivity::class.java, ForgotPasswordActivity.getStartIntent(context))
            .create()
            .resume()
            .pause()
            .get()
        assertTrue(shadowOf(activity.emailInput).watchers.size == 1)
        assertTrue(shadowOf(activity.emailInput).watchers.first() !is SimpleTextWatcher)
    }

    @Test
    fun shouldHideProgressWhenShowMessage() {
        activity.loadData()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)
        activity.showMessage("")
        assertEquals(View.GONE, activity.loadingView.visibility)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}