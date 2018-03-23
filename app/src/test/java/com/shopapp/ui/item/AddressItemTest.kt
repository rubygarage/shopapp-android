package com.shopapp.ui.item

import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.entity.Address
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.base.adapter.AddressListAdapter
import kotlinx.android.synthetic.main.item_address.view.*
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
class AddressItemTest {

    private lateinit var context: Context
    private lateinit var itemView: AddressItem
    private lateinit var address: Address
    private val actionListener: AddressListAdapter.ActionButtonListener = mock()

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        itemView = AddressItem(context)
        address = MockInstantiator.newAddress()
        itemView.actionListener = actionListener
    }

    @Test
    fun shouldShowCorrectDataWhenSetDefaultAddress() {
        itemView.setAddress(address, address)

        assertFalse(itemView.deleteButton.isEnabled)
        assertFalse(itemView.defaultButton.isEnabled)
    }

    @Test
    fun shouldShowCorrectDataWhenSetAddressNotDefault() {
        itemView.setAddress(address, null)

        assertTrue(itemView.deleteButton.isEnabled)
        assertTrue(itemView.defaultButton.isEnabled)
    }

    @Test
    fun shouldCallActionListenerWhenEditButtonClicked() {
        itemView.setAddress(address, null)
        itemView.editButton.performClick()

        verify(actionListener).onEditButtonClicked(address)
    }

    @Test
    fun shouldCallActionListenerWhenDeleteButtonClicked() {
        itemView.setAddress(address, null)
        itemView.deleteButton.performClick()

        verify(actionListener).onDeleteButtonClicked(address)
    }

    @Test
    fun shouldCallActionListenerWhenDefaultButtonClicked() {
        itemView.setAddress(address, null)
        itemView.defaultButton.performClick()

        verify(actionListener).onDefaultButtonClicked(address)
    }
}