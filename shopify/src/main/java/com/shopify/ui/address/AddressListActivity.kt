package com.shopify.ui.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.domain.entity.Address
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.ui.address.adapter.AddressListAdapter
import com.shopify.ui.address.contract.AddressListPresenter
import com.shopify.ui.address.contract.AddressListView
import com.shopify.ui.address.di.AddressModule
import com.shopify.ui.item.AddressItem
import com.ui.base.lce.BaseActivity
import com.ui.base.recycler.divider.SpaceDecoration
import com.ui.const.RequestCode
import kotlinx.android.synthetic.main.activity_address_list.*
import javax.inject.Inject

class AddressListActivity :
        BaseActivity<List<Address>, AddressListView, AddressListPresenter>(),
        AddressListView,
        AddressItem.ActionListener {

    companion object {

        private const val CHECKOUT_ID = "checkout_id"
        private const val ADDRESS = "address"

        fun getStartIntent(context: Context, checkoutId: String, address: Address?): Intent {
            val intent = Intent(context, AddressListActivity::class.java)
            intent.putExtra(CHECKOUT_ID, checkoutId)
            intent.putExtra(ADDRESS, address)
            return intent
        }
    }

    @Inject lateinit var addressListPresenter: AddressListPresenter
    private lateinit var addressListAdapter: AddressListAdapter
    private val dataList: MutableList<Address> = mutableListOf()
    private var checkoutId: String? = null
    private var address: Address? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.shipping_address))

        checkoutId = intent.getStringExtra(CHECKOUT_ID)
        address = address ?: intent.getParcelableExtra(ADDRESS)

        addNewAddressButton.setOnClickListener {
            startActivityForResult(
                    AddressActivity.getStartIntent(this), RequestCode.ADD_ADDRESS)
        }

        setupRecycler()
        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.ADD_ADDRESS || requestCode == RequestCode.EDIT_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                loadData()
            }
        }
    }

    //INIT

    override fun inject() {
        ShopifyWrapper.component.attachAddressComponent(AddressModule()).inject(this)
    }

    override fun createPresenter() = addressListPresenter

    override fun getContentView() = R.layout.activity_address_list

    //SETUP

    private fun setupRecycler() {
        addressListAdapter = AddressListAdapter(dataList, this)
        addressListAdapter.defaultAddress = address
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

    override fun showContent(data: List<Address>) {
        super.showContent(data)
        dataList.clear()
        dataList.addAll(data)
        addressListAdapter.notifyDataSetChanged()
    }

    override fun selectedAddressChanged(address: Address) {
        this.address = address
        addressListAdapter.defaultAddress = address
        loadData()
    }

    //CALLBACK

    override fun onEditButtonClicked(address: Address) {
        val checkoutId = if (this.address == address) this.checkoutId else null
        startActivityForResult(AddressActivity.getStartIntent(this, checkoutId, address), RequestCode.EDIT_ADDRESS)
    }

    override fun onDeleteButtonClicked(address: Address) {
        val isRemoved = dataList.remove(address)
        if (isRemoved) {
            addressListAdapter.notifyDataSetChanged()
            presenter.deleteAddress(address.id)
        }
    }

    override fun onAddressChecked(address: Address) {
        checkoutId?.let {
            presenter.setShippingAddress(it, address)
        }
    }
}