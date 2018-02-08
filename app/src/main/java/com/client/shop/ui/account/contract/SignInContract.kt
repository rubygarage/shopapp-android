package com.client.shop.ui.account.contract

import com.domain.interactor.account.SignInUseCase
import com.domain.validator.FieldValidator
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface SignInView : BaseLceView<Unit> {

    fun showEmailError()

    fun showPasswordError()

    fun onCheckPassed()

    fun onFailure()
}

class SignInPresenter @Inject constructor(
    private val fieldValidator: FieldValidator,
    private val signInUseCase: SignInUseCase
) :
    BaseLcePresenter<Unit, SignInView>(signInUseCase) {

    fun logIn(email: String, password: String) {

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
            signInUseCase.execute(
                { view?.showContent(Unit) },
                {
                    view?.onFailure()
                    resolveError(it)
                },
                SignInUseCase.Params(email, password))
        }
    }
}