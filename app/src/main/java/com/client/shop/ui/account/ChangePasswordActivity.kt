package com.client.shop.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.account.contract.ChangePasswordPresenter
import com.client.shop.ui.account.contract.ChangePasswordView
import com.client.shop.ui.account.di.AuthModule
import com.ui.base.lce.BaseActivity
import com.ui.custom.SimpleTextWatcher
import com.ui.ext.hideKeyboard
import kotlinx.android.synthetic.main.activity_change_password.*
import javax.inject.Inject

class ChangePasswordActivity :
    BaseActivity<Unit, ChangePasswordView, ChangePasswordPresenter>(),
    ChangePasswordView {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, ChangePasswordActivity::class.java)
    }

    @Inject
    lateinit var changePasswordPresenter: ChangePasswordPresenter
    private lateinit var passwordTextWatcher: TextWatcher
    private lateinit var passwordConfirmTextWatcher: TextWatcher

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.set_new_password))
        setupButtons()
        setupTextChangeListeners()
    }

    override fun onResume() {
        super.onResume()
        if (this::passwordTextWatcher.isInitialized) {
            passwordInput.addTextChangedListener(passwordTextWatcher)
        }
        if (this::passwordConfirmTextWatcher.isInitialized) {
            passwordConfirmInput.addTextChangedListener(passwordConfirmTextWatcher)
        }
    }

    override fun onPause() {
        super.onPause()
        passwordConfirmInput.removeTextChangedListener(passwordConfirmTextWatcher)
        passwordInput.removeTextChangedListener(passwordTextWatcher)
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_change_password

    override fun createPresenter() = changePasswordPresenter

    override fun useModalStyle() = true

    //SETUP

    private fun setupTextChangeListeners() {
        passwordTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields()
                if (passwordInputLayout.isErrorEnabled) {
                    passwordInputLayout.isErrorEnabled = false
                }
            }
        }
        passwordConfirmTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields()
                if (passwordConfirmInputLayout.isErrorEnabled) {
                    passwordConfirmInputLayout.isErrorEnabled = false
                }
            }
        }
    }

    private fun checkInputFields() {
        updateButton.isEnabled = passwordConfirmInput.text.isNotBlank() && passwordInput.text.isNotBlank()
    }

    private fun setupButtons() {
        updateButton.setOnClickListener {
            clearFormFocus()
            presenter.changePassword(passwordInput.text.toString(), passwordConfirmInput.text.toString())
        }
    }

    //LCE
    private fun clearFormFocus() {
        containerConstraintLayout.requestFocus()
        containerConstraintLayout.hideKeyboard()
    }

    override fun passwordChanged() {
        finish()
    }

    override fun showUpdateProgress() {
        progressBar.show()
        updateButton.visibility = View.INVISIBLE
    }

    override fun hideUpdateProgress() {
        progressBar.hide()
        updateButton.visibility = View.VISIBLE
    }

    override fun passwordValidError() {
        passwordInputLayout.error = getString(R.string.invalid_password_error_message)
    }

    override fun passwordsMatchError() {
        passwordConfirmInputLayout.error = getString(R.string.password_match_error_message)
    }
}