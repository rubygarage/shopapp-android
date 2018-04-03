package com.shopapp.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.TextWatcher
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.getTrimmedString
import com.shopapp.ui.account.contract.SignInPresenter
import com.shopapp.ui.account.contract.SignInView
import com.shopapp.ui.account.router.SignInRouter
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_sign_in.*
import javax.inject.Inject

class SignInActivity : BaseLceActivity<Unit, SignInView, SignInPresenter>(),
    SignInView {

    @Inject
    lateinit var signInPresenter: SignInPresenter

    @Inject
    lateinit var router: SignInRouter

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
        ShopApplication.appComponent.attachAuthComponent().inject(this)
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
        forgotPassword.setOnClickListener { router.showForgotPassword(this) }
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
            changeState(LceLayout.LceState.ContentState)
        } else {
            changeState(LceLayout.LceState.LoadingState(true))
        }
    }
}