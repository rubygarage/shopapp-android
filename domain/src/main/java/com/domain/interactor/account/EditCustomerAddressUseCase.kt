package com.domain.interactor.account

import com.domain.entity.Address
import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class EditCustomerAddressUseCase @Inject constructor(private val authRepository: AuthRepository) :
    CompletableUseCase<EditCustomerAddressUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return authRepository.editCustomerAddress(params.addressId, params.address).andThen(
            if (params.isDefault) {
                authRepository.setDefaultShippingAddress(params.addressId)
            } else {
                Completable.complete()
            }
        )
    }

    class Params(val addressId: String, val address: Address, val isDefault: Boolean)
}