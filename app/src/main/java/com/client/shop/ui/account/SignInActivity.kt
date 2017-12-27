package com.client.shop.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.TextWatcher
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.account.contract.SignInPresenter
import com.client.shop.ui.account.contract.SignInView
import com.client.shop.ui.account.di.AuthModule
import com.ui.base.lce.BaseActivity
import com.ui.custom.SimpleTextWatcher
import com.ui.ext.getTrimmedString
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : BaseActivity<Unit, SignInView, SignInPresenter>(),
        SignInView {

    @Inject lateinit var signInPresenter: SignInPresenter
    private lateinit var emailTextWatcher: TextWatcher
    private lateinit var passwordTextWatcher: TextWatcher

    companion object {
        fun getStartIntent(context: Context) = Intent(context, SignInActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.sign_in))
        setupInputListeners()
        setupButtonListeners()
    }

    override fun onResume() {
        super.onResume()
        if (this::emailTextWatcher.isInitialized && this::passwordTextWatcher.isInitialized) {
            emailInput.addTextChangedListener(emailTextWatcher)
            passwordInput.addTextChangedListener(passwordTextWatcher)
        }
    }

    override fun onPause() {
        super.onPause()
        emailInput.removeTextChangedListener(emailTextWatcher)
        passwordInput.removeTextChangedListener(passwordTextWatcher)
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_sign_in

    override fun createPresenter() = signInPresenter

    override fun useModalStyle() = true

    //SETUP

    private fun setupInputListeners() {
        emailTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields(emailInputLayout)
            }
        }
        passwordTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields(passwordInputLayout)
            }
        }
    }

    private fun setupButtonListeners() {
        loginButton.setOnClickListener { loadData(true) }
        forgotPassword.setOnClickListener { startActivity(ForgotPasswordActivity.getStartIntent(this)) }
    }

    private fun checkInputFields(inputLayout: TextInputLayout) {
        loginButton.isEnabled = emailInput.text.isNotBlank() && passwordInput.text.isNotBlank()
        if (inputLayout.isErrorEnabled) {
            inputLayout.isErrorEnabled = false
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.logIn(emailInput.getTrimmedString(), passwordInput.getTrimmedString())
    }

    override fun showContent(data: Unit) {
        super.showContent(data)
        showMessage(R.string.login_success_message)
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun showEmailError() {
        emailInputLayout.isErrorEnabled = true
        emailInputLayout.error = getString(R.string.invalid_email_error_message)
    }

    override fun showPasswordError() {
        passwordInputLayout.isErrorEnabled = true
        passwordInputLayout.error = getString(R.string.invalid_password_error_message)
    }

    override fun onCheckPassed() {
        changeUiState(false)
    }

    override fun onFailure() {
        changeUiState(true)
    }

    private fun changeUiState(isEnabled: Boolean) {
        emailInput.isEnabled = isEnabled
        passwordInput.isEnabled = isEnabled
        if (isEnabled) {
            progressBar.hide()
            loginButton.visibility = View.VISIBLE
        } else {
            progressBar.show()
            loginButton.visibility = View.GONE
        }
    }
}