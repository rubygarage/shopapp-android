package com.shopapp.ui.account

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Policy
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.custom.SimpleTextWatcher
import com.shopapp.ui.policy.PolicyActivity
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
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
class SignUpActivityTest {

    private lateinit var context: Context
    private lateinit var privacy: Policy
    private lateinit var terms: Policy
    private lateinit var activity: SignUpActivity

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        privacy = MockInstantiator.newPolicy()
        terms = MockInstantiator.newPolicy()
        activity = Robolectric.buildActivity(SignUpActivity::class.java,
            SignUpActivity.getStartIntent(context, privacy, terms))
            .create()
            .resume()
            .get()
        activity.loadingView.minShowTime = 0
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(context.getString(R.string.sign_up), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldSetPolicyTextWhenDataExist() {
        assertTrue(activity.policyText.text.isNotEmpty())
        assertEquals(Color.TRANSPARENT, activity.policyText.highlightColor)
    }

    @Test
    fun shouldNotSetPolicyTextWhenDataDoesNotExist() {
        activity = Robolectric.buildActivity(SignUpActivity::class.java,
            SignUpActivity.getStartIntent(context, null, null))
            .create()
            .resume()
            .get()
        assertTrue(activity.policyText.text.isEmpty())
    }

    @Test
    fun shouldStartPolicyActivityWhenPrivacySpanClicked() {
        val spannableString = activity.policyText.text  as SpannableString
        val spans = spannableString.getSpans(0, spannableString.length, ClickableSpan::class.java)

        assertTrue(spans.isNotEmpty())
        spans.first().onClick(activity.policyText)
        verify(activity.router).showPolicy(activity, privacy)
    }

    @Test
    fun shouldStartPolicyActivityWhenTermsSpanClicked() {
        val spannableString = activity.policyText.text  as SpannableString
        val spans = spannableString.getSpans(0, spannableString.length, ClickableSpan::class.java)

        assertTrue(spans.isNotEmpty())
        spans.last().onClick(activity.policyText)
        verify(activity.router).showPolicy(activity, terms)
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

    @Test
    fun shouldAddTextWatcherWhenOnResume() {
        assertNotNull(Shadows.shadowOf(activity.emailInput).watchers.find { it is SimpleTextWatcher })
        assertNotNull(Shadows.shadowOf(activity.passwordInput).watchers.find { it is SimpleTextWatcher })
    }

    @Test
    fun shouldRemoveTextWatcherWhenOnPause() {
        context = RuntimeEnvironment.application.baseContext
        val activity = Robolectric.buildActivity(SignUpActivity::class.java,
            SignUpActivity.getStartIntent(context, null, null))
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
    fun shouldLoadDataWhenTryAgainButtonClicked() {
        activity.onTryAgainButtonClicked()

        verify(activity.presenter).signUp(any(), any(), any(), any(), any())
    }

    @Test
    fun shouldShowProgressWhenCheckPassed() {
        assertEquals(View.GONE, activity.loadingView.visibility)
        activity.onCheckPassed()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)
        assertEquals(ContextCompat.getDrawable(context, R.color.colorBackgroundLightTranslucent), activity.lceLayout.loadingView.background)
    }

    @Test
    fun shouldHideProgressWhenFailure() {
        assertEquals(View.GONE, activity.loadingView.visibility)
        activity.loadData()
        assertEquals(View.VISIBLE, activity.loadingView.visibility)
        activity.onFailure()
        assertEquals(View.GONE, activity.loadingView.visibility)
    }

    @After
    fun tearDown() {
        activity.finish()
    }
}