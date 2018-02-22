package com.shopapp.domain.interactor.account

import com.shopapp.gateway.entity.Country
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<List<Country>, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<List<Country>> {
        return authRepository.getCountries()
    }
}