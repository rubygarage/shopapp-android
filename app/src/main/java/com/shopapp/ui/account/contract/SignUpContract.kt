package com.shopapp.ui.account.contract

import com.shopapp.domain.interactor.account.SignUpUseCase
import com.shopapp.domain.interactor.shop.ConfigUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Config
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface SignUpView : BaseLceView<Config> {

    fun showEmailError()

    fun showPasswordError()

    fun onCheckPassed()

    fun onSignUpFinished()

    fun onFailure()
}

class SignUpPresenter @Inject constructor(
    private val configUseCase: ConfigUseCase,
    private val fieldValidator: FieldValidator,
    private val signUpUseCase: SignUpUseCase
) :
    BaseLcePresenter<Config, SignUpView>(configUseCase, signUpUseCase) {

    fun getConfig() {
        configUseCase.execute(
            { view.showContent(it) },
            { resolveError(it) },
            Unit
        )
    }

    fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String) {

        var isError = false
        if (!fieldValidator.isEmailValid(email)) {
            isError = true
            view?.showEmailError()
        }
        if (!fieldValidator.isPasswordValid(password)) {
            isError = true
            view?.showPasswordError()
        }
        if (!isError) {
            view?.onCheckPassed()
            signUpUseCase.execute(
                { view?.onSignUpFinished() },
                {
                    view?.onFailure()
                    resolveError(it)
                },
                SignUpUseCase.Params(firstName, lastName, email, password, phone)
            )
        }
    }
}