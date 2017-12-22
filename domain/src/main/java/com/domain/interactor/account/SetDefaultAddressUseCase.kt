package com.domain.interactor.account

import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class SetDefaultAddressUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<String>() {

    override fun buildUseCaseCompletable(params: String): Completable {
        return authRepository.setDefaultShippingAddress(params)
    }
}