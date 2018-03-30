package com.shopapp.ui.account.router

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Policy
import com.shopapp.ui.account.*
import com.shopapp.ui.address.account.AddressListActivity
import com.shopapp.ui.const.RequestCode
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.list.OrderListActivity
import com.shopapp.ui.policy.PolicyActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class AccountRouterTest {

    private lateinit var fragment: AccountFragment
    private lateinit var router: AccountRouter

    @Before
    fun setUp() {
        fragment = AccountFragment()
        router = AccountRouter()
        SupportFragmentTestUtil.startFragment(fragment, HomeActivity::class.java)
    }

    @Test
    fun shouldShowSignInActivity() {
        router.showSignInForResult(fragment, RequestCode.SIGN_IN)
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(SignInActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowSignUp() {
        val terms: Policy = mock()
        val privacy: Policy = mock()

        router.showSignUpForResult(fragment, privacy, terms, RequestCode.SIGN_UP)

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)

        assertEquals(SignUpActivity::class.java, shadowIntent.intentClass)
        assertEquals(terms, startedIntent.extras.getParcelable(SignUpActivity.TERMS_OF_SERVICE))
        assertEquals(privacy, startedIntent.extras.getParcelable(SignUpActivity.PRIVACY_POLICY))
    }

    @Test
    fun shouldShowOrder() {
        router.showOrderList(fragment.context)

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(OrderListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowPersonalInfo() {
        router.showPersonalInfoForResult(fragment, RequestCode.PERSONAL_INFO)

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(PersonalInfoActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowAddressList() {
        router.showAddressList(fragment.context)
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(AddressListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowAccountSettings() {
        router.showSettings(fragment.context)
        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(AccountSettingsActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowPrivacy() {
        val privacy: Policy = mock()
        router.showPolicy(fragment.context, privacy)

        val startedIntent = Shadows.shadowOf(fragment.activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(PolicyActivity::class.java, shadowIntent.intentClass)
        assertEquals(privacy, startedIntent.extras.getParcelable(PolicyActivity.EXTRA_POLICY))
    }
}