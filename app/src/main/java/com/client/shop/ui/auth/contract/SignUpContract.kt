package com.client.shop.ui.auth.contract

import com.client.shop.R
import com.client.shop.ext.isEmailValid
import com.client.shop.ext.isPasswordValid
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.CompletableUseCase
import com.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

interface SignUpView : BaseView<Unit> {

    fun showEmailError()

    fun showPasswordError()

    fun onCheckPassed()

    fun onFailure()
}

class SignUpPresenter @Inject constructor(private val signUpUseCase: SignUpUseCase) :
        BasePresenter<Unit, SignUpView>(arrayOf(signUpUseCase)) {

    fun signUp(firstName: String, lastName: String, email: String, password: String) {

        if (firstName.isEmpty() || lastName.isEmpty()) {
            view?.showMessage(R.string.empty_fields_error_message)
        } else if (!email.isEmailValid()) {
            view?.showEmailError()
        } else if (!password.isPasswordValid()) {
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

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<SignUpUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return with(params) {
            authRepository.signUp(firstName, lastName, email, password)
        }
    }

    data class Params(
            val firstName: String,
            val lastName: String,
            val email: String,
            val password: String
    )
}