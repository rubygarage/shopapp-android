package com.shopify.ui.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import com.domain.entity.Address
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.ui.address.contract.AddressPresenter
import com.shopify.ui.address.contract.AddressView
import com.shopify.ui.address.di.AddressModule
import com.ui.base.lce.BaseActivity
import com.ui.base.lce.view.LceLayout
import com.ui.custom.SimpleTextWatcher
import com.ui.ext.getTrimmedString
import kotlinx.android.synthetic.main.activity_address.*
import javax.inject.Inject

class AddressActivity :
        BaseActivity<Address?, AddressView, AddressPresenter>(),
        AddressView {

    companion object {

        private const val CHECKOUT_ID = "checkout_id"
        private const val ADDRESS = "address"
        private const val EDIT_MODE = "edit_mode"

        fun getStartIntent(context: Context) = Intent(context, AddressActivity::class.java)

        fun getStartIntent(context: Context, checkoutId: String): Intent {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra(CHECKOUT_ID, checkoutId)
            return intent
        }

        fun getStartIntent(context: Context, checkoutId: String?, address: Address): Intent {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra(CHECKOUT_ID, checkoutId)
            intent.putExtra(ADDRESS, address)
            intent.putExtra(EDIT_MODE, true)
            return intent
        }
    }

    @Inject
    lateinit var addressPresenter: AddressPresenter
    private var isEditMode = false
    private var checkoutId: String? = null
    private var address: Address? = null
    private lateinit var fieldTextWatcher: TextWatcher

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.add_new_address))

        checkoutId = intent.getStringExtra(CHECKOUT_ID)
        address = intent.getParcelableExtra(ADDRESS)
        isEditMode = intent.getBooleanExtra(EDIT_MODE, false)

        fieldTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields()
            }
        }

        submitButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            submitButton.visibility = View.INVISIBLE
            if (isEditMode) {
                address?.let {
                    presenter.editAddress(checkoutId, it.id, getAddress(), defaultShippingAddress.isChecked)
                }
            } else {
                presenter.submitAddress(checkoutId, getAddress(), defaultShippingAddress.isChecked)
            }
        }

        if (isEditMode) {
            defaultShippingAddress.visibility = View.VISIBLE
            submitButton.setText(R.string.edit)
            fillFields(address)
            checkInputFields()
        } else {
            submitButton.setText(R.string.submit)
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::fieldTextWatcher.isInitialized) {
            countryInput.addTextChangedListener(fieldTextWatcher)
            firstNameInput.addTextChangedListener(fieldTextWatcher)
            lastNameInput.addTextChangedListener(fieldTextWatcher)
            addressInput.addTextChangedListener(fieldTextWatcher)
            cityInput.addTextChangedListener(fieldTextWatcher)
            postalCodeInput.addTextChangedListener(fieldTextWatcher)
        }
    }

    override fun onPause() {
        super.onPause()
        countryInput.removeTextChangedListener(fieldTextWatcher)
        firstNameInput.removeTextChangedListener(fieldTextWatcher)
        lastNameInput.removeTextChangedListener(fieldTextWatcher)
        addressInput.removeTextChangedListener(fieldTextWatcher)
        cityInput.removeTextChangedListener(fieldTextWatcher)
        postalCodeInput.removeTextChangedListener(fieldTextWatcher)
    }

    //INIT

    override fun getContentView() = R.layout.activity_address

    override fun createPresenter() = addressPresenter

    override fun inject() {
        ShopifyWrapper.component.attachAddressComponent(AddressModule()).inject(this)
    }

    //SETUP

    private fun checkInputFields() {
        submitButton.isEnabled = countryInput.text.isNotBlank() &&
                firstNameInput.text.isNotBlank() &&
                lastNameInput.text.isNotBlank() &&
                addressInput.text.isNotBlank() &&
                cityInput.text.isNotBlank() &&
                postalCodeInput.text.isNotBlank()
    }

    private fun getAddress() = Address(
            address = addressInput.getTrimmedString(),
            secondAddress = secondAddressInput.getTrimmedString(),
            city = cityInput.getTrimmedString(),
            country = countryInput.getTrimmedString(),
            state = stateInput.getTrimmedString(),
            firstName = firstNameInput.getTrimmedString(),
            lastName = lastNameInput.getTrimmedString(),
            zip = postalCodeInput.getTrimmedString(),
            phone = phoneInput.getTrimmedString()
    )

    private fun fillFields(address: Address?) {
        address?.let {
            firstNameInput.setText(it.firstName)
            lastNameInput.setText(it.lastName)
            addressInput.setText(it.address)
            secondAddressInput.setText(it.secondAddress)
            cityInput.setText(it.city)
            stateInput.setText(it.state ?: "")
            countryInput.setText(it.country)
            postalCodeInput.setText(it.zip)
            phoneInput.setText(it.phone ?: "")
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.checkIsLoggedIn()
    }

    override fun showContent(data: Address?) {
        super.showContent(data)
        fillFields(data)
    }

    override fun sessionChecked(isLoggedIn: Boolean) {
        defaultShippingAddress.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
        val checkoutId = checkoutId
        if (checkoutId != null) {
            presenter.getAddressFromCheckout(checkoutId)
        } else {
            changeState(LceLayout.LceState.ContentState)
        }
    }

    override fun addressChanged() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun submitAddressError() {
        progressBar.visibility = View.GONE
        submitButton.visibility = View.VISIBLE
    }
}