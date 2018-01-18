package com.shopify.ui.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.domain.entity.Address
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.constant.Extra
import com.shopify.ui.address.adapter.AddressListAdapter
import com.shopify.ui.address.contract.AddressListPresenter
import com.shopify.ui.address.contract.AddressListView
import com.shopify.ui.address.di.AddressModule
import com.shopify.ui.item.AddressItem
import com.shopify.ui.payment.card.CardActivity
import com.ui.base.lce.BaseActivity
import com.ui.base.recycler.divider.SpaceDecoration
import com.ui.const.RequestCode
import kotlinx.android.synthetic.main.activity_address_list.*
import javax.inject.Inject

class AddressListActivity :
    BaseActivity<List<Address>, AddressListView, AddressListPresenter>(),
    AddressListView,
    AddressItem.ActionListener,
    View.OnClickListener {

    companion object {

        private const val CHECKOUT_ID = "checkout_id"
        private const val ADDRESS = "address"

        fun getStartIntent(
            context: Context,
            checkoutId: String?,
            address: Address?
        ): Intent {
            val intent = Intent(context, AddressListActivity::class.java)
            intent.putExtra(CHECKOUT_ID, checkoutId)
            intent.putExtra(ADDRESS, address)
            return intent
        }

        fun getStartIntent(context: Context): Intent {
            val intent = getStartIntent(context, null, null)
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            return intent
        }
    }

    @Inject
    lateinit var addressListPresenter: AddressListPresenter
    private lateinit var addressListAdapter: AddressListAdapter
    private val dataList: MutableList<Address> = mutableListOf()
    private var checkoutId: String? = null
    private var address: Address? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkoutId = intent.getStringExtra(CHECKOUT_ID)
        address = address ?: intent.getParcelableExtra(ADDRESS)

        val titleRes =
            if (checkoutId != null) R.string.shipping_address else R.string.billing_address
        setTitle(getString(titleRes))

        setupRecycler()
        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.ADD_ADDRESS && resultCode == Activity.RESULT_OK) {
            if (checkoutId != null) {
                addressChanged()
            } else {
                val address: Address? = data?.extras?.getParcelable(Extra.ADDRESS)
                addressSelected(address)
            }
        } else if (requestCode == RequestCode.EDIT_ADDRESS && resultCode == Activity.RESULT_OK) {
            addressChanged()
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
        addressListAdapter = AddressListAdapter(
            dataList, checkoutId == null, this, this
        )
        addressListAdapter.defaultAddress = address
        val decorator =
            SpaceDecoration(topSpace = resources.getDimensionPixelSize(R.dimen.address_item_divider))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = addressListAdapter
        recyclerView.addItemDecoration(decorator)
    }

    private fun addressChanged() {
        setResult(Activity.RESULT_OK)
        loadData()
    }

    private fun addressSelected(address: Address?) {
        address?.let { startActivity(CardActivity.getStartIntent(this, it)) }
        finish()
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
        setResult(Activity.RESULT_OK)
        loadData()
    }

    //CALLBACK

    override fun onEditButtonClicked(address: Address) {
        val checkoutId = if (this.address == address) this.checkoutId else null
        startActivityForResult(
            AddressActivity.getStartIntent(this, checkoutId, address),
            RequestCode.EDIT_ADDRESS
        )
    }

    override fun onDeleteButtonClicked(address: Address) {
        val isRemoved = dataList.remove(address)
        if (isRemoved) {
            addressListAdapter.notifyDataSetChanged()
            presenter.deleteAddress(address.id)
        }
    }

    override fun onAddressChecked(address: Address) {
        val finalCheckoutId = checkoutId
        if (finalCheckoutId != null) {
            presenter.setShippingAddress(finalCheckoutId, address)
        } else {
            addressSelected(address)
        }
    }

    override fun onClick(v: View?) {
        startActivityForResult(
            AddressActivity.getStartIntent(this), RequestCode.ADD_ADDRESS
        )
    }
}