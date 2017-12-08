package com.client.shop.ui.account.contract

import com.domain.interactor.account.SignUpUseCase
import com.domain.validator.FieldValidator
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface SignUpView : BaseLceView<Unit> {

    fun showEmailError()

    fun showPasswordError()

    fun onCheckPassed()

    fun onFailure()
}

class SignUpPresenter @Inject constructor(private val signUpUseCase: SignUpUseCase) :
        BaseLcePresenter<Unit, SignUpView>(signUpUseCase) {

    private val validator = FieldValidator()

    fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String) {

        var isError = false
        if (!validator.isEmailValid(email)) {
            isError = true
            view?.showEmailError()
        }
        if (!validator.isPasswordValid(password)) {
            isError = true
            view?.showPasswordError()
        }
        if (!isError) {
            view?.onCheckPassed()
            signUpUseCase.execute(
                    { view?.showContent(Unit) },
                    {
                        view?.onFailure()
                        resolveError(it)
                    },
                    SignUpUseCase.Params(firstName, lastName, email, password, phone)
            )
        }
    }
}