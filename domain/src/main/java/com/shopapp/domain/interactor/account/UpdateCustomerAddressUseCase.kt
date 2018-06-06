package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.entity.Address
import io.reactivex.Completable
import javax.inject.Inject

class UpdateCustomerAddressUseCase @Inject constructor(private val repository: CustomerRepository) :
    CompletableUseCase<Address>() {

    override fun buildUseCaseCompletable(params: Address): Completable {
        return repository.updateCustomerAddress(params)
    }
}