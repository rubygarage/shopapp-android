package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.gateway.entity.Address
import io.reactivex.Single
import javax.inject.Inject

class CreateCustomerAddressUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<String, Address>() {

    override fun buildUseCaseSingle(params: Address): Single<String> {
        return authRepository.createCustomerAddress(params)
    }
}