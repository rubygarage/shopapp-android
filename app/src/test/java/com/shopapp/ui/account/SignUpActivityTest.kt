package com.client.shop.ui.account

import android.app.Activity
import android.content.Context
import android.view.View
import com.client.shop.R
import com.client.shop.TestShopApplication
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.ui.account.SignUpActivity
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_sign_up.*
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
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class SignUpActivityTest {

    private lateinit var activity: SignUpActivity

    private lateinit var context: Context

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        activity = Robolectric.setupActivity(SignUpActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @After
    fun tearDown() {
        activity.finish()
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(context.getString(R.string.sign_up), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldShowSuccessMessageOnShowContent() {
        activity.showContent(Unit)
        val looper = shadowOf(activity.mainLooper)
        looper.idle()
        assertEquals(ShadowToast.getTextOfLatestToast(), context.getString(R.string.register_success_message))
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
    fun shouldShowErrorOnPasswordInvalid() {
        activity.showPasswordError()
        assertEquals(context.getString(R.string.invalid_password_error_message), activity.passwordInputLayout.error)
    }

    @Test
    fun shouldShowErrorOnEmailInvalid() {
        activity.showEmailError()
        assertEquals(context.getString(R.string.invalid_email_error_message), activity.emailInputLayout.error)
    }

    @Test
    fun shouldEnableButtonWhenFieldsAreNotEmpty() {
        activity.passwordInput.setText("123456789")
        activity.emailInput.setText("email@test.com")
        val createButton = activity.createButton
        assertNotNull(createButton)
        assertTrue(createButton.isEnabled)
    }

    @Test
    fun shouldDisableButtonWhenPasswordIsEmpty() {
        activity.emailInput.setText("email@test.com")
        activity.passwordInput.setText("")
        val loginButton = activity.createButton
        assertNotNull(loginButton)
        assertFalse(loginButton.isEnabled)
    }

    @Test
    fun shouldDisableButtonWhenConfirmEmailIsEmpty() {
        activity.emailInput.setText("")
        activity.passwordInput.setText("123456789")
        val loginButton = activity.createButton
        assertNotNull(loginButton)
        assertFalse(loginButton.isEnabled)
    }

    @Test
    fun shouldPassTrimDataToPresenterOnButtonClick() {
        activity.emailInput.setText("testEmail@i.com  ")
        activity.passwordInput.setText("123456789  ")
        activity.firstNameInput.setText("firstName  ")
        activity.lastNameInput.setText("lastName  ")
        activity.phoneInput.setText(" 0633291677  ")
        val createButton = activity.createButton
        assertNotNull(createButton)
        createButton.performClick()
        verify(activity.presenter).signUp(
            "firstName",
            "lastName",
            "testEmail@i.com",
            "123456789",
            "0633291677"
        )
    }
}