package com.client.shop.ui.auth.contract

import com.domain.interactor.auth.SignInUseCase
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

class SignInPresenter @Inject constructor(private val signInUseCase: SignInUseCase) :
        BaseLcePresenter<Unit, SignInView>(signInUseCase) {

    private val validator = FieldValidator()

    fun logIn(email: String, password: String) {

        if (!validator.isEmailValid(email)) {
            view?.showEmailError()
        } else if (!validator.isPasswordValid(password)) {
            view?.showPasswordError()
        } else {
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

    fun forgotPassword(email: String) {

        if (!validator.isEmailValid(email)) {
            view?.showMessage(R.string.invalid_email_error_message)
        } else {
            forgotPasswordUseCase.execute(
                    { view?.showMessage(R.string.check_inbox_message) },
                    { resolveError(it) },
                    email
            )
        }
    }
}

class SignInUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<SignInUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return with(params) {
            authRepository.signIn(email, password)
        }
    }

    data class Params(
            val email: String,
            val password: String
    )
}

class ForgotPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<String>() {

    override fun buildUseCaseCompletable(params: String): Completable {
        return authRepository.forgotPassword(params)
    }
}