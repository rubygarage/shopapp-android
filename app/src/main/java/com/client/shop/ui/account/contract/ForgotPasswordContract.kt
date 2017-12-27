package com.client.shop.ui.account.contract

import com.domain.interactor.account.ForgotPasswordUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView

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