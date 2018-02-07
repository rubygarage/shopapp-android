package com.ui.module.address

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.domain.entity.Address
import com.ui.R
import com.ui.base.lce.BaseActivity
import com.ui.base.lce.view.LceLayout
import com.ui.base.recycler.divider.SpaceDecoration
import com.ui.module.address.adapter.AddressListAdapter
import com.ui.module.address.contract.AddressListPresenter
import com.ui.module.address.contract.AddressListView
import kotlinx.android.synthetic.main.activity_address_list.*
import javax.inject.Inject

abstract class BaseAddressListActivity<A : AddressListAdapter, V : AddressListView, P : AddressListPresenter<V>> :
    BaseActivity<Pair<Address?, List<Address>>, V, P>(),
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