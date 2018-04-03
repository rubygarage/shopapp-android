package com.shopapp.ui.account

import android.app.Activity
import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class SignInActivityTest {

    private lateinit var activity: SignInActivity

    private lateinit var context: Context

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        activity = Robolectric.setupActivity(SignInActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @After
    fun tearDown() {
        activity.finish()
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(context.getString(R.string.sign_in), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldEnableButtonWhenFieldsAreNotEmpty() {
        activity.passwordInput.setText("123456789")
        activity.emailInput.setText("email@test.com")
        val loginButton = activity.loginButton
        assertNotNull(loginButton)
        assertTrue(loginButton.isEnabled)
    }

    @Test
    fun shouldDisableButtonWhenPasswordIsEmpty() {
        activity.passwordInput.setText("123456789")
        activity.emailInput.setText("email@test.com")
        assertTrue(activity.loginButton.isEnabled)

        activity.passwordInput.setText("")
        assertFalse(activity.loginButton.isEnabled)
    }

    @Test
    fun shouldDisableButtonWhenConfirmEmailIsEmpty() {
        activity.passwordInput.setText("123456789")
        activity.emailInput.setText("email@test.com")
        assertTrue(activity.loginButton.isEnabled)

        activity.emailInput.setText("")
        assertFalse(activity.loginButton.isEnabled)
    }

    @Test
    fun shouldCallPresenterOnButtonClick() {
        val loginButton = activity.loginButton
        assertNotNull(loginButton)
        loginButton.performClick()
        verify(activity.presenter).logIn(any(), any())
    }

    @Test
    fun shouldPassTrimDataToPresenterOnButtonClick() {
        activity.emailInput.setText("testEmail@i.com  ")
        activity.passwordInput.setText("123456789  ")
        val loginButton = activity.loginButton
        assertNotNull(loginButton)
        loginButton.performClick()
        verify(activity.presenter).logIn("testEmail@i.com", "123456789")
    }

    @Test
    fun shouldShowSuccessMessageOnShowContent() {
        activity.showContent(Unit)
        val looper = shadowOf(activity.mainLooper)
        looper.idle()
        assertEquals(ShadowToast.getTextOfLatestToast(), context.getString(R.string.login_success_message))
    }

    @Test
    fun shouldChangeStateToContentOnShowContent() {
        activity.showContent(Unit)
        assertEquals(View.VISIBLE, activity.contentView.visibility)
    }

    @Test
    fun shouldSetOkResultToActivityOnShowContent() {
        activity.showContent(Unit)
        val shadow = shadowOf(activity)
        assertEquals(shadow.resultCode, Activity.RESULT_OK)
    }

    @Test
    fun shouldFinishActivityOnShowContent() {
        activity.showContent(Unit)
        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldDisableViewOnCheckPassed() {
        activity.onCheckPassed()
        assertFalse(activity.emailInput.isEnabled)
        assertFalse(activity.passwordInput.isEnabled)
    }

    @Test
    fun shouldChangeStateToProgressOnCheckPassed() {
        activity.onCheckPassed()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)
    }

    @Test
    fun shouldEnableViewOnFailure() {
        activity.onFailure()
        assertTrue(activity.emailInput.isEnabled)
        assertTrue(activity.passwordInput.isEnabled)
    }

    @Test
    fun shouldChangeStateToContentOnFailure() {
        activity.onFailure()
        assertEquals(View.VISIBLE, activity.contentView.visibility)
    }

    @Test
    fun shouldAddTextWatcherWhenOnResume() {
        assertNotNull(Shadows.shadowOf(activity.emailInput).watchers.find { it is SimpleTextWatcher })
        assertNotNull(Shadows.shadowOf(activity.passwordInput).watchers.find { it is SimpleTextWatcher })
    }

    @Test
    fun shouldRemoveTextWatcherWhenOnPause() {
        context = RuntimeEnvironment.application.baseContext
        val activity = Robolectric.buildActivity(SignInActivity::class.java, SignInActivity.getStartIntent(context))
            .create()
            .resume()
            .pause()
            .get()

        assertTrue(Shadows.shadowOf(activity.emailInput).watchers.size == 1)
        assertTrue(Shadows.shadowOf(activity.emailInput).watchers.first() !is SimpleTextWatcher)
        assertTrue(Shadows.shadowOf(activity.passwordInput).watchers.size == 1)
        assertTrue(Shadows.shadowOf(activity.passwordInput).watchers.first() !is SimpleTextWatcher)
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
    fun shouldDisableErrorWhenPasswordChanged() {
        val inputLayout = activity.passwordInputLayout
        inputLayout.error = "error"
        assertTrue(inputLayout.isErrorEnabled)
        activity.passwordInput.setText("password")
        assertFalse(inputLayout.isErrorEnabled)
    }

    @Test
    fun shouldShowEmailError() {
        activity.showEmailError()

        assertTrue(activity.emailInputLayout.isErrorEnabled)
        assertEquals(context.getString(R.string.invalid_email_error_message), activity.emailInputLayout.error)
    }

    @Test
    fun shouldShowPasswordError() {
        activity.showPasswordError()

        assertTrue(activity.passwordInputLayout.isErrorEnabled)
        assertEquals(context.getString(R.string.invalid_password_error_message), activity.passwordInputLayout.error)
    }

    @Test
    fun shouldShowForgotPassword() {
        activity.forgotPassword.performClick()
        verify(activity.router).showForgotPassword(activity)
    }
}