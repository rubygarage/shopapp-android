package com.domain.interactor.account

import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<SignInUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return with(params) {
            authRepository.signIn(email, password)
        }
    }

    data class Params(
            val email: String,
            val password: String
    )
}