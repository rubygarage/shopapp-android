package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateAccountSettingsUseCase @Inject constructor(private val authRepository: AuthRepository) :
    CompletableUseCase<Boolean>() {

    override fun buildUseCaseCompletable(params: Boolean): Completable {
        return authRepository.updateAccountSettings(params)
    }
}
