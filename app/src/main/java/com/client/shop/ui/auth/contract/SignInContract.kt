package com.client.shop.ui.auth.contract

import com.client.shop.ext.isEmailValid
import com.client.shop.ext.isPasswordValid
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.SingleUseCase
import com.domain.entity.Customer
import com.repository.AuthRepository
import com.repository.SessionRepository
import io.reactivex.Single
import javax.inject.Inject

interface SignInView : BaseView<Customer> {

    fun showEmailError()

    fun showPasswordError()

    fun onCheckPassed()

    fun onFailure()
}

class SignInPresenter @Inject constructor(private val signInUseCase: SignInUseCase) :
        BasePresenter<Customer, SignInView>(arrayOf(signInUseCase)) {

    fun logIn(email: String, password: String) {

        if (!email.isEmailValid()) {
            view?.showEmailError()
        } else if (!password.isPasswordValid()) {
            view?.showPasswordError()
        } else {
            view?.onCheckPassed()
            signInUseCase.execute(
                    { view?.showContent(it) },
                    {
                        view?.onFailure()
                        resolveError(it)
                    },
                    SignInUseCase.Params(email, password))
        }
    }
}

class SignInUseCase @Inject constructor(
        private val authRepository: AuthRepository,
        private val sessionRepository: SessionRepository
) :
        SingleUseCase<Customer, SignInUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Customer> {
        return with(params) {
            authRepository.requestToken(email, password)
                    .flatMap {
                        sessionRepository.saveSession(it)
                        authRepository.signIn(email, it.accessToken)
                    }
        }
    }

    data class Params(
            val email: String,
            val password: String
    )
}