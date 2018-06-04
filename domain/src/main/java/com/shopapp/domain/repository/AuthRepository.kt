package com.shopapp.domain.repository

import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepository {

    fun signUp(firstName: String, lastName: String, email: String, password: String,
               phone: String): Completable

    fun signIn(email: String, password: String): Completable

    fun signOut(): Completable

    fun isSignedIn(): Single<Boolean>

    fun resetPassword(email: String): Completable
}