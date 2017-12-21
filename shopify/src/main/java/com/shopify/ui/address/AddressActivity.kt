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
import com.ui.custom.SimpleTextWatcher
import com.ui.ext.getTrimmedString
import kotlinx.android.synthetic.main.activity_address.*
import javax.inject.Inject

class AddressActivity :
        BaseActivity<Address?, AddressView, AddressPresenter>(),
        AddressView {

    companion object {

        private const val CHECKOUT_ID = "checkout_id"

        fun getStartIntent(context: Context, checkoutId: String): Intent {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra(CHECKOUT_ID, checkoutId)
            return intent
        }
    }

    @Inject
    lateinit var addressPresenter: AddressPresenter
    private lateinit var checkoutId: String
    private lateinit var fieldTextWatcher: TextWatcher

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.add_new_address))

        checkoutId = intent.getStringExtra(CHECKOUT_ID)

        fieldTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields()
            }
        }
        submitButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            submitButton.visibility = View.INVISIBLE
            presenter.submitAddress(checkoutId, getAddress(), defaultShippingAddress.isChecked)
        }
        loadData()
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
            addressInput.getTrimmedString(),
            secondAddressInput.getTrimmedString(),
            cityInput.getTrimmedString(),
            countryInput.getTrimmedString(),
            stateInput.getTrimmedString(),
            firstNameInput.getTrimmedString(),
            lastNameInput.getTrimmedString(),
            postalCodeInput.getTrimmedString(),
            phoneInput.getTrimmedString()
    )

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.checkIsLoggedIn()
    }

    override fun showContent(data: Address?) {
        super.showContent(data)
        data?.let {
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

    override fun sessionChecked(isLoggedIn: Boolean) {
        defaultShippingAddress.visibility = if (isLoggedIn) View.VISIBLE else View.GONE
        presenter.getAddressFromCheckout(checkoutId)
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