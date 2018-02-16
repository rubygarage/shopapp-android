package com.client.shop.ui.account

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import com.client.shop.R
import com.client.shop.TestShopApplication
import com.nhaarman.mockito_kotlin.verify
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_lce.*
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
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class ChangePasswordActivityTest {

    private lateinit var activity: ChangePasswordActivity

    private lateinit var context: Context

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        activity = Robolectric.setupActivity(ChangePasswordActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
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
    fun shouldShowProgressOn() {
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
        val updateButton = activity.updateButton
        assertNotNull(updateButton)
        assertFalse(updateButton.isEnabled)
    }

    @Test
    fun shouldDisableButtonWhenConfirmPasswordIsEmpty() {
        activity.passwordConfirmInput.setText("")
        val updateButton = activity.updateButton
        assertNotNull(updateButton)
        assertFalse(updateButton.isEnabled)
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
    fun shouldHideProgressViewOnHideUpdateProgress() {
        //todo Fix inner handler check
        val looper = shadowOf(activity.mainLooper)
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

        activity.showUpdateProgress()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)

        activity.hideUpdateProgress()
        looper.pause()
        looper.idle()
        assertEquals(View.GONE, activity.loadingView.visibility)
    }


}