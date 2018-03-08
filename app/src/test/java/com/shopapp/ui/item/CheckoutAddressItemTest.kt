package com.shopapp.ui.item

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Address
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.checkout.adapter.CheckoutAddressListAdapter
import kotlinx.android.synthetic.main.item_checkout_address.view.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CheckoutAddressItemTest {

    private lateinit var context: Context
    private lateinit var itemView: CheckoutAddressItem
    private lateinit var address: Address
    private val listener: CheckoutAddressListAdapter.AddressSelectListener = mock()

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        itemView = CheckoutAddressItem(context)
        address = MockInstantiator.newAddress()
        itemView.addressSelectListener = listener
    }

    @Test
    fun shouldRadioButtonBeCheckedWhenSetSelectedAddress() {
        itemView.setAddress(address, address, address)

        assertTrue(itemView.selectedAddressRadioButton.isChecked)
        verify(listener, never()).onAddressSelected(address)
    }

    @Test
    fun shouldRadioButtonBeUnChecked() {
        itemView.setAddress(address, address, mock())

        assertFalse(itemView.selectedAddressRadioButton.isChecked)
        verify(listener, never()).onAddressSelected(address)
    }

    @Test
    fun shouldCallAddressSelectListener() {
        itemView.setAddress(address, address, mock())
        itemView.selectedAddressRadioButton.isChecked = true

        verify(listener).onAddressSelected(address)
    }
}