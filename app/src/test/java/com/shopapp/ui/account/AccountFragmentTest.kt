package com.shopapp.ui.account

import android.app.Activity
import android.content.Context
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Policy
import com.shopapp.gateway.entity.Shop
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.RequestCode
import com.shopapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_lce.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class AccountFragmentTest {

    private lateinit var fragment: AccountFragment
    private lateinit var context: Context

    @Before
    fun setUp() {
        fragment = AccountFragment()
        startFragment(fragment, HomeActivity::class.java)
        context = RuntimeEnvironment.application.baseContext
    }

    @Test
    fun shouldChangeActivityTitle() {
        assertEquals(context.getString(R.string.my_account), fragment.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldGetShopInfoWhenShopIsNull() {
        verify(fragment.presenter).getShopInfo()
    }

    @Test
    fun shouldShowSettingsMenuItemWhenCustomerReceived() {
        val menu = shadowOf(fragment.activity).optionsMenu
        assertFalse(menu.findItem(R.id.settings).isVisible)
        fragment.customerReceived(MockInstantiator.newCustomer())
        assertTrue(menu.findItem(R.id.settings).isVisible)
    }

    @Test
    fun shouldHideSettingsMenuItemWhenUserDoesNotAuthorised() {
        val menu = shadowOf(fragment.activity).optionsMenu
        assertFalse(menu.findItem(R.id.settings).isVisible)
        fragment.showContent(false)
        assertFalse(menu.findItem(R.id.settings).isVisible)
    }

    @Test
    fun shouldShowPrivacy() {
        val shop: Shop = mock()
        val privacy: Policy = mock()
        given(shop.privacyPolicy).willReturn(privacy)

        fragment.shopReceived(shop)
        fragment.privacyPolicy.performClick()
        verify(fragment.router).showPolicy(fragment.context, privacy)
    }

    @Test
    fun shouldSetupTermsOfServiceRouting() {
        val shop: Shop = mock()
        val terms: Policy = mock()
        given(shop.termsOfService).willReturn(terms)

        fragment.shopReceived(shop)
        fragment.termsOfService.performClick()
        verify(fragment.router).showPolicy(fragment.context, terms)
    }

    @Test
    fun shouldSetupRefundPolicyRouting() {
        val shop: Shop = mock()
        val refund: Policy = mock()
        given(shop.refundPolicy).willReturn(refund)

        fragment.shopReceived(shop)
        fragment.refundPolicy.performClick()

        verify(fragment.router).showPolicy(fragment.context, refund)
    }

    @Test
    fun shouldShowUnAuthFragmentIfNotSignedIn() {
        fragment.showContent(false)
        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.accountContainer)
        assertNotNull(childFragment)
        assertTrue(childFragment is AccountUnAuthFragment)
    }

    @Test
    fun shouldShowAuthFragmentIfSignedIn() {
        fragment.customerReceived(MockInstantiator.newCustomer())
        val childFragment = fragment.childFragmentManager.findFragmentById(R.id.accountContainer)
        assertNotNull(childFragment)
        assertTrue(childFragment is AccountAuthFragment)
    }

    @Test
    fun shouldGetCustomerIfSignedIn() {
        fragment.showContent(true)
        verify(fragment.presenter).getCustomer()
    }

    @Test
    fun shouldCheckUserIsAuthorize() {
        fragment.loadData()
        verify(fragment.presenter, times(2)).isAuthorized()
    }

    @Test
    fun shouldCheckSessionOnSignedOut() {
        fragment.signedOut()
        verify(fragment.presenter, times(2)).isAuthorized()
    }

    @Test
    fun shouldShowMessageOnSignedOut() {
        val looper = Shadows.shadowOf(fragment.activity?.mainLooper)
        fragment.signedOut()
        looper.idle()
        Assert.assertEquals(
            ShadowToast.getTextOfLatestToast(),
            context.getString(R.string.logout_success_message)
        )
    }

    @Test
    fun shouldCheckAuthOnNullCustomer() {
        fragment.customerReceived(null)
        verify(fragment.presenter, times(2)).isAuthorized()
    }

    @Test
    fun shouldShowAccountSettings() {
        val shadow = shadowOf(fragment.activity)
        shadow.clickMenuItem(R.id.settings)
        verify(fragment.router).showSettings(fragment.context)
    }

    @Test
    fun shouldSignOut() {
        fragment.logout.performClick()
        verify(fragment.presenter).signOut()
    }

    @Test
    fun shouldLoadDataWhenOnActivityResultWithIsSignInAndResultOk() {
        fragment.onActivityResult(RequestCode.SIGN_IN, Activity.RESULT_OK, null)
        verify(fragment.presenter, times(2)).isAuthorized()
    }

    @Test
    fun shouldLoadDataWhenOnActivityResultWithIsSignUpAndResultOk() {
        fragment.onActivityResult(RequestCode.SIGN_UP, Activity.RESULT_OK, null)
        verify(fragment.presenter, times(2)).isAuthorized()
    }

    @Test
    fun shouldLoadDataWhenOnActivityResultWithPersonalInfoInAndResultOk() {
        fragment.onActivityResult(RequestCode.PERSONAL_INFO, Activity.RESULT_OK, null)
        verify(fragment.presenter, times(2)).isAuthorized()
    }

    @Test
    fun shouldNotLoadDataWhenOnActivityResultWithIsSignInAndResultCanceled() {
        fragment.onActivityResult(RequestCode.SIGN_IN, Activity.RESULT_CANCELED, null)
        verify(fragment.presenter, times(1)).isAuthorized()
    }

    @Test
    fun shouldNotLoadDataWhenOnActivityResultWithIsSignUpAndResultCanceled() {
        fragment.onActivityResult(RequestCode.SIGN_UP, Activity.RESULT_CANCELED, null)
        verify(fragment.presenter, times(1)).isAuthorized()
    }

    @Test
    fun shouldNotLoadDataWhenOnActivityResultWithPersonalInfoInAndResultCanceled() {
        fragment.onActivityResult(RequestCode.PERSONAL_INFO, Activity.RESULT_CANCELED, null)
        verify(fragment.presenter, times(1)).isAuthorized()
    }
}