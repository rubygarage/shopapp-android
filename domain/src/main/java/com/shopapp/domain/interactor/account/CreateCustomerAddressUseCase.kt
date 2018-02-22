package com.shopapp.domain.interactor.account

import com.shopapp.gateway.entity.Address
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class CreateCustomerAddressUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<String, CreateCustomerAddressUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<String> {
        return authRepository.createCustomerAddress(params.address)
    }

    class Params(
        val address: Address
    )
}