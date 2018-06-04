package com.shopapp.data.impl

import com.shopapp.data.rx.RxCallbackCompletable
import com.shopapp.data.rx.RxCallbackSingle
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.gateway.Api
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepositoryImpl(private val api: Api) : AuthRepository {

    override fun signUp(firstName: String, lastName: String, email: String, password: String,
                        phone: String): Completable {
        return Completable.create { api.signUp(firstName, lastName, email, password, phone,
                RxCallbackCompletable(it)) }
    }

    override fun signIn(email: String, password: String): Completable {
        return Completable.create { api.signIn(email, password, RxCallbackCompletable(it)) }
    }

    override fun signOut(): Completable {
        return Completable.create { api.signOut(RxCallbackCompletable(it)) }
    }

    override fun isSignedIn(): Single<Boolean> {
        return Single.create<Boolean> { api.isSignedIn(RxCallbackSingle<Boolean>(it)) }
    }

    override fun resetPassword(email: String): Completable {
        return Completable.create {
            api.resetPassword(email, RxCallbackCompletable(it))
        }
    }
}