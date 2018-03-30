package com.shopapp.ui.address.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.account.router.AddressListRouter
import com.shopapp.ui.address.base.BaseAddressListActivity
import com.shopapp.ui.address.base.adapter.AddressListAdapter
import com.shopapp.ui.address.base.contract.AddressListPresenter
import com.shopapp.ui.address.base.contract.AddressListView
import com.shopapp.ui.const.RequestCode
import javax.inject.Inject

class AddressListActivity : BaseAddressListActivity<AddressListAdapter, AddressListView, AddressListPresenter<AddressListView>>() {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, AddressListActivity::class.java)
    }

    @Inject
    lateinit var router: AddressListRouter

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.shipping_address))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val isAddAddressRequest = requestCode == RequestCode.ADD_SHIPPING_ADDRESS
        val isEditAddressRequest = requestCode == RequestCode.EDIT_SHIPPING_ADDRESS
        val isResultOk = resultCode == Activity.RESULT_OK
        if ((isAddAddressRequest || isEditAddressRequest) && isResultOk) {
            loadData()
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAddressComponent().inject(this)
    }

    override fun getAdapter() = AddressListAdapter(dataList, this, this)

    //CALLBACK

    override fun onEditButtonClicked(address: Address) {
        router.showAddressForResult(this, RequestCode.EDIT_SHIPPING_ADDRESS, address)
    }

    override fun onClick(v: View?) {
        router.showAddressForResult(this, RequestCode.ADD_SHIPPING_ADDRESS)
    }
}