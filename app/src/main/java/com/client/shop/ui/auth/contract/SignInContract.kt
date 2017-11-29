package com.client.shop.ui.auth.contract

import com.domain.interactor.base.CompletableUseCase
import com.domain.validator.FieldValidator
import com.repository.AuthRepository
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import io.reactivex.Completable
import javax.inject.Inject

interface SignInView : BaseView<Unit> {

    fun showEmailError()

    fun showPasswordError()

    fun onCheckPassed()

    fun onFailure()
}

class SignInPresenter @Inject constructor(private val signInUseCase: SignInUseCase) :
        BasePresenter<Unit, SignInView>(arrayOf(signInUseCase)) {

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