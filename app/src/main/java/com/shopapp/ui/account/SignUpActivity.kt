package com.shopapp.ui.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.shopapp.gateway.entity.Policy
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.getTrimmedString
import com.shopapp.ext.hideKeyboard
import com.shopapp.ui.account.contract.SignUpPresenter
import com.shopapp.ui.account.contract.SignUpView
import com.shopapp.ui.account.router.SignUpRouter
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_sign_up.*
import javax.inject.Inject


class SignUpActivity :
    BaseLceActivity<Unit, SignUpView, SignUpPresenter>(),
    SignUpView {

    @Inject
    lateinit var signUpPresenter: SignUpPresenter

    @Inject
    lateinit var router: SignUpRouter

    private lateinit var emailTextWatcher: TextWatcher
    private lateinit var passwordTextWatcher: TextWatcher

    companion object {
        const val PRIVACY_POLICY = "privacy_policy"
        const val TERMS_OF_SERVICE = "terms_of_service"

        fun getStartIntent(context: Context, privacyPolicy: Policy?, termsOfService: Policy?): Intent {
            val intent = Intent(context, SignUpActivity::class.java)
            intent.putExtra(PRIVACY_POLICY, privacyPolicy)
            intent.putExtra(TERMS_OF_SERVICE, termsOfService)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.sign_up))
        setupInputListeners()
        setupInfoText()
        createButton.setOnClickListener {
            clearFormFocus()
            loadData(true)
        }
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

    override fun getContentView() = R.layout.activity_sign_up

    override fun createPresenter() = signUpPresenter

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent().inject(this)
    }

    override fun useModalStyle() = true

    //SETUP

    private fun setupInfoText() {

        val privacyPolicy: Policy? = intent.getParcelableExtra(PRIVACY_POLICY)
        val termsOfService: Policy? = intent.getParcelableExtra(TERMS_OF_SERVICE)

        if (privacyPolicy != null && termsOfService != null) {

            val privacyPolicySpan = SpannableString(getString(R.string.privacy_policy))
            val clickablePrivacyPolicySpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    router.showPolicy(this@SignUpActivity, privacyPolicy)
                }
            }
            privacyPolicySpan.setSpan(clickablePrivacyPolicySpan, 0, privacyPolicySpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            val termsOfServiceSpan = SpannableString(getString(R.string.terms_of_service))
            val clickableTermsOfServiceSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    router.showPolicy(this@SignUpActivity, termsOfService)
                }
            }
            termsOfServiceSpan.setSpan(clickableTermsOfServiceSpan, 0, termsOfServiceSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            val space = Character.SPACE_SEPARATOR.toChar().toString()
            policyText.text = TextUtils.concat(
                getString(R.string.policy_text),
                space,
                privacyPolicySpan,
                System.lineSeparator(),
                getString(R.string.and),
                space,
                termsOfServiceSpan
            )
            policyText.movementMethod = LinkMovementMethod.getInstance()
            policyText.highlightColor = Color.TRANSPARENT
        }
    }

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

    private fun checkInputFields(inputLayout: TextInputLayout) {
        createButton.isEnabled = emailInput.text.isNotBlank() && passwordInput.text.isNotBlank()
        if (inputLayout.isErrorEnabled) {
            inputLayout.isErrorEnabled = false
        }
    }

    private fun clearFormFocus() {
        createButton.requestFocus()
        createButton.hideKeyboard()
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.signUp(
            firstNameInput.getTrimmedString(),
            lastNameInput.getTrimmedString(),
            emailInput.getTrimmedString(),
            passwordInput.getTrimmedString(),
            phoneInput.getTrimmedString()
        )
    }

    override fun showContent(data: Unit) {
        super.showContent(data)
        showMessage(R.string.register_success_message)
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onTryAgainButtonClicked() {
        loadData(true)
    }

    override fun showEmailError() {
        emailInputLayout.error = getString(R.string.invalid_email_error_message)
    }

    override fun showPasswordError() {
        passwordInputLayout.error = getString(R.string.invalid_password_error_message)
    }

    override fun onCheckPassed() {
        changeState(LceLayout.LceState.LoadingState(true))
    }

    override fun onFailure() {
        changeState(LceLayout.LceState.ContentState)
    }
}