package com.shopapp.ui.account

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_change_password.*
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
class ChangePasswordActivityTest {

    private lateinit var activity: ChangePasswordActivity
    private lateinit var context: Context

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        activity = Robolectric.buildActivity(ChangePasswordActivity::class.java, ChangePasswordActivity.getStartIntent(context))
            .create()
            .resume()
            .get()
        activity.loadingView.minShowTime = 0
    }

    @After
    fun tearDown() {
        activity.finish()
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(context.getString(R.string.set_new_password), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldMoveFocusToRootViewOnUpdateButtonClick() {
        val updateButton = activity.updateButton
        assertNotNull(updateButton)
        updateButton.performClick()
        assertTrue(activity.containerConstraintLayout.isFocused)
    }

    @Test
    fun shouldChangePasswordOnUpdateButtonClick() {
        activity.passwordInput.setText("123456789")
        activity.passwordConfirmInput.setText("123456789")
        val updateButton = activity.updateButton
        assertNotNull(updateButton)
        updateButton.performClick()
        verify(activity.presenter).changePassword("123456789", "123456789")
    }

    @Test
    fun shouldEnableButtonWhenFieldsAreNotEmpty() {
        activity.passwordInput.setText("123456789")
        activity.passwordConfirmInput.setText("123456789")
        val updateButton = activity.updateButton
        assertNotNull(updateButton)
        assertTrue(updateButton.isEnabled)
    }

    @Test
    fun shouldDisableButtonWhenPasswordIsEmpty() {
        activity.passwordInput.setText("")
        activity.passwordConfirmInput.setText("123456789")
        val updateButton = activity.updateButton
        assertNotNull(updateButton)
        assertFalse(updateButton.isEnabled)
    }

    @Test
    fun shouldDisableButtonWhenConfirmPasswordIsEmpty() {
        activity.passwordInput.setText("123456789")
        activity.passwordConfirmInput.setText("")
        val updateButton = activity.updateButton
        assertNotNull(updateButton)
        assertFalse(updateButton.isEnabled)
    }

    @Test
    fun shouldDisableErrorWhenPasswordChanged() {
        val inputLayout = activity.passwordInputLayout
        inputLayout.error = "error"
        assertTrue(inputLayout.isErrorEnabled)
        activity.passwordInput.setText("123456789")
        assertFalse(inputLayout.isErrorEnabled)
    }

    @Test
    fun shouldDisableErrorWhenPasswordConfirmChanged() {
        val inputLayout = activity.passwordConfirmInputLayout
        inputLayout.error = "error"
        assertTrue(inputLayout.isErrorEnabled)
        activity.passwordConfirmInput.setText("123456789")
        assertFalse(inputLayout.isErrorEnabled)
    }

    @Test
    fun shouldFinishActivityOnPasswordChanged() {
        activity.passwordChanged()
        assertTrue(activity.isFinishing)
    }

    @Test
    fun shouldShowErrorOnPasswordInvalid() {
        activity.passwordValidError()
        assertEquals(context.getString(R.string.invalid_password_error_message), activity.passwordInputLayout.error)
    }

    @Test
    fun shouldShowErrorOnPasswordNotMatched() {
        activity.passwordsMatchError()
        assertEquals(context.getString(R.string.password_match_error_message), activity.passwordConfirmInputLayout.error)
    }

    @Test
    fun shouldShowTranslucentProgressViewOnShowUpdateProgress() {

        val drawable = ColorDrawable(ContextCompat.getColor(context, R.color.colorBackgroundLightTranslucent))

        activity.showUpdateProgress()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)
        assertEquals(drawable, activity.loadingView.background)
    }

    @Test
    fun shouldHideUpdateProgress() {
        assertEquals(View.GONE, activity.loadingView.visibility)
        activity.loadData()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)
        activity.hideUpdateProgress()
        assertEquals(View.GONE, activity.loadingView.visibility)
    }

    @Test
    fun shouldAddTextWatcherWhenOnResume() {
        assertNotNull(shadowOf(activity.passwordInput).watchers.find { it is SimpleTextWatcher })
        assertNotNull(shadowOf(activity.passwordConfirmInput).watchers.find { it is SimpleTextWatcher })
    }

    @Test
    fun shouldRemoveTextWatcherWhenOnPause() {
        context = RuntimeEnvironment.application.baseContext
        val activity = Robolectric.buildActivity(ChangePasswordActivity::class.java, ChangePasswordActivity.getStartIntent(context))
            .create()
            .resume()
            .pause()
            .get()
        assertTrue(shadowOf(activity.passwordInput).watchers.size == 1)
        assertTrue(shadowOf(activity.passwordInput).watchers.first() !is SimpleTextWatcher)
        assertTrue(shadowOf(activity.passwordConfirmInput).watchers.size == 1)
        assertTrue(shadowOf(activity.passwordConfirmInput).watchers.first() !is SimpleTextWatcher)
    }

}