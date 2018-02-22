package com.shopapp.domain.interactor.account

import com.shopapp.gateway.entity.Address
import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class EditCustomerAddressUseCase @Inject constructor(private val authRepository: AuthRepository) :
    CompletableUseCase<EditCustomerAddressUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return authRepository.editCustomerAddress(params.addressId, params.address)
    }

    class Params(val addressId: String, val address: Address)
}