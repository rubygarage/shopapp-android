package com.shopapp.domain.repository

import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
import com.shopapp.gateway.entity.Customer
import io.reactivex.Completable
import io.reactivex.Single

interface CustomerRepository {

    fun getCustomer(): Single<Customer?>

    fun addCustomerAddress(address: Address): Completable

    fun updateCustomerAddress(address: Address): Completable

    fun deleteCustomerAddress(addressId: String): Completable

    fun setDefaultShippingAddress(addressId: String): Completable

    fun updateCustomer(firstName: String, lastName: String, phone: String): Single<Customer>

    fun updatePassword(password: String): Completable

    fun getCountries(): Single<List<Country>>

    fun updateCustomerSettings(isAcceptMarketing: Boolean): Completable
}