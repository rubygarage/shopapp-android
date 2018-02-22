package com.shopapp.domain.interactor.account

import com.shopapp.gateway.entity.Customer
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCustomerUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<Customer?, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Customer?> {
        return authRepository.getCustomer()
    }
}