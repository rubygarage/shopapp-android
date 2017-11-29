package com.domain.interactor.auth

import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) :
        CompletableUseCase<SignUpUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return with(params) {
            authRepository.signUp(firstName, lastName, email, password)
        }
    }

    data class Params(
            val firstName: String,
            val lastName: String,
            val email: String,
            val password: String
    )
}