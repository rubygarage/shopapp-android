package com.shopapp.ui.address.checkout.adapter

import com.nhaarman.mockito_kotlin.*
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.item.CheckoutAddressItem
import org.junit.Assert
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
class CheckoutAddressListAdapterTest {

    @Mock
    private lateinit var dataList: List<Address>

    private lateinit var adapter: CheckoutAddressListAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(5)
        adapter = CheckoutAddressListAdapter(dataList, mock(), mock(), mock())
    }

    @Test
    fun shouldReturnCheckoutAddressItem() {
        val itemView = adapter.getItemView(RuntimeEnvironment.application.applicationContext, 0)
        Assert.assertTrue(itemView is CheckoutAddressItem)
    }

    @Test
    fun shouldCallSetAddress() {
        val itemView: CheckoutAddressItem = mock()
        val address: Address = mock()
        adapter.bindData(itemView, address, 1)
        verify(itemView).setAddress(eq(address), anyOrNull(), anyOrNull())
    }
}