package com.shopapp.ui.account

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.di.module.TestConfigModule
import com.shopapp.gateway.entity.Customer
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.RequestCode
import kotlinx.android.synthetic.main.fragment_account_auth.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class AccountAuthFragmentTest {

    private lateinit var fragment: AccountAuthFragment
    private lateinit var context: Context
    private lateinit var customer: Customer

    @Before
    fun setUp() {
        context = RuntimeEnvironment.application.baseContext
        customer = MockInstantiator.newCustomer()
    }

    @Test
    fun shouldShowCustomerTitleView() {
        val customer: Customer = mock {
            on { firstName } doReturn "name"
            on { lastName } doReturn "lastName"
        }
        fragment = AccountAuthFragment.newInstance(customer)
        startFragment(fragment)
        val resultName = context.getString(R.string.full_name_pattern, "name", "lastName")
        assertEquals(resultName, fragment.name.text.toString())
        assertEquals("NL", fragment.avatarView.text.toString())
    }

    @Test
    fun shouldShowCustomerTitleViewWhenFirstNameIsEmpty() {
        val customer: Customer = mock {
            on { firstName } doReturn ""
            on { lastName } doReturn "lastName"
        }
        fragment = AccountAuthFragment.newInstance(customer)
        startFragment(fragment)
        val resultName = context.getString(R.string.full_name_pattern, "", "lastName").trim()
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
        fragment = AccountAuthFragment.newInstance(customer)
        startFragment(fragment)
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
        fragment = AccountAuthFragment.newInstance(customer)
        startFragment(fragment)

        assertEquals("email@test.com", fragment.name.text.toString())
        assertEquals("E", fragment.avatarView.text.toString())
    }

    @Test
    fun shouldShowOrder() {
        fragment = AccountAuthFragment.newInstance(customer)
        given(TestConfigModule.config.isOrdersEnabled).willReturn(true)
        startFragment(fragment)
        fragment.myOrders.performClick()
        verify(fragment.router).showOrderList(fragment.context)
        assertEquals(View.VISIBLE, fragment.myOrders.visibility)
    }

    @Test
    fun shouldHideShowOrder() {
        fragment = AccountAuthFragment.newInstance(customer)
        given(TestConfigModule.config.isOrdersEnabled).willReturn(false)
        startFragment(fragment)
        assertEquals(View.GONE, fragment.myOrders.visibility)
    }

    @Test
    fun shouldShowPersonalInfo() {
        fragment = AccountAuthFragment.newInstance(customer)
        startFragment(fragment)
        fragment.personalInfo.performClick()
        verify(fragment.router).showPersonalInfoForResult(fragment, RequestCode.PERSONAL_INFO)
    }

    @Test
    fun shouldShowAddressList() {
        fragment = AccountAuthFragment.newInstance(customer)
        startFragment(fragment)
        fragment.shippingAddress.performClick()
        verify(fragment.router).showAddressList(fragment.context)
    }
}