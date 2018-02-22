package com.shopapp.domain.interactor.account

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.AuthRepository
import io.reactivex.Completable
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) :
    CompletableUseCase<SignUpUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return with(params) {
            authRepository.signUp(firstName, lastName, email, password, phone)
        }
    }

    data class Params(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val phone: String
    )
}