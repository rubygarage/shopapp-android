package com.shopapp.ui.address.base

import android.content.Context
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.base.adapter.AddressListAdapter
import com.shopapp.ui.address.base.contract.AddressListPresenter
import com.shopapp.ui.address.base.contract.AddressListView
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class BaseAddressListActivityTest {

    private lateinit var context: Context
    private lateinit var activity: TestBaseAddressListActivity

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        activity = Robolectric.buildActivity(TestBaseAddressListActivity::class.java).create().get()
    }

    @Test
    fun shouldSetUpRecyclerView() {
        val recyclerView = activity.recyclerView
        assertNotNull(recyclerView.adapter)
        assertNotNull(recyclerView.layoutManager)
        assertEquals(1, recyclerView.itemDecorationCount)
    }

    @Test
    fun shouldLoadDataWhenOnCreate() {
        verify(activity.presenter).getAddressList()
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldShowContentView() {
        val size = 5
        val defaultAddress = MockInstantiator.newAddress()
        val addressList = MockInstantiator.newList(defaultAddress, size)
        activity.showContent(Pair(defaultAddress, addressList))
        val adapter = activity.recyclerView.adapter as AddressListAdapter

        assertEquals(size + 1, adapter.itemCount)
        assertEquals(defaultAddress, adapter.defaultAddress)
    }

    @Test
    fun shouldSetDefaultAddressWhenDefaultButtonClicked() {
        val address = MockInstantiator.newAddress()
        val addressList = MockInstantiator.newList(address, 5)
        activity.showContent(Pair(address, addressList))
        activity.onDefaultButtonClicked(address)

        verify(activity.presenter).setDefaultAddress(address, addressList)
    }

    @Test
    fun shouldDeleteAddressWhenDeleteButtonClicked() {
        val addressId = "addressId"
        val address = MockInstantiator.newAddress()
        given(address.id).willReturn(addressId)
        val addressList = MockInstantiator.newList(address, 5)
        activity.showContent(Pair(address, addressList))
        activity.onDeleteButtonClicked(address)

        verify(activity.presenter).deleteAddress(addressId)
    }

    class TestBaseAddressListActivity
        : BaseAddressListActivity<AddressListAdapter, AddressListView, AddressListPresenter<AddressListView>>() {

        override fun getAdapter() = AddressListAdapter(dataList, this, this)

        override fun inject() {
            TestShopApplication.testAppComponent.attachAddressComponent().inject(this)
        }

        override fun onEditButtonClicked(address: Address) {

        }

        override fun onClick(v: View?) {

        }
    }
}