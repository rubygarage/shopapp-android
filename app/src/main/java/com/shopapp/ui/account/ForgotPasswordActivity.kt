package com.shopapp.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.hideKeyboard
import com.shopapp.ui.account.contract.ForgotPasswordPresenter
import com.shopapp.ui.account.contract.ForgotPasswordView
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_forgot_password.*
import javax.inject.Inject

class ForgotPasswordActivity :
    BaseLceActivity<Unit, ForgotPasswordView, ForgotPasswordPresenter>(),
    ForgotPasswordView,
    View.OnClickListener {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, ForgotPasswordActivity::class.java)
    }

    @Inject
    lateinit var forgotPasswordPresenter: ForgotPasswordPresenter
    private lateinit var emailTextWatcher: TextWatcher

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.forgot_password))

        emailTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                submitButton.isEnabled = s.isNotEmpty()
                if (emailInputLayout.isErrorEnabled) {
                    emailInputLayout.isErrorEnabled = false
                }
            }
        }
        submitButton.setOnClickListener(this)
        resend.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if (this::emailTextWatcher.isInitialized) {
            emailInput.addTextChangedListener(emailTextWatcher)
        }
    }

    override fun onPause() {
        super.onPause()
        emailInput.removeTextChangedListener(emailTextWatcher)
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent().inject(this)
    }

    override fun getContentView() = R.layout.activity_forgot_password

    override fun createPresenter() = forgotPasswordPresenter

    //LCE

    override fun showContent(data: Unit) {
        super.showContent(data)
        resetEmail.text = emailInput.text
        inputGroup.visibility = View.GONE
        resendGroup.visibility = View.VISIBLE
    }

    override fun showMessage(message: String) {
        super.showMessage(message)
        changeState(LceLayout.LceState.ContentState)
    }

    override fun showEmailValidError() {
        emailInputLayout.isErrorEnabled = true
        emailInputLayout.error = getString(R.string.invalid_email_error_message)
    }

    //CALLBACK

    override fun onClick(v: View) {
        v.requestFocus()
        v.hideKeyboard()
        changeState(LceLayout.LceState.LoadingState(true))
        presenter.forgotPassword(emailInput.text.toString())
    }
}