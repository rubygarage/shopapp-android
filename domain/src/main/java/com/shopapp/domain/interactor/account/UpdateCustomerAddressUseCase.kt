package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.entity.Address
import io.reactivex.Completable
import javax.inject.Inject

class UpdateCustomerAddressUseCase @Inject constructor(private val repository: CustomerRepository) :
    CompletableUseCase<UpdateCustomerAddressUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return repository.updateCustomerAddress(params.address)
    }

    class Params(val address: Address)
}