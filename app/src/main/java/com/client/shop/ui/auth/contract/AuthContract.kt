package com.client.shop.ui.auth.contract

import com.domain.interactor.base.SingleUseCase
import com.repository.AuthRepository
import com.ui.contract.BasePresenter
import com.ui.contract.BaseView
import io.reactivex.Single
import javax.inject.Inject

interface AuthView : BaseView<Boolean>

class AuthPresenter @Inject constructor(private val authUseCase: AuthUseCase) :
        BasePresenter<Boolean, AuthView>(arrayOf(authUseCase)) {

    fun isAuthorized() {

        authUseCase.execute(
                { view?.showContent(it) },
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