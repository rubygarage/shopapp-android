package com.shopapp.ui.address.base.adapter

import android.view.View
import com.nhaarman.mockito_kotlin.*
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.item.AddressHeaderItem
import com.shopapp.ui.item.AddressItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
class AddressListAdapterTest {

    companion object {
        private const val SIZE = 4
    }

    @Mock
    private lateinit var dataList: List<Address>

    @Mock
    private lateinit var clickListener: AddressListAdapter.ActionButtonListener

    @Mock
    private lateinit var headerClickListener: View.OnClickListener

    private lateinit var adapter: AddressListAdapter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        given(dataList.size).willReturn(SIZE)
        adapter = AddressListAdapter(dataList, clickListener, headerClickListener)
    }

    @Test
    fun shouldReturnCorrectCount() {
        assertEquals(SIZE + 1, adapter.itemCount)
    }

    @Test
    fun shouldReturnAddressItem() {
        val itemView = adapter.getItemView(RuntimeEnvironment.application.applicationContext, 0)
        assertTrue(itemView is AddressItem)
    }

    @Test
    fun shouldReturnAddressHeaderItem() {
        val itemView = adapter.getHeaderView(RuntimeEnvironment.application.applicationContext)
        assertTrue(itemView is AddressHeaderItem)
    }

    @Test
    fun shouldCallSetAddress() {
        val itemView: AddressItem = mock()
        val address: Address = mock()
        adapter.bindData(itemView, address, 1)
        verify(itemView).setAddress(eq(address), anyOrNull())
    }
}