package com.shopapp.ui.account.contract

import com.shopapp.domain.interactor.account.ForgotPasswordUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView

interface ForgotPasswordView : BaseLceView<Unit> {
    fun showEmailValidError()
}

class ForgotPasswordPresenter(
    private val validator: FieldValidator,
    private val forgotPasswordUseCase: ForgotPasswordUseCase) :
    BaseLcePresenter<Unit, ForgotPasswordView>(forgotPasswordUseCase) {

    fun forgotPassword(email: String) {
        if (!validator.isEmailValid(email)) {
            view?.showEmailValidError()
        } else {
            forgotPasswordUseCase.execute(
                { view?.showContent(Unit) },
                { resolveError(it) },
                email
            )
        }
    }
}