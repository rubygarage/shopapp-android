package com.shopapp.domain.interactor.account

import com.shopapp.gateway.entity.Customer
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class EditCustomerUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<Customer, EditCustomerUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Customer> {
        return with(params) {
            authRepository.editCustomer(firstName, lastName, phone)
        }
    }

    data class Params(
        val firstName: String,
        val lastName: String,
        val phone: String
    )
}