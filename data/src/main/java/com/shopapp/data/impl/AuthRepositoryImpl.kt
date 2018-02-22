package com.shopapp.data.impl

import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
import com.shopapp.gateway.entity.Customer
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.data.rx.RxCallbackCompletable
import com.shopapp.data.rx.RxCallbackSingle
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepositoryImpl(private val api: Api) : AuthRepository {

    override fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String): Completable {
        return Completable.create { api.signUp(firstName, lastName, email, password, phone, RxCallbackCompletable(it)) }
    }

    override fun signIn(email: String, password: String): Completable {
        return Completable.create { api.signIn(email, password, RxCallbackCompletable(it)) }
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

    override fun getCustomer(): Single<Customer?> {
        return Single.create { api.getCustomer(RxCallbackSingle(it)) }
    }

    override fun createCustomerAddress(address: Address): Single<String> {
        return Single.create { api.createCustomerAddress(address, RxCallbackSingle<String>(it)) }
    }

    override fun getCountries(): Single<List<Country>> {
        return Single.create { api.getCountries(RxCallbackSingle<List<Country>>(it)) }
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

    override fun editCustomer(firstName: String, lastName: String, phone: String): Single<Customer> {
        return Single.create {
            api.editCustomerInfo(firstName, lastName, phone, RxCallbackSingle<Customer>(it))
        }
    }

    override fun changePassword(password: String): Completable {
        return Completable.create { api.changePassword(password, RxCallbackCompletable(it)) }
    }

    override fun updateAccountSettings(isAcceptMarketing: Boolean): Completable {
        return Completable.create { api.updateCustomerSettings(isAcceptMarketing, RxCallbackCompletable(it)) }
    }
}