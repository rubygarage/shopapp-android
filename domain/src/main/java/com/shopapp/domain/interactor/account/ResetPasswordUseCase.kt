package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val repository: AuthRepository) :
    CompletableUseCase<String>() {

    override fun buildUseCaseCompletable(params: String): Completable {
        return repository.resetPassword(params)
    }
}