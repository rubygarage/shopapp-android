package com.domain.repository

import com.domain.entity.Address
import com.domain.entity.Customer
import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepository {

    fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String): Completable

    fun signIn(email: String, authToken: String): Completable

    fun signOut(): Completable

    fun isLoggedIn(): Single<Boolean>

    fun forgotPassword(email: String): Completable

    fun getCustomer(): Single<Customer>

    fun createCustomerAddress(address: Address): Single<String>

    fun editCustomerAddress(addressId: String, address: Address): Completable

    fun deleteCustomerAddress(addressId: String): Completable

    fun setDefaultShippingAddress(addressId: String): Completable
}