package com.data.impl

import com.data.rx.RxCallbackCompletable
import com.data.rx.RxCallbackSingle
import com.domain.entity.Address
import com.domain.entity.Customer
import com.domain.network.Api
import com.domain.repository.AuthRepository
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepositoryImpl(private val api: Api) : AuthRepository {

    override fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String): Completable {
        return Completable.create { api.signUp(firstName, lastName, email, password, phone, RxCallbackCompletable(it)) }
    }

    override fun signIn(email: String, authToken: String): Completable {
        return Completable.create { api.signIn(email, authToken, RxCallbackCompletable(it)) }
    }

    override fun signOut(): Completable {
        return Completable.create { api.signOut(RxCallbackCompletable(it)) }
    }

    override fun isLoggedIn(): Single<Boolean> {
        return Single.create<Boolean> { api.isLoggedIn(RxCallbackSingle<Boolean>(it)) }
    }

    override fun forgotPassword(email: String): Completable {
        return Completable.create {
            api.forgotPassword(email, RxCallbackCompletable(it))
        }
    }

    override fun getCustomer(): Single<Customer> {
        return Single.create { api.getCustomer(RxCallbackSingle<Customer>(it)) }
    }

    override fun createCustomerAddress(address: Address): Single<String> {
        return Single.create { api.createCustomerAddress(address, RxCallbackSingle<String>(it)) }
    }

    override fun editCustomerAddress(addressId: String, address: Address): Completable {
        return Completable.create { api.editCustomerAddress(addressId, address, RxCallbackCompletable(it)) }
    }

    override fun deleteCustomerAddress(addressId: String): Completable {
        return Completable.create { api.deleteCustomerAddress(addressId, RxCallbackCompletable(it)) }
    }

    override fun setDefaultShippingAddress(addressId: String): Completable {
        return Completable.create { api.setDefaultShippingAddress(addressId, RxCallbackCompletable(it)) }
    }

    override fun editCustomer(firstName: String, lastName: String, email: String, phone: String): Single<Customer> {
        return Single.create {
            api.editCustomerInfo(firstName, lastName, email, phone, RxCallbackSingle<Customer>(it))
        }
    }
}