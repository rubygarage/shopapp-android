package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) :
    CompletableUseCase<Unit>() {

    override fun buildUseCaseCompletable(params: Unit): Completable {
        return authRepository.signOut()
    }
}