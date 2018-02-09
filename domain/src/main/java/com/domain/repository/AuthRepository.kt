package com.domain.repository

import com.client.shop.getaway.entity.Address
import com.client.shop.getaway.entity.Country
import com.client.shop.getaway.entity.Customer
import io.reactivex.Completable
import io.reactivex.Single

interface AuthRepository {

    fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String): Completable

    fun signIn(email: String, authToken: String): Completable

    fun signOut(): Completable

    fun isLoggedIn(): Single<Boolean>

    fun forgotPassword(email: String): Completable

    fun getCustomer(): Single<Customer?>

    fun createCustomerAddress(address: Address): Single<String>

    fun editCustomerAddress(addressId: String, address: Address): Completable

    fun deleteCustomerAddress(addressId: String): Completable

    fun setDefaultShippingAddress(addressId: String): Completable

    fun editCustomer(firstName: String, lastName: String, phone: String): Single<Customer>

    fun changePassword(password: String): Completable

    fun getCountries(): Single<List<Country>>

    fun updateAccountSettings(isAcceptMarketing: Boolean): Completable
}