package com.client.shop.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.account.contract.PersonalInfoPresenter
import com.client.shop.ui.account.contract.PersonalInfoView
import com.client.shop.ui.account.di.AuthModule
import com.domain.entity.Customer
import com.ui.base.lce.BaseActivity
import com.ui.custom.SimpleTextWatcher
import com.ui.ext.hideKeyboard
import kotlinx.android.synthetic.main.activity_personal_info.*
import javax.inject.Inject

class PersonalInfoActivity :
    BaseActivity<Customer, PersonalInfoView, PersonalInfoPresenter>(),
    PersonalInfoView {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, PersonalInfoActivity::class.java)
        }
    }

    @Inject
    lateinit var personalInfoPresenter: PersonalInfoPresenter
    private lateinit var emailTextWatcher: TextWatcher
    private lateinit var fieldTextWatcher: TextWatcher
    private var customer: Customer? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.order_details))
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
        emailTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields()
                if (emailInputLayout.isErrorEnabled) {
                    emailInputLayout.isErrorEnabled = false
                }
            }
        }
        fieldTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields()
            }
        }

    }

    private fun checkInputFields() {
        val isEmailChanged = emailInput.text.isNotBlank() && emailInput.text.toString() != customer?.email
        val isNameChanged = firstNameInput.text.toString() != customer?.firstName ?: ""
        val isLastNameChanged = lastNameInput.text.toString() != customer?.lastName ?: ""
        val isPhoneChanged = phoneInput.text.toString() != customer?.phone ?: ""

        saveButton.isEnabled = isEmailChanged || isNameChanged || isLastNameChanged || isPhoneChanged

    }

    private fun setupClickListeners() {
        saveButton.setOnClickListener {
            clearFormFocus()
            presenter.editCustomer(
                firstNameInput.text.toString(),
                lastNameInput.text.toString(),
                emailInput.text.toString(),
                phoneInput.text.toString()
            )
        }

        changePassword.setOnClickListener {
            startActivity(ChangePasswordActivity.getStartIntent(this))
        }
    }

    private fun clearFormFocus() {
        containerConstraintLayout.requestFocus()
        containerConstraintLayout.hideKeyboard()
    }

    private fun addTextChangeListeners() {
        if (this::emailTextWatcher.isInitialized) {
            emailInput.addTextChangedListener(emailTextWatcher)
        }
        if (this::fieldTextWatcher.isInitialized) {
            firstNameInput.addTextChangedListener(fieldTextWatcher)
            lastNameInput.addTextChangedListener(fieldTextWatcher)
            phoneInput.addTextChangedListener(fieldTextWatcher)
        }
    }

    private fun removeTextChangeListeners() {
        emailInput.removeTextChangedListener(emailTextWatcher)
        firstNameInput.removeTextChangedListener(fieldTextWatcher)
        lastNameInput.removeTextChangedListener(fieldTextWatcher)
        phoneInput.removeTextChangedListener(fieldTextWatcher)
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
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
        emailInput.setText(data.email)
        phoneInput.setText(data.phone)
    }

    override fun showEmailError() {
        emailInputLayout.error = getString(R.string.invalid_email_error_message)
    }

    override fun showUpdateProgress() {
        progressBar.show()
        saveButton.visibility = View.INVISIBLE
    }

    override fun hideUpdateProgress() {
        progressBar.hide()
        saveButton.visibility = View.VISIBLE
    }
}