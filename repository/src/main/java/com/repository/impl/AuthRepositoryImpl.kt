package com.repository.impl

import com.apicore.Api
import com.daocore.Dao
import com.domain.entity.AccessData
import com.domain.entity.Customer
import com.repository.AuthRepository
import com.repository.rx.RxCallback
import io.reactivex.Single

class AuthRepositoryImpl(private val api: Api, private val dao: Dao) : AuthRepository {

    override fun signUp(firstName: String, lastName: String, email: String, password: String): Single<Customer> {
        return Single.create<Customer> { api.signUp(firstName, lastName, email, password, RxCallback<Customer>(it)) }
                .flatMap { dao.saveCustomer(it) }
    }

    override fun signIn(email: String, authToken: String): Single<Customer> {
        return Single.create<Customer> { api.signIn(email, authToken, RxCallback<Customer>(it)) }
                .flatMap { dao.saveCustomer(it) }
    }

    override fun requestToken(email: String, password: String): Single<AccessData> {
        return Single.create<AccessData> { api.requestToken(email, password, RxCallback<AccessData>(it)) }
    }

    override fun isLoggedIn(): Single<Boolean> {
        return dao.isLoggedIn()
    }
}