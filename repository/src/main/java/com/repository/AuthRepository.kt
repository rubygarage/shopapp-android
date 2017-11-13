package com.repository

import com.domain.entity.AccessData
import com.domain.entity.Customer
import io.reactivex.Single

interface AuthRepository {

    fun signUp(firstName: String, lastName: String, email: String, password: String): Single<Customer>

    fun signIn(email: String, authToken: String): Single<Customer>

    fun isLoggedIn(): Single<Boolean>

    fun requestToken(email: String, password: String): Single<AccessData>
}