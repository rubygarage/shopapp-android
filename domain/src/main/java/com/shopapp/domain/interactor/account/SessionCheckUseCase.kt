package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class SessionCheckUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<Boolean, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Boolean> {
        return authRepository.isLoggedIn()
    }
}