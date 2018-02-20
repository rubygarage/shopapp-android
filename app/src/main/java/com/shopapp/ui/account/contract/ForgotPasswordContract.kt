package com.shopapp.ui.account.contract

<<<<<<< HEAD:app/src/main/java/com/shopapp/ui/account/contract/ForgotPasswordContract.kt
import com.shopapp.domain.interactor.account.ForgotPasswordUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
=======
import com.client.shop.ui.base.contract.BaseLcePresenter
import com.client.shop.ui.base.contract.BaseLceView
import com.domain.interactor.account.ForgotPasswordUseCase
import com.domain.validator.FieldValidator
>>>>>>> finish with forgot password activity:app/src/main/java/com/client/shop/ui/account/contract/ForgotPasswordContract.kt

interface ForgotPasswordView : BaseLceView<Unit> {
    fun showEmailValidError()
}

class ForgotPasswordPresenter(
    private val validator: FieldValidator,
    private val forgotPasswordUseCase: ForgotPasswordUseCase) :
    BaseLcePresenter<Unit, ForgotPasswordView>(forgotPasswordUseCase) {

    fun resetPassword(email: String) {
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