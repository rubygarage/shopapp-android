package com.client.shop.ui.auth

import android.os.Bundle
import android.view.View
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.auth.contract.SignInPresenter
import com.client.shop.ui.auth.contract.SignInView
import com.client.shop.ui.auth.di.AuthModule
import com.client.shop.ui.base.ui.lce.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject

class SignInFragment :
        BaseFragment<Unit, SignInView, SignInPresenter>(),
        SignInView {

    @Inject lateinit var signInPresenter: SignInPresenter

    //ANDROID

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            loadData(true)
        }
    }

    //INIT

    override fun inject(component: AppComponent) {
        component.attachAuthComponent(AuthModule()).inject(this)
    }

    override fun getContentView() = R.layout.fragment_sign_in

    override fun createPresenter() = signInPresenter

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