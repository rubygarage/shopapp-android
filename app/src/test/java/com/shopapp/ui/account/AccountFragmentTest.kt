package com.shopapp.ui.account

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.Policy
import com.shopapp.gateway.entity.Shop
import com.shopapp.ui.address.account.AddressListActivity
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.list.OrderListActivity
import com.shopapp.ui.policy.PolicyActivity
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import org.junit.Assert
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
    fun shouldSetupPrivacyRouting() {
        val shop: Shop = mock()
        val privacy: Policy = mock()
        given(shop.privacyPolicy).willReturn(privacy)

        fragment.shopReceived(shop)
        fragment.privacyPolicy.performClick()

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(PolicyActivity::class.java, shadowIntent.intentClass)
        assertEquals(privacy, startedIntent.extras.getParcelable(PolicyActivity.EXTRA_POLICY))
    }

    @Test
    fun shouldSetupTermsOfServiceRouting() {
        val shop: Shop = mock()
        val terms: Policy = mock()
        given(shop.termsOfService).willReturn(terms)

        fragment.shopReceived(shop)
        fragment.termsOfService.performClick()

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(PolicyActivity::class.java, shadowIntent.intentClass)
        assertEquals(terms, startedIntent.extras.getParcelable(PolicyActivity.EXTRA_POLICY))
    }

    @Test
    fun shouldSetupRefundPolicyRouting() {
        val shop: Shop = mock()
        val refund: Policy = mock()
        given(shop.refundPolicy).willReturn(refund)

        fragment.shopReceived(shop)
        fragment.refundPolicy.performClick()

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(PolicyActivity::class.java, shadowIntent.intentClass)
        assertEquals(refund, startedIntent.extras.getParcelable(PolicyActivity.EXTRA_POLICY))
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
    fun shouldShowSignInActivity() {
        fragment.signInButton.performClick()
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(SignInActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowSignUpActivity() {
        val shop: Shop = mock()
        val terms: Policy = mock()
        val privacy: Policy = mock()
        given(shop.termsOfService).willReturn(terms)
        given(shop.privacyPolicy).willReturn(privacy)
        fragment.shopReceived(shop)
        fragment.createAccount.performClick()
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(SignUpActivity::class.java, shadowIntent.intentClass)
        assertEquals(terms, startedIntent.extras.getParcelable(SignUpActivity.TERMS_OF_SERVICE))
        assertEquals(privacy, startedIntent.extras.getParcelable(SignUpActivity.PRIVACY_POLICY))
    }

    @Test
    fun shouldShowOrderActivity() {
        fragment.myOrders.performClick()
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(OrderListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowPersonalInfoActivity() {
        fragment.personalInfo.performClick()
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(PersonalInfoActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowAddressListActivity() {
        fragment.shippingAddress.performClick()
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(AddressListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowAccountSettingsActivity() {
        val shadow = shadowOf(fragment.activity)
        shadow.clickMenuItem(R.id.settings)
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(AccountSettingsActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldSignOut() {
        fragment.logout.performClick()
        verify(fragment.presenter).signOut()
    }

}