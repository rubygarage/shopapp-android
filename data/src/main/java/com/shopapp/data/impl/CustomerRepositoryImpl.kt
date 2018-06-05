package com.shopapp.data.impl

import com.shopapp.data.rx.RxCallbackCompletable
import com.shopapp.data.rx.RxCallbackSingle
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
import com.shopapp.gateway.entity.Customer
import io.reactivex.Completable
import io.reactivex.Single

class CustomerRepositoryImpl(private val api: Api) : CustomerRepository {

    override fun getCustomer(): Single<Customer?> {
        return Single.create { api.getCustomer(RxCallbackSingle(it)) }
    }

    override fun addCustomerAddress(address: Address): Completable {
        return Completable.create { api.addCustomerAddress(address, RxCallbackCompletable(it)) }
    }

    override fun getCountries(): Single<List<Country>> {
        return Single.create { api.getCountries(RxCallbackSingle<List<Country>>(it)) }
    }

    override fun updateCustomerAddress(address: Address): Completable {
        return Completable.create { api.updateCustomerAddress(address, RxCallbackCompletable(it)) }
    }

    override fun deleteCustomerAddress(addressId: String): Completable {
        return Completable.create {
            api.deleteCustomerAddress(addressId, RxCallbackCompletable(it))
        }
    }

    override fun setDefaultShippingAddress(addressId: String): Completable {
        return Completable.create {
            api.setDefaultShippingAddress(addressId, RxCallbackCompletable(it))
        }
    }

    override fun updateCustomer(firstName: String, lastName: String, phone: String):
            Single<Customer> {

        return Single.create {
            api.updateCustomer(firstName, lastName, phone, RxCallbackSingle<Customer>(it))
        }
    }

    override fun updatePassword(password: String): Completable {
        return Completable.create { api.updatePassword(password, RxCallbackCompletable(it)) }
    }

    override fun updateCustomerSettings(isAcceptMarketing: Boolean): Completable {
        return Completable.create {
            api.updateCustomerSettings(isAcceptMarketing, RxCallbackCompletable(it))
        }
    }
}