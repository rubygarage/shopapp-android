package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.entity.Address
import io.reactivex.Completable
import javax.inject.Inject

class AddCustomerAddressUseCase @Inject constructor(private val repository: CustomerRepository) :
    CompletableUseCase<Address>() {

    override fun buildUseCaseCompletable(params: Address): Completable {
        return repository.addCustomerAddress(params)
    }
}