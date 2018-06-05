package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.entity.Address
import io.reactivex.Single
import javax.inject.Inject

class AddCustomerAddressUseCase @Inject constructor(private val repository: CustomerRepository) :
        SingleUseCase<String, Address>() {

    override fun buildUseCaseSingle(params: Address): Single<String> {
        return repository.addCustomerAddress(params)
    }
}