package com.shopapp.ui.account

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Policy
import com.shopapp.gateway.entity.Shop
import com.shopapp.ui.const.RequestCode
import kotlinx.android.synthetic.main.fragment_account_unauth.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class AccountUnAuthFragmentTest {

    private lateinit var fragment: AccountUnAuthFragment
    private lateinit var privacy: Policy
    private lateinit var terms: Policy

    @Before
    fun setUp() {
        val shop: Shop = mock()
        terms = mock()
        privacy = mock()
        given(shop.termsOfService).willReturn(terms)
        given(shop.privacyPolicy).willReturn(privacy)
        fragment = AccountUnAuthFragment.newInstance(shop)
        startFragment(fragment)
    }

    @Test
    fun shouldShowSignIn() {
        fragment.signInButton.performClick()
        verify(fragment.router).showSignInForResult(fragment, RequestCode.SIGN_IN)
    }

    @Test
    fun shouldShowSignUp() {
        fragment.createAccount.performClick()
        verify(fragment.router).showSignUpForResult(fragment, privacy, terms, RequestCode.SIGN_UP)
    }
}