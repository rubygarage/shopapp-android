package com.shopapp.ui.account.contract

import com.shopapp.domain.interactor.account.ChangePasswordUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView

interface ChangePasswordView : BaseLceView<Unit> {

    fun passwordValidError()

    fun passwordsMatchError()

    fun showUpdateProgress()

    fun hideUpdateProgress()

    fun passwordChanged()
}

class ChangePasswordPresenter(
    private val validator: FieldValidator,
    private val changePasswordUseCase: ChangePasswordUseCase) :
    BaseLcePresenter<Unit, ChangePasswordView>(changePasswordUseCase) {

    fun changePassword(password: String, confirmPassword: String) {
        var isError = false

        if (!validator.isPasswordValid(password)) {
            isError = true
            view?.passwordValidError()
        }

        if (password != confirmPassword) {
            isError = true
            view?.passwordsMatchError()
        }

        if (!isError) {
            view?.showUpdateProgress()
            changePasswordUseCase.execute(
                {
                    view?.let {
                        it.hideUpdateProgress()
                        it.passwordChanged()
                    }
                },
                {
                    resolveError(it)
                    view?.hideUpdateProgress()
                },
                password
            )
        }
    }
}