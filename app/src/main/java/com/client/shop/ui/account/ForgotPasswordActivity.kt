package com.client.shop.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.account.contract.ForgotPasswordPresenter
import com.client.shop.ui.account.contract.ForgotPasswordView
import com.client.shop.ui.account.di.AuthModule
import com.domain.validator.FieldValidator
import com.ui.base.lce.BaseActivity
import com.ui.base.lce.view.LceLayout
import com.ui.custom.SimpleTextWatcher
import com.ui.ext.hideKeyboard
import kotlinx.android.synthetic.main.activity_forgot_password.*
import javax.inject.Inject

class ForgotPasswordActivity :
    BaseActivity<Unit, ForgotPasswordView, ForgotPasswordPresenter>(),
    ForgotPasswordView,
    View.OnClickListener {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, ForgotPasswordActivity::class.java)
    }

    @Inject
    lateinit var forgotPasswordPresenter: ForgotPasswordPresenter
    @Inject
    lateinit var fieldValidator: FieldValidator
    private lateinit var emailTextWatcher: TextWatcher

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.forgot_password))

        emailTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                submitButton.isEnabled = fieldValidator.isEmailValid(s.toString())
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
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
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

    //CALLBACK

    override fun onClick(v: View) {
        v.requestFocus()
        v.hideKeyboard()
        changeState(LceLayout.LceState.LoadingState(true))
        presenter.resetPassword(emailInput.text.toString())
    }
}