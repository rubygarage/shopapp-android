package com.client.shop.ui.auth.contract

import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.SingleUseCase
import com.repository.AuthRepository
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