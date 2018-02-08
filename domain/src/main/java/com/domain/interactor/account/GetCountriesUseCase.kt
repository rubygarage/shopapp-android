package com.domain.interactor.account

import com.domain.entity.Country
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCountriesUseCase @Inject constructor(private val authRepository: AuthRepository) :
    SingleUseCase<List<Country>, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<List<Country>> {
        return authRepository.getCountries()
    }
}