package com.shopapp.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.hideKeyboard
import com.shopapp.ui.account.contract.ChangePasswordPresenter
import com.shopapp.ui.account.contract.ChangePasswordView
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_change_password.*
import javax.inject.Inject

class ChangePasswordActivity :
    BaseLceActivity<Unit, ChangePasswordView, ChangePasswordPresenter>(),
    ChangePasswordView,
    View.OnClickListener {

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
        updateButton.setOnClickListener(this)
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
        ShopApplication.appComponent.attachAuthComponent().inject(this)
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

    //LCE

    private fun clearFormFocus() {
        containerConstraintLayout.requestFocus()
        containerConstraintLayout.hideKeyboard()
    }

    override fun passwordChanged() {
        finish()
    }

    override fun showUpdateProgress() {
        changeState(LceLayout.LceState.LoadingState(true))
    }

    override fun hideUpdateProgress() {
        changeState(LceLayout.LceState.ContentState)
    }

    override fun passwordValidError() {
        passwordInputLayout.error = getString(R.string.invalid_password_error_message)
    }

    override fun passwordsMatchError() {
        passwordConfirmInputLayout.error = getString(R.string.password_match_error_message)
    }

    //CALLBACK

    override fun onClick(v: View?) {
        clearFormFocus()
        presenter.changePassword(passwordInput.text.toString(), passwordConfirmInput.text.toString())
    }
}