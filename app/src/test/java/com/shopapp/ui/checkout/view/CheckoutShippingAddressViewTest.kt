package com.shopapp.ui.checkout.view

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.test.MockInstantiator
import kotlinx.android.synthetic.main.item_address.view.*
import kotlinx.android.synthetic.main.item_address_header.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CheckoutShippingAddressViewTest {

    @Mock
    private lateinit var editClickListener: View.OnClickListener

    @Mock
    private lateinit var addAddressClickListener: View.OnClickListener

    private lateinit var context: Context
    private lateinit var view: CheckoutShippingAddressView

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        context = RuntimeEnvironment.application.baseContext
        view = CheckoutShippingAddressView(context)
        view.setClickListeners(editClickListener, addAddressClickListener)
    }

    @Test
    fun shouldShowAddNewAddressButtonOnlyWhenAddressNull() {
        view.setAddress(null)

        assertEquals(View.GONE, view.editButton.visibility)
        assertEquals(View.VISIBLE, view.addNewAddressButton.visibility)
        assertEquals(View.GONE, view.addressContent.visibility)
    }

    @Test
    fun shouldShowAddressContentWhenAddressNull() {
        val address = MockInstantiator.newAddress()
        view.setAddress(address)

        assertEquals(View.VISIBLE, view.editButton.visibility)
        assertEquals(View.GONE, view.addNewAddressButton.visibility)
        assertEquals(View.VISIBLE, view.addressContent.visibility)
    }

    @Test
    fun shouldCallTryAgainWhenButtonClicked() {
        view.editButton.performClick()
        verify(editClickListener).onClick(view.editButton)
    }

    @Test
    fun shouldCallBackToShopWhenButtonClicked() {
        view.addNewAddressButton.performClick()
        verify(addAddressClickListener).onClick(view.addNewAddressButton)
    }
}