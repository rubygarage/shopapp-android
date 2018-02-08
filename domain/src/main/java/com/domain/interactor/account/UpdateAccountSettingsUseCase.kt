package com.domain.interactor.account

import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateAccountSettingsUseCase @Inject constructor(private val authRepository: AuthRepository) :
    CompletableUseCase<Boolean>() {

    override fun buildUseCaseCompletable(params: Boolean): Completable {
        return authRepository.updateAccountSettings(params)
    }
}
