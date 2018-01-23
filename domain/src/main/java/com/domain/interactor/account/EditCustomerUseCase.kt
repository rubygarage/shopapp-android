package com.domain.interactor.account

import com.domain.entity.Customer
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class EditCustomerUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<Customer, EditCustomerUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Customer> {
        return with(params) {
            authRepository.editCustomer(firstName, lastName, email, phone)
        }
    }

    data class Params(
        val firstName: String,
        val lastName: String,
        val email: String,
        val phone: String
    )
}