package com.domain.interactor.account

import com.domain.entity.Address
import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class CreateCustomerAddressUseCase @Inject constructor(private val authRepository: AuthRepository) :
    CompletableUseCase<CreateCustomerAddressUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return authRepository.createCustomerAddress(params.address)
            .flatMapCompletable {
                return@flatMapCompletable if (params.isDefault) {
                    authRepository.setDefaultShippingAddress(it)
                } else {
                    Completable.complete()
                }
            }
    }

    class Params(
        val address: Address,
        val isDefault: Boolean
    )
}