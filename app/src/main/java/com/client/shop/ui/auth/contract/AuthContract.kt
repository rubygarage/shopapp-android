package com.client.shop.ui.auth.contract

import com.domain.interactor.auth.AuthUseCase
import com.domain.interactor.auth.SignOutUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface AuthView : BaseLceView<Boolean> {

    fun signedOut()
}

class AuthPresenter @Inject constructor(
        private val authUseCase: AuthUseCase,
        private val signOutUseCase: SignOutUseCase
) :
        BaseLcePresenter<Boolean, AuthView>(authUseCase, signOutUseCase) {

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