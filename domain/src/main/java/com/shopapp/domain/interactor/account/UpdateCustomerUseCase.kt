package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.entity.Customer
import io.reactivex.Single
import javax.inject.Inject

class UpdateCustomerUseCase @Inject constructor(private val repository: CustomerRepository) :
    SingleUseCase<Customer, UpdateCustomerUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Customer> {
        return with(params) {
            repository.updateCustomer(firstName, lastName, phone)
        }
    }

    data class Params(
        val firstName: String,
        val lastName: String,
        val phone: String
    )
}