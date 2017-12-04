package com.domain.interactor.auth

import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<String>() {

    override fun buildUseCaseCompletable(params: String): Completable {
        return authRepository.forgotPassword(params)
    }
}