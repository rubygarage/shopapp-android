package com.client.shop.ui.auth

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.auth.contract.SignInPresenter
import com.client.shop.ui.auth.contract.SignInView
import com.client.shop.ui.auth.di.AuthModule
import com.ui.base.lce.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject

class SignInFragment :
        BaseFragment<Unit, SignInView, SignInPresenter>(),
        SignInView {

    @Inject lateinit var signInPresenter: SignInPresenter
    private lateinit var forgotPasswordDialog: AlertDialog

    //ANDROID

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupForgotDialog()
        setupButtons()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
    }

    override fun getContentView() = R.layout.fragment_sign_in

    override fun createPresenter() = signInPresenter

    //SETUP

    private fun setupForgotDialog() {
        forgotPasswordDialog = AlertDialog.Builder(context)
                .setTitle(R.string.reset_password_title)
                .setView(R.layout.dialog_forgot_password)
                .setPositiveButton(R.string.submit_button, { _, _ ->
                    val emailInput: EditText? = forgotPasswordDialog.findViewById(R.id.emailInput)
                    emailInput?.let { presenter.forgotPassword(emailInput.text.toString().trim()) }
                    emailInput?.setText("")
                })
                .setNegativeButton(R.string.cancel_button, { _, _ -> })
                .create()
    }

    private fun setupButtons() {
        loginButton.setOnClickListener {
            loadData(true)
        }
        forgotPassword.setOnClickListener {
            forgotPasswordDialog.show()
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.logIn(emailInput.text.trim().toString(), passwordInput.text.trim().toString())
    }

    override fun showContent(data: Unit) {
        super.showContent(data)
        showMessage(R.string.login_success_message)
        activity.finish()
    }

    override fun showEmailError() {
        emailInput.error = getString(R.string.invalid_email_error_message)
    }

    override fun showPasswordError() {
        passwordInput.error = getString(R.string.invalid_password_error_message)
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