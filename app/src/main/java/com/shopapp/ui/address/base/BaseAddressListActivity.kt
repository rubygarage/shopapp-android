package com.shopapp.ui.address.base

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.shopapp.R
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.base.adapter.AddressListAdapter
import com.shopapp.ui.address.base.contract.AddressListPresenter
import com.shopapp.ui.address.base.contract.AddressListView
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.base.recycler.divider.SpaceDecoration
import kotlinx.android.synthetic.main.activity_address_list.*
import javax.inject.Inject

abstract class BaseAddressListActivity<A : AddressListAdapter, V : AddressListView, P : AddressListPresenter<V>> :
    BaseLceActivity<Pair<Address?, List<Address>>, V, P>(),
    AddressListView,
    AddressListAdapter.ActionButtonListener,
    View.OnClickListener {

    @Inject
    lateinit var addressListPresenter: P
    protected lateinit var addressListAdapter: A
    protected val dataList: MutableList<Address> = mutableListOf()
    protected var defaultAddress: Address? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecycler()
        loadData()
    }

    //INIT

    override fun createPresenter() = addressListPresenter

    override fun getContentView() = R.layout.activity_address_list

    //SETUP

    protected abstract fun getAdapter(): A

    private fun setupRecycler() {
        addressListAdapter = getAdapter()
        val decorator = SpaceDecoration(topSpace = resources.getDimensionPixelSize(R.dimen.address_item_divider))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = addressListAdapter
        recyclerView.addItemDecoration(decorator)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getAddressList()
    }

    override fun showContent(data: Pair<Address?, List<Address>>) {
        super.showContent(data)
        defaultAddress = data.first
        dataList.clear()
        dataList.addAll(data.second)
        addressListAdapter.defaultAddress = defaultAddress
        addressListAdapter.notifyDataSetChanged()
    }

    protected open fun deleteAddress(address: Address) {
        val isRemoved = dataList.remove(address)
        if (isRemoved) {
            addressListAdapter.notifyDataSetChanged()
            presenter.deleteAddress(address.id)
        }
    }

    //CALLBACK

    override fun onDefaultButtonClicked(address: Address) {
        changeState(LceLayout.LceState.LoadingState())
        presenter.setDefaultAddress(address, dataList)
    }

    override fun onDeleteButtonClicked(address: Address) {
        deleteAddress(address)
    }
}