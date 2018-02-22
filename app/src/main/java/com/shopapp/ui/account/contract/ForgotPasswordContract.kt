package com.shopapp.ui.account.contract

import com.shopapp.domain.interactor.account.ForgotPasswordUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView

interface ForgotPasswordView : BaseLceView<Unit>

class ForgotPasswordPresenter(private val forgotPasswordUseCase: ForgotPasswordUseCase) :
    BaseLcePresenter<Unit, ForgotPasswordView>(forgotPasswordUseCase) {

    fun resetPassword(email: String) {
        forgotPasswordUseCase.execute(
            { view?.showContent(Unit) },
            { resolveError(it) },
            email
        )
    }
}