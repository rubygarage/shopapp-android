package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.entity.Country
import io.reactivex.Single
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(private val repository: CustomerRepository) :
        SingleUseCase<List<Country>, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<List<Country>> {
        return repository.getCountries()
    }
}