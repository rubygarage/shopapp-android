package com.domain.interactor.auth

import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<Unit>() {

    override fun buildUseCaseCompletable(params: Unit): Completable {
        return authRepository.signOut()
    }
}