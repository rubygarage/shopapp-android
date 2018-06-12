package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.CustomerRepository
import io.reactivex.Completable
import javax.inject.Inject

class DeleteCustomerAddressUseCase @Inject constructor(private val repository: CustomerRepository) :
    CompletableUseCase<String>() {

    override fun buildUseCaseCompletable(params: String): Completable {
        return repository.deleteCustomerAddress(params)
    }
}