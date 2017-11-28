package com.client.shop.ui.auth

import android.os.Bundle
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.auth.contract.SignUpPresenter
import com.client.shop.ui.auth.contract.SignUpView
import com.client.shop.ui.auth.di.AuthModule
import com.ui.base.lce.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up.*
import javax.inject.Inject

class SignUpFragment :
        BaseFragment<Unit, SignUpView, SignUpPresenter>(),
        SignUpView {

    @Inject lateinit var signUpPresenter: SignUpPresenter

    //ANDROID

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createButton.setOnClickListener {
            loadData(true)
        }
    }

    //INIT

    override fun getContentView() = R.layout.fragment_sign_up

    override fun createPresenter() = signUpPresenter

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.signUp(
                firstNameInput.text.trim().toString(),
                lastNameInput.text.trim().toString(),
                emailInput.text.trim().toString(),
                passwordInput.text.trim().toString())
    }

    override fun showContent(data: Unit) {
        super.showContent(data)
        showMessage(R.string.register_success_message)
        activity.finish()
    }

    override fun tryAgainButtonClicked() {
        loadData(true)
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
        firstNameInput.isEnabled = isEnabled
        lastNameInput.isEnabled = isEnabled
        emailInput.isEnabled = isEnabled
        passwordInput.isEnabled = isEnabled
        if (isEnabled) {
            progressBar.hide()
            createButton.visibility = View.VISIBLE
        } else {
            progressBar.show()
            createButton.visibility = View.GONE
        }
    }
}