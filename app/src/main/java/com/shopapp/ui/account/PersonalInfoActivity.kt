package com.shopapp.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.widget.Toast
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.hideKeyboard
import com.shopapp.ext.setTextWhenDisable
import com.shopapp.gateway.entity.Customer
import com.shopapp.ui.account.contract.PersonalInfoPresenter
import com.shopapp.ui.account.contract.PersonalInfoView
import com.shopapp.ui.account.router.PersonalInfoRouter
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_personal_info.*
import javax.inject.Inject

class PersonalInfoActivity :
    BaseLceActivity<Customer, PersonalInfoView, PersonalInfoPresenter>(),
    PersonalInfoView {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, PersonalInfoActivity::class.java)
        }
    }

    @Inject
    lateinit var personalInfoPresenter: PersonalInfoPresenter

    @Inject
    lateinit var router: PersonalInfoRouter

    private lateinit var fieldTextWatcher: TextWatcher
    private var customer: Customer? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.personal_info))
        setupInputListeners()
        setupClickListeners()
        setupActionListeners()
        loadData()
    }

    private fun setupActionListeners() {
        phoneInput.setOnEditorActionListener { v, _, _ ->
            v.clearFocus()
            v.hideKeyboard()
            true
        }
    }

    override fun onResume() {
        super.onResume()
        addTextChangeListeners()
    }

    override fun onPause() {
        super.onPause()
        removeTextChangeListeners()
    }

    //SETUP

    private fun setupInputListeners() {
        fieldTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields()
            }
        }
    }

    private fun checkInputFields() {
        val isNameChanged = firstNameInput.text.toString() != customer?.firstName ?: ""
        val isLastNameChanged = lastNameInput.text.toString() != customer?.lastName ?: ""
        val isPhoneChanged = phoneInput.text.toString() != customer?.phone ?: ""

        saveButton.isEnabled = isNameChanged || isLastNameChanged || isPhoneChanged
    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            clearFormFocus()
            changeState(LceLayout.LceState.LoadingState(true))
            presenter.editCustomer(
                firstNameInput.text.toString(),
                lastNameInput.text.toString(),
                phoneInput.text.toString()
            )
        }
        changePassword.setOnClickListener {
            router.showChangePassword(this)
        }
    }

    private fun clearFormFocus() {
        containerConstraintLayout.requestFocus()
        containerConstraintLayout.hideKeyboard()
    }

    private fun addTextChangeListeners() {
        if (this::fieldTextWatcher.isInitialized) {
            firstNameInput.addTextChangedListener(fieldTextWatcher)
            lastNameInput.addTextChangedListener(fieldTextWatcher)
            phoneInput.addTextChangedListener(fieldTextWatcher)
        }
    }

    private fun removeTextChangeListeners() {
        firstNameInput.removeTextChangedListener(fieldTextWatcher)
        lastNameInput.removeTextChangedListener(fieldTextWatcher)
        phoneInput.removeTextChangedListener(fieldTextWatcher)
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent().inject(this)
    }

    override fun getContentView() = R.layout.activity_personal_info

    override fun createPresenter(): PersonalInfoPresenter {
        return personalInfoPresenter
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getCustomer()
    }

    override fun onCustomerChanged(customer: Customer) {
        Toast.makeText(this, R.string.customer_changed, Toast.LENGTH_SHORT).show()
        setResult(Activity.RESULT_OK)
        showContent(customer)
    }

    override fun showContent(data: Customer) {
        super.showContent(data)
        customer = data
        firstNameInput.setText(data.firstName)
        lastNameInput.setText(data.lastName)
        phoneInput.setText(data.phone)
    }

    override fun setupCustomerEmail(email: String) {
        emailInput.setTextWhenDisable(email)
    }

    override fun hideProgress() {
        changeState(LceLayout.LceState.ContentState)
    }
}