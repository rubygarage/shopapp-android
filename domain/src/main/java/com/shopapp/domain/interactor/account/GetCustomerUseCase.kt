package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.entity.Customer
import io.reactivex.Single
import javax.inject.Inject

class GetCustomerUseCase @Inject constructor(private val repository: CustomerRepository) :
    SingleUseCase<Customer?, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Customer?> {
        return repository.getCustomer()
    }
}