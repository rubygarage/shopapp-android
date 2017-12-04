package com.client.shop.ui.auth.contract

import com.domain.interactor.auth.AuthUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface AuthView : BaseView<Boolean> {

    fun signedOut()
}

class AuthPresenter @Inject constructor(
        private val authUseCase: AuthUseCase,
        private val signOutUseCase: SignOutUseCase
) :
        BasePresenter<Boolean, AuthView>(arrayOf(authUseCase, signOutUseCase)) {

    fun isAuthorized() {

        authUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                Unit
        )
    }

    fun signOut() {

        signOutUseCase.execute(
                { view?.signedOut() },
                { resolveError(it) },
                Unit
        )
    }
}

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) :
        SingleUseCase<Boolean, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Boolean> {
        return authRepository.isLoggedIn()
    }
}

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<Unit>() {

    override fun buildUseCaseCompletable(params: Unit): Completable {
        return authRepository.signOut()
    }
}