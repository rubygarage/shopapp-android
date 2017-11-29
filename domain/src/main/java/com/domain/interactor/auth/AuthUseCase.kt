package com.domain.interactor.auth

import com.domain.interactor.base.SingleUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) :
        SingleUseCase<Boolean, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Boolean> {
        return authRepository.isLoggedIn()
    }
}