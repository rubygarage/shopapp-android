package com.client.shop.ui.auth.contract

import com.client.shop.R
import com.domain.interactor.auth.SignUpUseCase
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

    fun signUp(firstName: String, lastName: String, email: String, password: String) {

        if (firstName.isEmpty() || lastName.isEmpty()) {
            view?.showMessage(R.string.empty_fields_error_message)
        } else if (!validator.isEmailValid(email)) {
            view?.showEmailError()
        } else if (!validator.isPasswordValid(password)) {
            view?.showPasswordError()
        } else {
            view?.onCheckPassed()
            signUpUseCase.execute(
                    { view?.showContent(Unit) },
                    {
                        view?.onFailure()
                        resolveError(it)
                    },
                    SignUpUseCase.Params(firstName, lastName, email, password)
            )
        }
    }
}