package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.CustomerRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateCustomerSettingsUseCase @Inject constructor(private val repository: CustomerRepository) :
        CompletableUseCase<Boolean>() {

    override fun buildUseCaseCompletable(params: Boolean): Completable {
        return repository.updateCustomerSettings(params)
    }
}
