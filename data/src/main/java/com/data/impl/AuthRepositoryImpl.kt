package com.data.impl

import com.data.rx.RxCallback
import com.domain.entity.Customer
import com.domain.entity.Error
import com.domain.network.Api
import com.domain.network.ApiCallback
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepositoryImpl(private val api: Api) : AuthRepository {

    override fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String): Completable {
        return Single.create<Customer> { api.signUp(firstName, lastName, email, password, phone, RxCallback<Customer>(it)) }
                .toCompletable()
    }

    override fun signIn(email: String, authToken: String): Completable {
        return Single.create<Customer> { api.signIn(email, authToken, RxCallback<Customer>(it)) }
                .toCompletable()
    }

    override fun signOut(): Completable {
        return Completable.create {
            api.signOut(object : ApiCallback<Unit> {
                override fun onResult(result: Unit) {
                    it.onComplete()
                }

                override fun onFailure(error: Error) {
                    it.onError(error)
                }
            })
        }
    }

    override fun isLoggedIn(): Single<Boolean> {
        return Single.create<Boolean> { api.isLoggedIn(RxCallback<Boolean>(it)) }
    }

    override fun forgotPassword(email: String): Completable {
        return Completable.create {
            api.forgotPassword(email, object : ApiCallback<Unit> {
                override fun onResult(result: Unit) {
                    it.onComplete()
                }

                override fun onFailure(error: Error) {
                    it.onError(error)
                }
            })
        }
    }

    override fun getCustomer(): Single<Customer> {
        return Single.create { api.getCustomer(RxCallback<Customer>(it)) }
    }
}