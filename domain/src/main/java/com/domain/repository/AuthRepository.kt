package com.domain.repository

import com.domain.entity.Customer
import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepository {

    fun signUp(firstName: String, lastName: String, email: String, password: String): Completable

    fun signIn(email: String, authToken: String): Completable

    fun signOut(): Completable

    fun isLoggedIn(): Single<Boolean>

    fun forgotPassword(email: String): Completable

    fun getCustomer(): Single<Customer>
}