package com.domain.interactor.account

import com.domain.entity.Customer
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCustomerUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<Customer?, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Customer?> {
        return authRepository.getCustomer()
    }
}