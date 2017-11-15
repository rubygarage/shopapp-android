package com.client.shop.ui.auth.contract

import com.client.shop.ext.isEmailValid
import com.client.shop.ext.isPasswordValid
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.CompletableUseCase
import com.domain.entity.Customer
import com.domain.interactor.base.SingleUseCase
import com.repository.AuthRepository
import io.reactivex.Completable
import com.repository.SessionRepository
import com.ui.contract.BasePresenter
import com.ui.contract.BaseView
import io.reactivex.Single
import javax.inject.Inject

interface SignInView : BaseView<Unit> {

    fun showEmailError()

    fun showPasswordError()

    fun onCheckPassed()

    fun onFailure()
}

class SignInPresenter @Inject constructor(private val signInUseCase: SignInUseCase) :
        BasePresenter<Unit, SignInView>(arrayOf(signInUseCase)) {

    fun logIn(email: String, password: String) {

        if (!email.isEmailValid()) {
            view?.showEmailError()
        } else if (!password.isPasswordValid()) {
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