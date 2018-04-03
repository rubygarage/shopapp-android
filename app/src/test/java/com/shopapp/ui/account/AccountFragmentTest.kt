package com.shopapp.ui.account

import android.app.Activity
import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.Policy
import com.shopapp.gateway.entity.Shop
import com.shopapp.ui.const.RequestCode
import com.shopapp.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.fragment_account.*
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
    fun shouldShowSettingsMenuItemWhenUserIsAuthorised() {
        val menu = shadowOf(fragment.activity).optionsMenu
        assertFalse(menu.findItem(R.id.settings).isVisible)
        fragment.showContent(true)
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
    fun shouldShowAuthViewsIfNotSignedIn() {
        fragment.showContent(false)
        assertEquals(View.VISIBLE, fragment.unauthGroup.visibility)
        assertEquals(View.GONE, fragment.authGroup.visibility)
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
        assertEquals(View.VISIBLE, fragment.unauthGroup.visibility)
        assertEquals(View.GONE, fragment.authGroup.visibility)
    }

    @Test
    fun shouldShowMessageOnSignedOut() {
        val looper = Shadows.shadowOf(fragment.activity?.mainLooper)
        fragment.signedOut()
        looper.idle()
        Assert.assertEquals(ShadowToast.getTextOfLatestToast(), context.getString(R.string.logout_success_message))
    }

    @Test
    fun shouldCheckAuthOnNullCustomer() {
        fragment.customerReceived(null)
        verify(fragment.presenter, times(2)).isAuthorized()
    }

    @Test
    fun shouldShowCustomerTitleView() {
        val customer: Customer = mock {
            on { firstName } doReturn "name"
            on { lastName } doReturn "lastName"
        }
        val resultName = context.getString(R.string.full_name_pattern, "name", "lastName")
        fragment.customerReceived(customer)
        assertEquals(View.GONE, fragment.unauthGroup.visibility)
        assertEquals(View.VISIBLE, fragment.authGroup.visibility)
        assertEquals(resultName, fragment.name.text.toString())
        assertEquals("NL", fragment.avatarView.text.toString())
    }

    @Test
    fun shouldShowCustomerTitleViewWhenFirstNameIsEmpty() {
        val customer: Customer = mock {
            on { firstName } doReturn ""
            on { lastName } doReturn "lastName"
        }
        val resultName = context.getString(R.string.full_name_pattern, "", "lastName").trim()
        fragment.customerReceived(customer)
        assertEquals(View.GONE, fragment.unauthGroup.visibility)
        assertEquals(View.VISIBLE, fragment.authGroup.visibility)
        assertEquals(resultName, fragment.name.text.toString())
        assertEquals("L", fragment.avatarView.text.toString())
    }

    @Test
    fun shouldShowCustomerTitleViewWhenLastNameIsEmpty() {
        val customer: Customer = mock {
            on { firstName } doReturn "name"
            on { lastName } doReturn ""
        }
        val resultName = context.getString(R.string.full_name_pattern, "name", "").trim()
        fragment.customerReceived(customer)
        assertEquals(View.GONE, fragment.unauthGroup.visibility)
        assertEquals(View.VISIBLE, fragment.authGroup.visibility)
        assertEquals(resultName, fragment.name.text.toString())
        assertEquals("N", fragment.avatarView.text.toString())
    }

    @Test
    fun shouldShowCustomerEmailIfNameIsEmpty() {
        val customer: Customer = mock {
            on { firstName } doReturn ""
            on { lastName } doReturn ""
            on { email } doReturn "email@test.com"
        }
        fragment.customerReceived(customer)
        assertEquals(View.GONE, fragment.unauthGroup.visibility)
        assertEquals(View.VISIBLE, fragment.authGroup.visibility)
        assertEquals("email@test.com", fragment.name.text.toString())
        assertEquals("E", fragment.avatarView.text.toString())
    }

    @Test
    fun shouldShowSignIn() {
        fragment.signInButton.performClick()
        verify(fragment.router).showSignInForResult(fragment, RequestCode.SIGN_IN)
    }

    @Test
    fun shouldShowSignUp() {
        val shop: Shop = mock()
        val terms: Policy = mock()
        val privacy: Policy = mock()
        given(shop.termsOfService).willReturn(terms)
        given(shop.privacyPolicy).willReturn(privacy)
        fragment.shopReceived(shop)
        fragment.createAccount.performClick()
        verify(fragment.router).showSignUpForResult(fragment, privacy, terms, RequestCode.SIGN_UP)
    }

    @Test
    fun shouldShowOrder() {
        fragment.myOrders.performClick()
        verify(fragment.router).showOrderList(fragment.context)
    }

    @Test
    fun shouldShowPersonalInfo() {
        fragment.personalInfo.performClick()
        verify(fragment.router).showPersonalInfoForResult(fragment, RequestCode.PERSONAL_INFO)
    }

    @Test
    fun shouldShowAddressList() {
        fragment.shippingAddress.performClick()
        verify(fragment.router).showAddressList(fragment.context)
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